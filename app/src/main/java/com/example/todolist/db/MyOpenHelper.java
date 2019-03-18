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
            +"time date,status integer,ratio float,year integer,month integer,day integer," +
            "list_num integer,finish_num integer,unfinish_num integer)";
    private static final String CREATE_ALARM_ITEM="create table AlarmItem("
            +"a_id integer primary key autoincrement,"
            +"time char(5),note varchar,isOpen boolean)";
    public MyOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LIST_ITEM);
        db.execSQL(CREATE_DAY_STATUS);
        db.execSQL(CREATE_ALARM_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                db.execSQL(CREATE_ALARM_ITEM);
            case 2:
                db.execSQL("alter table DayStatus add ratio float");
                db.execSQL("alter table DayStatus add year integer");
                db.execSQL("alter table DayStatus add month integer");
                db.execSQL("alter table DayStatus add day integer");
            case 3:
                db.execSQL("alter table DayStatus add list_num integer");
                db.execSQL("alter table DayStatus add finish_num integer");
                db.execSQL("alter table DayStatus add unfinish_num integer");

        }
    }
}
