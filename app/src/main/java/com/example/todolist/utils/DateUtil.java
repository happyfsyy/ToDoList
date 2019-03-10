package com.example.todolist.utils;

import android.content.res.Resources;

import com.example.todolist.R;
import com.example.todolist.activity.MyApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static Resources resources= MyApplication.getContext().getResources();
//    private static SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy/MM/dd");
    private static Calendar calendar=Calendar.getInstance();
    public static String getYearMonthDayNumberic(Date date){
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(resources.getString(R.string.year_month_day_numberic),year,month,day);
    }
    public static String getYearAndMonth(Date date){
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        return String.format(resources.getString(R.string.year_and_month),year,month);
    }
    public static String getYearMonthDay(Date date){
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(resources.getString(R.string.year_month_day),year,month,day);
    }
}
