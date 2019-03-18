package com.example.todolist.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.example.todolist.R;
import com.example.todolist.bean.DayStatus;
import com.example.todolist.db.DayStatusDao;
import com.example.todolist.db.ListItemDao;
import com.example.todolist.receiver.MyService;
import com.example.todolist.utils.LogUtil;
import com.example.todolist.utils.ToastUtil;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private View homeTab;
    private View calendarTab;
    private View graphTab;
    private View alarmTab;
    private FragmentManager fragmentManager;
    private ListFragment homeFragment;
    private DateFragment calendarFragment;
    private GraphFragment graphFragment;
    private AlarmFragment alarmFragment;
    private static final String[] FRAGMENT_TAGS=
            new String[]{"homeFragment","calendarFragment","graphFragment","alarmFragment"};
    private int savedIndex=0;
    private long preTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager=getSupportFragmentManager();
        initLastData();
        initViews();
        initToolbar();
        initListener();
        if(savedInstanceState!=null){
            savedIndex=savedInstanceState.getInt("savedIndex");
        }
        setTabSelection(savedIndex);
    }
    private void initLastData(){
        SharedPreferences preferences=getSharedPreferences("list",MODE_PRIVATE);
        int lastVisitYear=preferences.getInt("lastVisitYear",0);
        int lastVisitMonth=preferences.getInt("lastVisitMonth",0);
        int lastVisitDay=preferences.getInt("lastVisitDay",0);
        Calendar tempCalendar=Calendar.getInstance();
        int nowYear=tempCalendar.get(Calendar.YEAR);
        int nowMonth=tempCalendar.get(Calendar.MONTH)+1;
        int nowDay=tempCalendar.get(Calendar.DAY_OF_MONTH);
        if(lastVisitYear!=0){
            if(lastVisitYear!=nowYear||lastVisitMonth!=nowMonth||lastVisitDay!=nowDay){
                String time=String.format(getString(R.string.year_month_day_numberic),lastVisitYear,lastVisitMonth,lastVisitDay);
                DayStatus dayStatus=ListItemDao.updateNoRecord(time);
                if(dayStatus!=null){
                    DayStatusDao.insertDayStatus(dayStatus);
                }
            }
        }
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("lastVisitYear",nowYear);
        editor.putInt("lastVisitMonth",nowMonth);
        editor.putInt("lastVisitDay",nowDay);
        editor.apply();
    }
    private void initViews(){
        toolbar=findViewById(R.id.main_toolbar);
        homeTab=findViewById(R.id.home_tab);
        calendarTab=findViewById(R.id.calendar_tab);
        graphTab=findViewById(R.id.graph_tab);
        alarmTab=findViewById(R.id.alarm_tab);
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
    }

    private void initListener(){
        homeTab.setOnClickListener(this);
        calendarTab.setOnClickListener(this);
        graphTab.setOnClickListener(this);
        alarmTab.setOnClickListener(this);
    }
    private void setTabSelection(int index){
        clearSelection();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index){
            case 0:
                savedIndex=0;
                toolbar.setTitle(getResources().getString(R.string.home));
                homeTab.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                if(homeFragment==null){
                    homeFragment=new ListFragment();
                    transaction.add(R.id.main_content,homeFragment,FRAGMENT_TAGS[0]);
                }else{
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                savedIndex=1;
                toolbar.setTitle(getResources().getString(R.string.calendar));
                calendarTab.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                if(calendarFragment==null){
                    calendarFragment=new DateFragment();
                    transaction.add(R.id.main_content,calendarFragment,FRAGMENT_TAGS[1]);
                }else{
                    transaction.show(calendarFragment);
                }
                break;
            case 2:
                savedIndex=2;
                toolbar.setTitle(getResources().getString(R.string.graph));
                graphTab.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                if(graphFragment==null){
                    graphFragment=new GraphFragment();
                    transaction.add(R.id.main_content,graphFragment,FRAGMENT_TAGS[2]);
                }else{
                    transaction.show(graphFragment);
                }
                break;
            case 3:
            default:
                savedIndex=3;
                toolbar.setTitle(getResources().getString(R.string.alarm));
                alarmTab.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                if(alarmFragment==null){
                    alarmFragment=new AlarmFragment();
                    transaction.add(R.id.main_content,alarmFragment,FRAGMENT_TAGS[3]);
                }else{
                    transaction.show(alarmFragment);
                }
                break;
        }
        transaction.commit();
    }
    private void clearSelection(){
        homeTab.setBackgroundColor(getResources().getColor(R.color.color_white));
        calendarTab.setBackgroundColor(getResources().getColor(R.color.color_white));
        graphTab.setBackgroundColor(getResources().getColor(R.color.color_white));
        alarmTab.setBackgroundColor(getResources().getColor(R.color.color_white));
    }
    private void hideFragments(FragmentTransaction transaction){
        if(homeFragment!=null){
            transaction.hide(homeFragment);
        }
        if(calendarFragment!=null){
            transaction.hide(calendarFragment);
        }
        if(graphFragment!=null){
            transaction.hide(graphFragment);
        }
        if(alarmFragment!=null){
            transaction.hide(alarmFragment);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_tab:
                setTabSelection(0);
                break;
            case R.id.calendar_tab:
                setTabSelection(1);
                break;
            case R.id.graph_tab:
                setTabSelection(2);
                break;
            case R.id.alarm_tab:
                setTabSelection(3);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("savedIndex",savedIndex);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long curTime=System.currentTimeMillis();
            if ((curTime - preTime) > 1000 * 2) {
                ToastUtil.showToast("再按一次退出程序");
                preTime = curTime;
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
