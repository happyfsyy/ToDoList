package com.example.todolist.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.example.todolist.utils.DateUtil;
import com.example.todolist.utils.DisplayUtil;
import com.example.todolist.utils.LogUtil;
import com.example.todolist.utils.ToastUtil;
import com.example.todolist.view.CalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class DateFragment extends Fragment {
    private View mainLayout;
    private Context context;
    private TextView yearAndMonth;
    private static Calendar calendar;
    private ViewPager viewPager;
    private CalendarView[] calendarViews=new CalendarView[3];
    private Date tempDate=new Date();
    private CalendarPagerAdapter adapter;
    private int currentImgIndex;
    private boolean isChanged=false;
    private TextView headerText;
    private RecyclerView recyclerView;
    private List<ListItem> dataList;
    private DateAdapter dateAdapter;
    private OnItemSelectedListener onItemSelectedListener;
    private SpannableStringBuilder builder;
    private ForegroundColorSpan colorSpan;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context=context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainLayout=inflater.inflate(R.layout.activity_date,container,false);
        initViews();
        return mainLayout;
    }

    private void initViews(){
        calendar=Calendar.getInstance();
        viewPager=mainLayout.findViewById(R.id.date_viewpager);
        yearAndMonth=mainLayout.findViewById(R.id.date_year_month);
        headerText=mainLayout.findViewById(R.id.date_item_header_text);
        recyclerView=mainLayout.findViewById(R.id.date_recyclerView);
        initYearAndMonthHeader();
        initCalendarViews();
        initViewPager();
        initHeaderText(new Date());
        initRecyclerView();
    }
    private void initYearAndMonthHeader(){
        String yearAndMonthText=DateUtil.getYearAndMonth(tempDate);
        yearAndMonth.setText(yearAndMonthText);
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
        onItemSelectedListener=new OnItemSelectedListener() {
            @Override
            public void onItemSelected(Date selectedDate) {
                LogUtil.e("换日期了:"+DateUtil.getYearMonthDayNumberic(selectedDate));
                initHeaderText(selectedDate);

                Date lastMonthDate=DateUtil.getLastMonthDate(selectedDate);
                Date nextMonthDate=DateUtil.getNextMonthDate(selectedDate);
                calendarViews[0].setSelectedDate(lastMonthDate);
                calendarViews[2].setSelectedDate(nextMonthDate);

                dataList.clear();
                List<ListItem> list= ListItemDao.queryAllItemsExceptNoContent(DateUtil.getYearMonthDayNumberic(selectedDate));
                dataList.addAll(list);
                dateAdapter.notifyDataSetChanged();
            }
        };
        for(int i=0;i<3;i++){
            calendarViews[i]=new CalendarView(context);
            Date curMonthDate=calendar.getTime();
            Date lastMonthDate=DateUtil.getLastMonthDate(curMonthDate);
            Date nextMonthDate=DateUtil.getNextMonthDate(curMonthDate);
            if(i==0){
                calendarViews[i].setCurMonthAndSelectedDate(lastMonthDate,lastMonthDate,false);
            }else if(i==1){
                calendarViews[i].setCurMonthAndSelectedDate(curMonthDate,curMonthDate,false);
            }else{
                calendarViews[i].setCurMonthAndSelectedDate(nextMonthDate,nextMonthDate,false);
            }
        }
        calendarViews[1].setOnItemSelectedListener(onItemSelectedListener);
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
                LogUtil.e("onPageSelected,position="+position);
                currentImgIndex=position;
                if(currentImgIndex==2){
                    clickNext();
                    LogUtil.e("currentImgIndex=2");
                    isChanged=true;
                }else if(currentImgIndex==0){
                    clickLast();
                    isChanged=true;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtil.e("onPageScrolled");
                if(isChanged&&state==ViewPager.SCROLL_STATE_IDLE){
                    setCurMonthAndSelectedDate();

                    LogUtil.e("onPageScrolled,setCurrentItem1");
                    viewPager.setCurrentItem(1,false);
                    isChanged=false;
                    onItemSelectedListener.onItemSelected(calendarViews[1].getSelectedDate());
                    initHeaderText(calendarViews[1].getSelectedDate());
                }
            }
        });
    }
    private void setCurMonthAndSelectedDate(){
        Date curMonthDate=calendar.getTime();
        Date lastMonthDate=DateUtil.getLastMonthDate(curMonthDate);
        Date nextMonthDate=DateUtil.getNextMonthDate(curMonthDate);

        Date lastSelectedDate0=calendarViews[0].getSelectedDate();
        Date lastSelectedDate1=calendarViews[1].getSelectedDate();
        Date lastSelectedDate2=calendarViews[2].getSelectedDate();
        Date curSelectedDate0,curSelectedDate1,curSelectedDate2;
        if(currentImgIndex==2){
            curSelectedDate0=DateUtil.getNextMonthDate(lastSelectedDate0);
            curSelectedDate1=DateUtil.getNextMonthDate(lastSelectedDate1);
            curSelectedDate2=DateUtil.getNextMonthDate(lastSelectedDate2);
        }else{
            curSelectedDate0=DateUtil.getLastMonthDate(lastSelectedDate0);
            curSelectedDate1=DateUtil.getLastMonthDate(lastSelectedDate1);
            curSelectedDate2=DateUtil.getLastMonthDate(lastSelectedDate2);
        }
        calendarViews[0].setCurMonthAndSelectedDate(lastMonthDate,curSelectedDate0,true);
        calendarViews[1].setCurMonthAndSelectedDate(curMonthDate,curSelectedDate1,true);
        calendarViews[2].setCurMonthAndSelectedDate(nextMonthDate,curSelectedDate2,true);
    }
    private void initHeaderText(Date date){
        String text=DateUtil.getCompareTodayText(date);
        String headerTextString=String.format(getResources().getString(R.string.date_item_header),text);
        if(builder==null){
            builder=new SpannableStringBuilder(headerTextString);
            colorSpan=new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        }
        builder.clear();
        builder.append(headerTextString);
        builder.setSpan(colorSpan,7,headerTextString.length()-2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        headerText.setText(builder);
    }
    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        tempDate=new Date();
        dataList= ListItemDao.queryAllItemsExceptNoContent(DateUtil.getYearMonthDayNumberic(tempDate));
        dateAdapter=new DateAdapter(context,dataList);
        recyclerView.setAdapter(dateAdapter);
    }
}