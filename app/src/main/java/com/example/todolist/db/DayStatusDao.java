package com.example.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.todolist.activity.MyApplication;
import com.example.todolist.bean.DayStatus;
import com.example.todolist.utils.DataUtil;
import com.example.todolist.utils.DateUtil;
import com.example.todolist.utils.LogUtil;

import java.util.Calendar;
import java.util.Date;

public class DayStatusDao {
    private static MyOpenHelper dbHelper=new MyOpenHelper(MyApplication.getContext(),"list.db",null,4);
    private static Calendar calendar=Calendar.getInstance();
    public static long insertDayStatus(DayStatus dayStatus){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        ContentValues values= DataUtil.getDayStatusCV(dayStatus);
        return database.insert("DayStatus",null,values);
    }
    public static int queryStatus(String time){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("DayStatus",new String[]{"status"},"time=?", new String[]{time},null,null,null);
        int status=-1;
        while(cursor.moveToNext()){
            status=cursor.getInt(cursor.getColumnIndex("status"));
        }
        cursor.close();
        return status;
    }
    public static int[] queryMonthStatus(Date date){
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        //当月天数，记录天数,优秀的，一般的，糟糕的，未记录的，最优秀的那一天，最糟糕的一天
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        int dayNum=calendar.get(Calendar.DAY_OF_MONTH);
        int recordDay=0,good=0,ordinary=0,bad=0,notRecord=0,bestDay=0,worstDay=0;
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("DayStatus",null,"year=? and month=?",new String[]{year+"",month+""},null,null,null);
        float bestRatio=0,worstRatio=1;
        while(cursor.moveToNext()){
            recordDay+=1;
            int status=cursor.getInt(cursor.getColumnIndex("status"));
            float ratio=cursor.getFloat(cursor.getColumnIndex("ratio"));
            int day=cursor.getInt(cursor.getColumnIndex("day"));
            LogUtil.e("day="+day);
            if(ratio>bestRatio){
                bestRatio=ratio;
                bestDay=day;
            }
            if(ratio<worstRatio){
                worstRatio=ratio;
                worstDay=day;
            }
            if(status==DayStatus.GOOD){
                good+=1;
            }else if(status==DayStatus.ORDINARY){
                ordinary+=1;
            }else if(status==DayStatus.BAD){
                bad+=1;
            }
        }
        cursor.close();
        notRecord=dayNum-recordDay;
        int[]monthParams=new int[8];
        monthParams[0]=dayNum;monthParams[1]=recordDay;monthParams[2]=good;monthParams[3]=ordinary;
        monthParams[4]=bad;monthParams[5]=notRecord;monthParams[6]=bestDay;monthParams[7]=worstDay;
        return monthParams;
    }
}
