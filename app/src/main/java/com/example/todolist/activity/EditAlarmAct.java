package com.example.todolist.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.adapter.WheelAdapter;
import com.example.todolist.utils.ToastUtil;
import com.example.todolist.view.WheelView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class EditAlarmAct extends BaseActivity{
    private Toolbar toolbar;
    private ImageView confirm;
    private WheelView hourWheel;
    private WheelView minuteWheel;
    private TextView delAlarm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        initViews();
    }
    private void initViews(){
        toolbar=findViewById(R.id.alarm_toolbar);
        confirm=findViewById(R.id.alarm_confirm);
        hourWheel=findViewById(R.id.hour_wheel);
        minuteWheel=findViewById(R.id.minute_wheel);
        delAlarm=findViewById(R.id.delete_alarm);
        initToolbar();
        initWheels();

    }
    private void initToolbar(){
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("确定咯");
            }
        });
        delAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("删除闹钟");
            }
        });
    }
    private void initWheels(){
        List<String> hourText=new ArrayList<>();
        for(int i=0;i<24;i++){
            hourText.add(String.format(getString(R.string.hour_or_minute),i));
        }
        WheelAdapter hourAdapter=new WheelAdapter(hourText);
        List<String> minuteText=new ArrayList<>();
        for(int i=0;i<60;i++){
            minuteText.add(String.format(getString(R.string.hour_or_minute),i));
        }
        WheelAdapter minuteAdapter=new WheelAdapter(minuteText);
        hourWheel.setAdapter(hourAdapter);
        minuteWheel.setAdapter(minuteAdapter);
    }
}
