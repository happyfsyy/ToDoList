package com.example.todolist.db;

import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.activity.MyApplication;
import com.example.todolist.bean.AlarmItem;
import com.example.todolist.utils.DataUtil;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlarmItemDao {
    private static MyOpenHelper helper=new MyOpenHelper(MyApplication.getContext(),"list.db",null,4);
    public static long insertAlarmItem(AlarmItem item){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values= DataUtil.getAlarmItemCV(item);
        return db.insert("AlarmItem",null,values);
    }
    public static List<AlarmItem> queryAllItems(){
        SQLiteDatabase db=helper.getWritableDatabase();
        List<AlarmItem> list=new ArrayList<>();
        Cursor cursor=db.query("AlarmItem",null,null,null,null,null,"a_id asc");
        while(cursor.moveToNext()){
            String time=cursor.getString(cursor.getColumnIndex("time"));
            String note=cursor.getString(cursor.getColumnIndex("note"));
            boolean isOpen=cursor.getString(cursor.getColumnIndex("isOpen")).equals("1");
            long id=cursor.getLong(cursor.getColumnIndex("a_id"));
            AlarmItem item=new AlarmItem(time,note,isOpen,id);
            list.add(item);
        }
        cursor.close();
        return list;
    }
    public static void deleteAlarmItem(long id){
        SQLiteDatabase db=helper.getWritableDatabase();
        db.delete("AlarmItem","a_id=?",new String[]{id+""});
    }
    public static void updateAlarmItem(long id,String time,String note){
        SQLiteDatabase database= helper.getWritableDatabase();
        ContentValues values=DataUtil.getAlarmItemCV(time,note);
        database.update("AlarmItem",values,"a_id=?",new String[]{id+""});
    }
    public static void updateAlarmItem(long id,boolean isOpen){
        SQLiteDatabase database=helper.getWritableDatabase();
        ContentValues values=DataUtil.getAlarmItemCV(isOpen);
        database.update("AlarmItem",values,"a_id=?",new String[]{id+""});
    }
}
