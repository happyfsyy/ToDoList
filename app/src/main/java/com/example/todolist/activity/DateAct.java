package com.example.todolist.activity;

import android.os.Bundle;

import com.example.todolist.R;
import com.example.todolist.listener.OnItemSelectedListener;
import com.example.todolist.utils.ToastUtil;
import com.example.todolist.view.CalendarView;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

public class DateAct extends BaseActivity{
    private CalendarView calendarView;
    private static Calendar calendar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        calendarView=findViewById(R.id.calendar_view);
        calendar=Calendar.getInstance();
        calendarView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(Date selectedDate) {
                ToastUtil.showToast(getYearMonthDay(selectedDate));
            }
        });
    }
    public static String getYearMonthDay(Date date){
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(MyApplication.getContext().getResources().getString(R.string.year_month_day),year,month,day);
    }
}
