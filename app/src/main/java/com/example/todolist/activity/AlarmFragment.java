package com.example.todolist.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.todolist.R;
import com.example.todolist.adapter.AlarmAdapter;
import com.example.todolist.bean.DayStatus;
import com.example.todolist.bean.ListItem;
import com.example.todolist.db.DayStatusDao;
import com.example.todolist.db.ListItemDao;
import com.example.todolist.listener.OnToggleClickListener;
import com.example.todolist.bean.AlarmItem;
import com.example.todolist.db.AlarmItemDao;
import com.example.todolist.listener.AlarmOnItemSelectedListener;
import com.example.todolist.utils.DateUtil;
import com.example.todolist.utils.DisplayUtil;
import com.example.todolist.utils.LogUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmFragment extends Fragment {
    private SharedPreferences preferences;
    private Context context;
    private View mainLayout;
    private RelativeLayout clockLayout;
    private RecyclerView recyclerView;
    private List<AlarmItem> dataList;
    private AlarmAdapter adapter;
    private FloatingActionButton add;
    private int selectedIndex;
    public static final int EDIT_ALARM=131;
    public static final int ADD_ALARM=132;
    public static final int DEL_ALARM=133;
    private Calendar calendar;
    private Intent intent;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    @Override
    public void onAttach(@NonNull Context context) {
        this.context=context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainLayout=inflater.inflate(R.layout.activity_alarm,container,false);
        initParams();
        initViews();
        return mainLayout;
    }
    private void initParams(){
        manager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        calendar=Calendar.getInstance();
        preferences=context.getSharedPreferences("list",Context.MODE_PRIVATE);
    }

    private void initViews(){
        clockLayout=mainLayout.findViewById(R.id.clock_layout);
        recyclerView=mainLayout.findViewById(R.id.alarm_recyclerview);
        add=mainLayout.findViewById(R.id.alarm_add);
        initAdd();
        initClockLayout();
        initRecyclerView();
    }
    private void initAdd(){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EditAlarmAct.class);
                intent.putExtra("requestCode",ADD_ALARM);
                startActivityForResult(intent,ADD_ALARM);
            }
        });
    }
    private void initClockLayout(){
        int height= (int)(DisplayUtil.getScreenWidth()*0.8f);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        clockLayout.setLayoutParams(params);
    }
    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        initList();
        adapter=new AlarmAdapter(context,dataList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemSelectedListener(new AlarmOnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                selectedIndex=index;
                String time=dataList.get(index).getTime();
                int hour=getHour(time);
                int minute=getMinute(time);
                LogUtil.e("hour="+hour+"\tminute="+minute);
                Intent intent=new Intent(context,EditAlarmAct.class);
                intent.putExtra("requestCode",EDIT_ALARM);
                intent.putExtra("hour",hour);
                intent.putExtra("minute", minute);
                intent.putExtra("note",dataList.get(index).getNote());
                startActivityForResult(intent,EDIT_ALARM);
            }
        });
        adapter.setToggleClickListener(new OnToggleClickListener() {
            @Override
            public void onToggleClick(int index) {
                boolean isOpen=dataList.get(index).isOpen();
                long id=dataList.get(index).getId();
                String note=dataList.get(index).getNote();
                AlarmItemDao.updateAlarmItem(id,!isOpen);
                dataList.get(index).setOpen(!isOpen);
                if(isOpen){
                    cancelAlarm(note,id);
                }else{
                    String time=dataList.get(index).getTime();
                    int hour=getHour(time);
                    int minute=getMinute(time);
                    startAlarm(hour,minute,note,id);
                }
                adapter.notifyItemChanged(index);
            }
        });
    }
    private void initList(){
        if(preferences.getBoolean("isFirst",true)){
            AlarmItem item=new AlarmItem("20:00",getString(R.string.new_alarm_note),true);
            long id=AlarmItemDao.insertAlarmItem(item);
            item.setId(id);
            startAlarm(20,0,item.getNote(),item.getId());
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("isFirst",false);
            editor.apply();

            dataList=new ArrayList<>();
            dataList.add(item);
        }else{
            dataList=AlarmItemDao.queryAllItems();
            if(dataList.size()==0){
                AlarmItem item=createEmptyAlarmItem();
                dataList.add(item);
            }else{
                for(int i=0;i<dataList.size();i++){
                    dataList.get(i).setType(AlarmAdapter.TYPE_NORMAL);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==Activity.RESULT_OK){
            int hour=data.getIntExtra("hour",0);
            int minute=data.getIntExtra("minute",0);
            String note=data.getStringExtra("note");
            int type=data.getIntExtra("type",0);
            String time;
            switch (requestCode){
                case ADD_ALARM:
                    time= DateUtil.getHourAndMinute(hour,minute);;
                    AlarmItem item=createNormalAlarmItem(time,note);
                    startAlarm(hour,minute,item.getNote(),item.getId());
                    if(dataList.size()==1&&dataList.get(0).getType()==AlarmAdapter.TYPE_EMPTY){
                        dataList.remove(0);
                        dataList.add(item);
                        adapter.notifyItemChanged(0);
                    }else{
                        dataList.add(item);
                        adapter.notifyItemInserted(dataList.size()-1);
                    }
                    break;
                case EDIT_ALARM:
                    if(type== EDIT_ALARM){
                        time= DateUtil.getHourAndMinute(hour,minute);;
                        AlarmItemDao.updateAlarmItem(dataList.get(selectedIndex).getId(),time,note);
                        dataList.get(selectedIndex).setTime(time);
                        dataList.get(selectedIndex).setNote(note);
                        adapter.notifyItemChanged(selectedIndex);
                        if(dataList.get(selectedIndex).isOpen()) {
                            //这里根据id会直接覆盖掉原有的闹钟，如果不行，就先cancel这个闹钟，新建一个闹钟
                            startAlarm(hour, minute, note,dataList.get(selectedIndex).getId());
                        }
                    }else if(type==DEL_ALARM){
                        if(dataList.get(selectedIndex).isOpen()){
                            cancelAlarm(dataList.get(selectedIndex).getNote(),dataList.get(selectedIndex).getId());
                        }
                        AlarmItemDao.deleteAlarmItem(dataList.get(selectedIndex).getId());
                        dataList.remove(selectedIndex);
                        if(dataList.size()==0){
                            dataList.add(createEmptyAlarmItem());
                            adapter.notifyItemChanged(selectedIndex);
                        }else{
                            adapter.notifyItemRemoved(selectedIndex);
                        }
                    }
                    break;
                default:
            }
        }else{
            LogUtil.e("resultCode="+resultCode);
        }
    }

    private AlarmItem createNormalAlarmItem(String time,String note){
        AlarmItem item=new AlarmItem(time,note,true);
        long id=AlarmItemDao.insertAlarmItem(item);
        item.setId(id);
        item.setType(AlarmAdapter.TYPE_NORMAL);
        return item;
    }
    private AlarmItem createEmptyAlarmItem(){
        AlarmItem item=new AlarmItem(AlarmAdapter.TYPE_EMPTY);
        return item;
    }
    private void startAlarm(int hour,int minute,String note,long a_id){
        intent=new Intent("com.example.todolist.alarm");
        intent.putExtra("note",note);
        intent.setComponent(new ComponentName(context.getPackageName(),context.getPackageName()+".receiver.AlarmReceiver"));
        intent.setPackage(context.getPackageName());
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        pendingIntent=PendingIntent.getBroadcast(context,(int)a_id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        long selectTime=calendar.getTimeInMillis();
        if(selectTime<System.currentTimeMillis()){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            selectTime=calendar.getTimeInMillis();
        }
        long oneDay=24*60*60*1000;
        manager.setRepeating(AlarmManager.RTC_WAKEUP,selectTime,oneDay,pendingIntent);
    }
    private void cancelAlarm(String note,long a_id){
        //如果这里不行，就保存一个pendingIntent的list
        intent=new Intent("com.example.todolist.alarm");
        intent.putExtra("note",note);
        intent.setComponent(new ComponentName(context.getPackageName(),context.getPackageName()+".receiver.AlarmReceiver"));
        intent.setPackage(context.getPackageName());
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        pendingIntent=PendingIntent.getBroadcast(context,(int)a_id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }
    private int getHour(String time){
        return Integer.parseInt(time.substring(0,2));
    }
    private int getMinute(String time){
        return Integer.parseInt(time.substring(3,5));
    }
}
