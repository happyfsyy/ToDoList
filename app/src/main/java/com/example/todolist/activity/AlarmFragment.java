package com.example.todolist.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.todolist.R;
import com.example.todolist.utils.DisplayUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmFragment extends Fragment {
    private Context context;
    private View mainLayout;
    private RelativeLayout clockLayout;
    private RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context=context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainLayout=inflater.inflate(R.layout.activity_alarm,container,false);
        initViews();
        return mainLayout;
    }

    private void initViews(){
        clockLayout=mainLayout.findViewById(R.id.clock_layout);
        recyclerView=mainLayout.findViewById(R.id.alarm_recyclerview);
        initClockLayout();
    }

    private void initClockLayout(){
        int height= (int)(DisplayUtil.getScreenWidth()*0.8f);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        clockLayout.setLayoutParams(params);
    }
    private void initRecyclerView(){

    }
}
