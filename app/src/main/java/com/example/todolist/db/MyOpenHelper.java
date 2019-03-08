package com.example.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String CREATE_LIST_ITEM="create table ListItem("
            +"l_id integer primary key autoincrement,"
            +"content varchar,status integer,time date)";
    private static final String CREATE_DAY_STATUS="create table DayStatus("
            +"f_id integer primary key autoincrement,"
            +"time date,status integer)";
    public MyOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LIST_ITEM);
        db.execSQL(CREATE_DAY_STATUS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
