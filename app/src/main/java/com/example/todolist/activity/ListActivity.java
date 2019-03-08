package com.example.todolist.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.adapter.ListAdapter;
import com.example.todolist.bean.DayStatus;
import com.example.todolist.bean.ListItem;
import com.example.todolist.db.DayStatusDao;
import com.example.todolist.db.ListItemDao;
import com.example.todolist.db.MyOpenHelper;
import com.example.todolist.listener.OnBackPressListener;
import com.example.todolist.listener.OnClickListener;
import com.example.todolist.listener.OnNextListener;
import com.example.todolist.listener.OnTextChangeListener;
import com.example.todolist.utils.DataUtil;
import com.example.todolist.utils.DateUtil;
import com.example.todolist.utils.SoftKeyboardUtil;
import com.example.todolist.utils.ToastUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private TextView dateTextView;
    private ImageView emotion;
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private List<ListItem> dataList=new ArrayList<>();
    private Calendar calendar;
    private Date date;
    private String time;
    private MyOpenHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initViews();
    }
    private void initViews(){
        toolbar=findViewById(R.id.list_toolbar);
        dateLayout=findViewById(R.id.list_date_layout);
        dateTextView=findViewById(R.id.list_date);
        emotion=findViewById(R.id.list_emotion);
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
        dbHelper=new MyOpenHelper(ListActivity.this,"list.db",null,1);
        database=dbHelper.getWritableDatabase();
        calendar=Calendar.getInstance();
        date=calendar.getTime();
        time=DateUtil.dateToString(date);
        dateTextView.setText(time);
        //todo 根据数据库获取当天的状况,需不需要开启线程
        int status= DayStatusDao.queryStatus(time);
        if(status==DayStatus.BAD){
            //todo 设置emotion的图片
        }else if(status==DayStatus.ORDINARY){

        }else if(status==DayStatus.GOOD){

        }else{
            throw new IllegalStateException("获得的status不是三种心情之一，status="+status);
        }

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("向表中添加数据试试看");
                DayStatus dayStatus=new DayStatus(time,DayStatus.BAD);
                dbHelper.getWritableDatabase();
                SQLiteDatabase database=dbHelper.getWritableDatabase();
                ContentValues values= DataUtil.getDayStatusCV(dayStatus);
                database.insert("DayStatus",null,values);
            }
        });
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
                markStatus(position, ListItem.FINISH);
                addEmptyItem(position);
            }
        });
        adapter.setOnUnFinishListener(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                markStatus(position, ListItem.UNFINISH);
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
                        int firstPos=((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                        int childCount=recyclerView.getChildCount();
                        for(int i=pos-firstPos+1;i<childCount;i++){
                            //这里的i是真实的可见的realPos
                            if(dataList.get(i+firstPos).getStatus()== ListItem.NO_CONTENT||
                                    dataList.get(i+firstPos).getStatus()==ListItem.NO_RECORD){
                                String content=dataList.get(i+firstPos).getContent();
                                View itemView=recyclerView.getChildAt(i);
                                ListAdapter.ViewHolder viewHolder=(ListAdapter.ViewHolder)recyclerView.getChildViewHolder(itemView);
                                viewHolder.content_edit.requestFocus();
                                if(content==null){
                                    viewHolder.content_edit.setSelection(0);
                                }else{
                                    viewHolder.content_edit.setSelection(content.length());
                                }
//                                SoftKeyboardUtil.hideKeyboard(ListActivity.this);
                                break;
                            }
                        }
                    }
                }
            }
        });
        adapter.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(Editable s, int pos) {
                ListAdapter.ViewHolder viewHolder=(ListAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(pos);
                if(!TextUtils.isEmpty(s.toString())){
                    //todo 这里的dataList与ListAdapter中的dataList不一致怎么办
                    if(viewHolder!=null){
                        viewHolder.finish.setVisibility(View.VISIBLE);
                        viewHolder.unFinish.setVisibility(View.VISIBLE);
                    }
                    if(dataList.get(pos).getStatus()== ListItem.NO_RECORD||
                        dataList.get(pos).getStatus()==ListItem.NO_CONTENT){
                        dataList.get(pos).setStatus(ListItem.NO_RECORD);
                        dataList.get(pos).setContent(s.toString());
                    }
                    ContentValues values=DataUtil.generateCV(s.toString(),ListItem.NO_RECORD);
                    ListItemDao.updateItem(dataList.get(pos).getId(),values);
                }else{
                    if(viewHolder!=null){
                        viewHolder.finish.setVisibility(View.INVISIBLE);
                        viewHolder.unFinish.setVisibility(View.INVISIBLE);
                    }
                    dataList.get(pos).setStatus(ListItem.NO_CONTENT);
                    dataList.get(pos).setContent("");
                    ContentValues values=DataUtil.generateCV("",ListItem.NO_CONTENT);
                    ListItemDao.updateItem(dataList.get(pos).getId(),values);
                }
            }
        });
        adapter.setOnBackPressListener(new OnBackPressListener() {
            @Override
            public void onBackPress(EditText editText, int pos) {
                int startSelection=editText.getSelectionStart();
                if(startSelection==0){
                    //todo 如果list只有一项，那么不能删除
                    //todo 如果只有这一项是no_record，也不能删除
                    if(pos-1>=0){
                        String curContent=dataList.get(pos).getContent();
                        int preStatus=dataList.get(pos-1).getStatus();
                        String preContent=dataList.get(pos-1).getContent();
                        if(preStatus== ListItem.NO_CONTENT||
                                preStatus== ListItem.NO_RECORD){
                            String content=preContent+curContent;
                            dataList.get(pos-1).setContent(content);
                            dataList.get(pos-1).setStatus(ListItem.NO_RECORD);
                            adapter.notifyItemChanged(pos-1);
                            ContentValues values=DataUtil.generateCV(content,ListItem.NO_RECORD);
                            ListItemDao.updateItem(dataList.get(pos-1).getId(),values);
                        }
                    }
                    dataList.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    ListItemDao.deleteItem(dataList.get(pos).getId());

                    Snackbar.make(recyclerView,"已删除当前项",Snackbar.LENGTH_SHORT)
                            .setAction("撤销删除", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //todo 撤销删除
                                    ToastUtil.showToast("撤销删除太麻烦");

                                }
                            }).show();
                }
            }
        });

    }
    private void initDataList(){
        dataList= ListItemDao.queryAllItems(time);
    }
    private ListItem createEmptyItem(){
        return new ListItem("", ListItem.NO_CONTENT,time);
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
            ListItem listItem=createEmptyItem();
            dataList.add(listItem);
            adapter.notifyItemInserted(dataList.size()-1);
            long id=ListItemDao.insertListItem(listItem);
            dataList.get(dataList.size()-1).setId(id);
        }
    }

    /**
     * 标记当前项的状态，完成/未完成
     * @param pos
     * @param status
     */
    private void markStatus(int pos, int status){
        dataList.get(pos).setStatus(status);
        adapter.notifyItemChanged(pos);
        ContentValues values=DataUtil.generateCV(status);
        ListItemDao.updateItem(dataList.get(pos).getId(),values);
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
