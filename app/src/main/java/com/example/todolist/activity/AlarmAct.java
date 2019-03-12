package com.example.todolist.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.todolist.R;
import com.example.todolist.utils.DisplayUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmAct extends BaseActivity{
    private Toolbar toolbar;
    private RelativeLayout clockLayout;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        initViews();
    }
    private void initViews(){
        toolbar=findViewById(R.id.alarm_toolbar);
        clockLayout=findViewById(R.id.clock_layout);
        recyclerView=findViewById(R.id.alarm_recyclerview);
        initToolBar();
        initClockLayout();
    }
    private void initToolBar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initClockLayout(){
        int height= (int)(DisplayUtil.getScreenWidth()*0.8f);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        clockLayout.setLayoutParams(params);
    }
    private void initRecyclerView(){

    }
}
