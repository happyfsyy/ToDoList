package com.example.todolist.utils;

import android.widget.Toast;

import com.example.todolist.activity.MyApplication;

public class ToastUtil {
    private static Toast mToast;
    public static void showToast(String msg){
        if(mToast==null){
            mToast=Toast.makeText(MyApplication.getContext(),msg,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(msg);
        }
        mToast.show();
    }
}
