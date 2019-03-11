package com.example.todolist.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.adapter.CalendarPagerAdapter;
import com.example.todolist.adapter.DateAdapter;
import com.example.todolist.bean.ListItem;
import com.example.todolist.db.ListItemDao;
import com.example.todolist.listener.OnItemSelectedListener;
import com.example.todolist.utils.DataUtil;
import com.example.todolist.utils.DateUtil;
import com.example.todolist.utils.DisplayUtil;
import com.example.todolist.utils.ToastUtil;
import com.example.todolist.view.CalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class DateAct extends BaseActivity{
    private ImageView lastMonth;
    private ImageView nextMonth;
    private TextView yearAndMonth;
    private static Calendar calendar;
    private ViewPager viewPager;
    private CalendarView[] calendarViews=new CalendarView[3];
    private Date tempDate=new Date();
    private CalendarPagerAdapter adapter;
    private int currentImgIndex;
    private boolean isChanged=false;
    private RecyclerView recyclerView;
    private List<ListItem> dataList;
    private DateAdapter dateAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        initViews();
    }
    private void initViews(){
        calendar=Calendar.getInstance();
        viewPager=findViewById(R.id.date_viewpager);
        lastMonth=findViewById(R.id.date_last_month);
        nextMonth=findViewById(R.id.date_next_month);
        yearAndMonth=findViewById(R.id.date_year_month);
        recyclerView=findViewById(R.id.date_recyclerView);
        initYearAndMonthHeader();
        initCalendarViews();
        initViewPager();
        initRecyclerView();
    }
    private void initYearAndMonthHeader(){
        String yearAndMonthText=DateUtil.getYearAndMonth(tempDate);
        yearAndMonth.setText(yearAndMonthText);
        lastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLast();
                setCalendarViewsDate();
            }
        });
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickNext();
                setCalendarViewsDate();
            }
        });
    }
    private void clickLast(){
        calendar.add(Calendar.MONTH,-1);
        String yearMonthText=DateUtil.getYearAndMonth(calendar.getTime());
        yearAndMonth.setText(yearMonthText);
    }
    private void clickNext(){
        calendar.add(Calendar.MONTH,1);
        String yearMonthText=DateUtil.getYearAndMonth(calendar.getTime());
        yearAndMonth.setText(yearMonthText);
    }
    private void initCalendarViews(){
        for(int i=0;i<3;i++){
            calendarViews[i]=new CalendarView(this);
            calendarViews[i].setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(Date selectedDate) {
                    for(int i=0;i<3;i++){
                        if(calendarViews[i].getSelectedDate().compareTo(selectedDate)!=0){
                            calendarViews[i].setSelectedDate(selectedDate);
                        }
                    }
                    ToastUtil.showToast(DateUtil.getYearMonthDay(selectedDate));
                    //todo private OnItemSelectedListener
                }
            });
        }
        setCalendarViewsDate();
    }
    private void initViewPager(){
        float height=(DisplayUtil.getScreenWidth()-2*DisplayUtil.dp2px(10))*0.8f;
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)height));
        adapter=new CalendarPagerAdapter(calendarViews);
        viewPager.setAdapter(adapter);
        currentImgIndex=1;
        viewPager.setCurrentItem(currentImgIndex);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentImgIndex=position;
                isChanged=true;
                if(currentImgIndex==2){
                    clickNext();
                }else if(currentImgIndex==0){
                    clickLast();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                if(isChanged&&state==ViewPager.SCROLL_STATE_IDLE){
                    setCalendarViewsDate();
                    viewPager.setCurrentItem(1,false);
                    isChanged=false;
                }
            }
        });
    }
    private void setCalendarViewsDate(){
        for(int i=0;i<3;i++){
            calendar.add(Calendar.MONTH,i-1);
            tempDate=calendar.getTime();
            calendarViews[i].setCurMonthDate(tempDate);
            calendar.add(Calendar.MONTH,1-i);
        }
    }
    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList= ListItemDao.queryAllItems(DateUtil.getYearMonthDayNumberic(tempDate));
        dateAdapter=new DateAdapter(this,dataList);
        View headerView= LayoutInflater.from(this).inflate(R.layout.date_header_item,null);
        dateAdapter.addHeaderView(headerView);
        recyclerView.setAdapter(dateAdapter);
    }
}
