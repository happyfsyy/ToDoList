package com.example.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.activity.MyApplication;
import com.example.todolist.bean.ListItem;
import com.example.todolist.utils.DataUtil;
import com.example.todolist.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListItemDao {
    private static MyOpenHelper dbHelper=new MyOpenHelper(MyApplication.getContext(),"list.db",null,1);
    public static long insertListItem(ListItem listItem){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        long id=database.insert("ListItem",null, DataUtil.getListItemCV(listItem));
        return id;
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
    public static void updateItem(long id, ContentValues values){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.update("ListItem",values,"l_id=?",new String[]{id+""});
    }
    public static void deleteItem(long id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("ListItem","l_id=?",new String[]{id+""});
    }

}