package com.example.todolist.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.todolist.R;
import com.example.todolist.adapter.AlarmAdapter;
import com.example.todolist.bean.AlarmItem;
import com.example.todolist.bean.ListItem;
import com.example.todolist.db.AlarmItemDao;
import com.example.todolist.utils.DateUtil;
import com.example.todolist.utils.DisplayUtil;
import com.example.todolist.utils.LogUtil;
import com.example.todolist.utils.ToastUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
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
                startActivity(intent);
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
    }
    private void initList(){
        if(preferences.getBoolean("isFirst",true)){
            AlarmItem item=new AlarmItem("20:00",getString(R.string.new_alarm_note),true);
            long id=AlarmItemDao.insertAlarmItem(item);
            item.setId(id);

            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("isFirst",false);
            editor.apply();

            dataList=new ArrayList<>();
            dataList.add(item);
        }else{
            dataList=AlarmItemDao.queryAllItems();
        }
    }
}
