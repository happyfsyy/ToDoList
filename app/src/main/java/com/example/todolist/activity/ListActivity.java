package com.example.todolist.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.adapter.ListAdapter;
import com.example.todolist.bean.ListItem;
import com.example.todolist.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListActivity extends BaseActivity{
    private Toolbar toolbar;
    private LinearLayout dateLayout;
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private List<ListItem> dataList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initViews();
    }
    private void initViews(){
        toolbar=findViewById(R.id.list_toolbar);
        dateLayout=findViewById(R.id.list_date_layout);
        recyclerView=findViewById(R.id.list_recycler_view);

        initToolbar();
        initDateLayout();
        initRecyclerView();
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
    private void initDateLayout(){
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("跳转到日历界面");
            }
        });
        //todo 获取当前日期
        //todo 根据数据库获取当天的状况来设置emotion的图片
    }
    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initDataList();
        adapter=new ListAdapter(dataList);
        recyclerView.setAdapter(adapter);

    }
    private void initDataList(){
        //todo 根据数据库获取当天的数据情况，这里暂时生成10个空的数据试试看
        for(int i=0;i<1;i++){
            dataList.add(createEmptyItem());
        }
    }
    private ListItem createEmptyItem(){
        return new ListItem(null, ListItem.ItemStatus.NO_CONTENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.list_settings:
                ToastUtil.showToast("跳转到闹钟设置界面");
                break;
        }
        return true;
    }
}
