package com.example.todolist.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.adapter.ListAdapter;
import com.example.todolist.bean.ListItem;
import com.example.todolist.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListActivity extends BaseActivity implements View.OnClickListener {
    private TextView dateTextView;
    private ImageView emotionImgView;
    private ImageView addImageView;
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
        dateTextView=findViewById(R.id.list_date);
        emotionImgView=findViewById(R.id.list_emotion);
        addImageView=findViewById(R.id.list_add);
        recyclerView=findViewById(R.id.list_recycler_view);

        dateTextView.setOnClickListener(this);
        addImageView.setOnClickListener(this);
        initEmotion();
        initRecyclerView();
    }
    private void initEmotion(){
        //todo 获取Preference当天的状况来设置emotion的图片
    }
    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initDataList();
        adapter=new ListAdapter(dataList);
        recyclerView.setAdapter(adapter);

    }
    private void initDataList(){
        //todo 根据SharedPreference获取当天的数据情况，这里暂时生成10个空的数据试试看
        for(int i=0;i<10;i++){
            dataList.add(createEmptyItem());
        }
    }
    private ListItem createEmptyItem(){
        return new ListItem(null, ListItem.ItemStatus.NO_CONTENT);
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if(viewId==dateTextView.getId()){
            ToastUtil.showToast("进入日历界面");
        }else if(viewId==addImageView.getId()){
            ToastUtil.showToast("添加一个item");
        }
    }
}
