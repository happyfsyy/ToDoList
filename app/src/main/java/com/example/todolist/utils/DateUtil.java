package com.example.todolist.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy/MM/dd");
    public static String dateToString(Date date){
        return simpleDateFormat.format(date);
    }
}
