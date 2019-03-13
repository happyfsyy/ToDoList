package com.example.todolist.activity;

import android.os.Bundle;
import android.view.View;

import com.example.todolist.R;
import com.example.todolist.utils.LogUtil;
import com.example.todolist.utils.ToastUtil;

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
    private AlarmFragment alarmFragment;
    private static final String[] FRAGMENT_TAGS=
            new String[]{"homeFragment","calendarFragment","graphFragment","alarmFragment"};
    private int savedIndex=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager=getSupportFragmentManager();
        initViews();
        initToolbar();
        initListener();
        if(savedInstanceState!=null){
            savedIndex=savedInstanceState.getInt("savedIndex");
        }
        setTabSelection(savedIndex);

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
//        graphTab.setOnClickListener(this);
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
                //todo graphFragment
                ToastUtil.showToast("还没开始做");
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
        //todo graphFragment
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
}
