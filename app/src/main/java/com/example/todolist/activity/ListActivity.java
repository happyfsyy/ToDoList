package com.example.todolist.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.todolist.R;
import com.example.todolist.adapter.ListAdapter;
import com.example.todolist.bean.ListItem;
import com.example.todolist.listener.OnBackPressListener;
import com.example.todolist.listener.OnClickListener;
import com.example.todolist.listener.OnNextListener;
import com.example.todolist.listener.OnTextChangeListener;
import com.example.todolist.utils.SoftKeyboardUtil;
import com.example.todolist.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnFinishListener(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                markStatus(position, ListItem.ItemStatus.FINISH);
                addEmptyItem(position);
            }
        });
        adapter.setOnUnFinishListener(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                markStatus(position, ListItem.ItemStatus.UNFINISH);
                addEmptyItem(position);
            }
        });
        adapter.setOnNextListener(new OnNextListener() {
            @Override
            public void onNext(int pos) {
                if (!TextUtils.isEmpty(dataList.get(pos).getContent())) {
                    if (isLastItem(pos)) {
                        addEmptyItem(pos);
                        //todo 这里要不要隐藏软键盘
                    } else {
                        //todo 向下寻找focus的editText，向上寻找focus的editText
//                    for(int i=pos+1;i<dataList.size();i++){
//                        if(dataList.get(i).getStatus()== ListItem.ItemStatus.NO_CONTENT||
//                            dataList.get(i).getStatus()==ListItem.ItemStatus.NO_RECORD){
//                            String content=dataList.get(i).getContent();
//                            ListAdapter.ViewHolder viewHolder=(ListAdapter.ViewHolder)recyclerView.findContainingViewHolder(recyclerView.getChildAt(i));
//                            viewHolder.content_edit.requestFocus();
//                            if(content==null){
//                                viewHolder.content_edit.setSelection(0);
//                            }else{
//                                viewHolder.content_edit.setSelection(content.length());
//                            }
//                            SoftKeyboardUtil.hideKeyboard(ListActivity.this);
//                            break;
//                        }
//                    }
                    }
                }
            }
        });
        adapter.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(Editable s, int pos) {
                if(!TextUtils.isEmpty(s.toString())){
                    //todo 这里的dataList与ListAdapter中的dataList不一致怎么办
                    dataList.get(pos).setStatus(ListItem.ItemStatus.NO_RECORD);
                    dataList.get(pos).setContent(s.toString());
                    //todo 对数据库中数据进行更新
                }else{
                    dataList.get(pos).setStatus(ListItem.ItemStatus.NO_CONTENT);
                    dataList.get(pos).setContent("");
                    //todo 对数据库中数据进行更新
                }
            }
        });
        adapter.setOnBackPressListener(new OnBackPressListener() {
            @Override
            public void onBackPress(EditText editText, int pos) {
                int startSelection=editText.getSelectionStart();
                if(startSelection==0){
                    dataList.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    //todo 对数据库中数据进行更新
                }
            }
        });

    }
    private void initDataList(){
//        ListItem item=new ListItem("dd", ListItem.ItemStatus.FINISH);
//        ListItem item1=new ListItem("dd1", ListItem.ItemStatus.FINISH);
//        ListItem item2=new ListItem("dd2", ListItem.ItemStatus.FINISH);
//        ListItem item3=new ListItem("dd3", ListItem.ItemStatus.FINISH);
//        ListItem item4=new ListItem("dd4", ListItem.ItemStatus.FINISH);
//        ListItem item5=new ListItem("dd5", ListItem.ItemStatus.FINISH);
//        ListItem item6=new ListItem("dd6", ListItem.ItemStatus.FINISH);
//        dataList.add(item);dataList.add(item2);dataList.add(item1);
//        dataList.add(item3);dataList.add(item4);dataList.add(item5);dataList.add(item6);
//        ListItem item7=createEmptyItem();dataList.add(item7);
        for(int i=0;i<1;i++){
            ListItem item=createEmptyItem();
            dataList.add(item);
        }
    }
    public static ListItem createEmptyItem(){
        return new ListItem(null, ListItem.ItemStatus.NO_CONTENT);
    }

    /**
     * 判断当前是不是最后一项
     * @param pos
     */
    private boolean isLastItem(int pos){
        return pos==dataList.size()-1;
    }
    /**
     * 如果当前是最后一项，就增加一个item
     * @param pos
     */
    private void addEmptyItem(int pos){
        if(isLastItem(pos)){
            ListItem listItem= ListActivity.createEmptyItem();
            dataList.add(listItem);
            //todo 对数据库中数据进行更新
            adapter.notifyItemInserted(dataList.size()-1);
        }
    }

    /**
     * 标记当前项的状态，完成/未完成
     * @param pos
     * @param status
     */
    private void markStatus(int pos, ListItem.ItemStatus status){
        dataList.get(pos).setStatus(status);
        //todo 对数据库中数据进行更新
        adapter.notifyItemChanged(pos);
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
