package com.example.todolist.utils;

import android.content.ContentValues;

import com.example.todolist.bean.AlarmItem;
import com.example.todolist.bean.DayStatus;
import com.example.todolist.bean.ListItem;

public class DataUtil {
    public static ContentValues getDayStatusCV(DayStatus dayStatus){
        ContentValues values=new ContentValues();
        values.put("time",dayStatus.getTime());
        values.put("status",dayStatus.getStatus());
        return values;
    }
    public static ContentValues getListItemCV(ListItem listItem){
        ContentValues values=new ContentValues();
        values.put("content",listItem.getContent());
        values.put("status",listItem.getStatus());
        values.put("time",listItem.getTime());
        return values;
    }
    public static ContentValues generateCV(String content,int status){
        ContentValues values=new ContentValues();
        values.put("content",content);
        values.put("status",status);
        return values;
    }
    public static ContentValues generateCV(int status){
        ContentValues values=new ContentValues();
        values.put("status",status);
        return values;
    }
    public static ContentValues getAlarmItemCV(AlarmItem item){
        ContentValues values=new ContentValues();
        values.put("time",item.getTime());
        values.put("note",item.getNote());
        values.put("isOpen",item.isOpen());
        return values;
    }
}
