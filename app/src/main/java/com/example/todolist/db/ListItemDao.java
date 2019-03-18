package com.example.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.activity.MyApplication;
import com.example.todolist.bean.DayStatus;
import com.example.todolist.bean.ListItem;
import com.example.todolist.utils.DataUtil;
import com.example.todolist.utils.DateUtil;
import com.example.todolist.utils.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListItemDao {
    private static MyOpenHelper dbHelper=new MyOpenHelper(MyApplication.getContext(),"list.db",null,4);
    public static long insertListItem(ListItem listItem){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        long id=database.insert("ListItem",null, DataUtil.getListItemCV(listItem));
        return id;
    }
    public static DayStatus updateNoRecord(String time){
        LogUtil.e("我是真正的，开始更新后台数据了");
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("ListItem",null,"time=? and status!=?",new String[]{time,"101"},null,null,"l_id asc");
        int recordNum=0,finishNum=0,unFinishNum=0;
        while(cursor.moveToNext()){
            int status=cursor.getInt(cursor.getColumnIndex("status"));
            recordNum+=1;
            if(status==ListItem.NO_RECORD){
                long id=cursor.getInt(cursor.getColumnIndex("l_id"));
                updateItem(id,ListItem.UNFINISH);
                unFinishNum+=1;
            }else if(status==ListItem.UNFINISH){
                unFinishNum+=1;
            }else if(status==ListItem.FINISH){
                finishNum+=1;
            }
        }
        cursor.close();
        if(recordNum==0)
            return null;
        return DataUtil.getDayStatus(time,recordNum,finishNum,unFinishNum);
    }
    public static List<ListItem> queryAllItems(String time){
        List<ListItem> list=new ArrayList<>();
        ListItem listItem;
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("ListItem",null,"time=?",new String[]{time},null,null,"l_id asc");
        while(cursor.moveToNext()){
            long id=cursor.getLong(cursor.getColumnIndex("l_id"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            int status=cursor.getInt(cursor.getColumnIndex("status"));
            listItem=new ListItem(content,status,time);
            listItem.setId(id);
            list.add(listItem);
        }
        cursor.close();
        return list;
    }
    public static List<ListItem> queryAllItemsExceptNoContent(String time){
        List<ListItem> list=new ArrayList<>();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("ListItem",null,"time=? and status!=?",new String[]{time,"101"},null,null,"l_id asc");
        ListItem listItem;
        while(cursor.moveToNext()){
            long id=cursor.getLong(cursor.getColumnIndex("l_id"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            int status=cursor.getInt(cursor.getColumnIndex("status"));
            listItem=new ListItem(content,status,time);
            listItem.setId(id);
            list.add(listItem);
        }
        cursor.close();
        return  list;
    }
    public static void updateItem(long id,int status,String content){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=DataUtil.generateCV(content,status);
        db.update("ListItem",values,"l_id=?",new String[]{id+""});
    }
    public static void updateItem(long id,int status){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=DataUtil.generateCV(status);
        db.update("ListItem",values,"l_id=?",new String[]{id+""});
    }
    public static void deleteItem(long id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("ListItem","l_id=?",new String[]{id+""});
    }
}
