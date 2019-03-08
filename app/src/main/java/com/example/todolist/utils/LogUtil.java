package com.example.todolist.utils;

import android.util.Log;

public class LogUtil {
    private static final boolean isDebug=true;
    private static final String TAG = "LogUtil";
    public static void e(String msg){
        if(isDebug){
            Log.e(TAG,msg);
        }
    }
    public static void e(String tag,String msg){
        if(isDebug){
            Log.e(tag,msg);
        }
    }
}
