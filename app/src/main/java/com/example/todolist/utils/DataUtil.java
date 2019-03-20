package com.example.todolist.utils;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;

import com.example.todolist.bean.AlarmItem;
import com.example.todolist.bean.DayStatus;
import com.example.todolist.bean.ListItem;

import java.util.concurrent.TimeoutException;

public class DataUtil {
    public static ContentValues getDayStatusCV(DayStatus dayStatus){
        ContentValues values=new ContentValues();
        values.put("time",dayStatus.getTime());
        values.put("status",dayStatus.getStatus());
        values.put("ratio",dayStatus.getRatio());
        values.put("list_num",dayStatus.getListNum());
        values.put("finish_num",dayStatus.getFinishNum());
        values.put("unfinish_num",dayStatus.getUnFinishNum());
        values.put("year",dayStatus.getYear());
        values.put("month",dayStatus.getMonth());
        values.put("day",dayStatus.getDay());
        return values;
    }
    public static DayStatus getDayStatus(String time,int listNum,int finishNum,int unFinishNum){
        int year= Integer.valueOf(time.substring(0,4));
        int month=Integer.valueOf(time.substring(5,7));
        int day= Integer.valueOf(time.substring(8,10));
        LogUtil.e("year="+year+"\tmonth="+month+"\tday="+day);
        float ratio=finishNum*1.0f/listNum;
        int status;
        if(ratio>=0.8f){
            status=DayStatus.GOOD;
        }else if(ratio>=0.5f){
            status=DayStatus.ORDINARY;
        }else{
            status=DayStatus.BAD;
        }
        return new DayStatus(time,status,ratio,listNum,finishNum,unFinishNum,year,month,day);
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
    public static ContentValues getAlarmItemCV(String time,String note){
        ContentValues values=new ContentValues();
        values.put("time",time);
        values.put("note",note);
        return values;
    }
    public static ContentValues getAlarmItemCV(boolean isOpen){
        ContentValues values=new ContentValues();
        values.put("isOpen",isOpen);
        return values;
    }
}
