package com.example.todolist.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.todolist.activity.MyApplication;

public class SoftKeyboardUtil {
    public static void hideKeyboard(Context context){
        InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((Activity)context).getWindow().getDecorView().getWindowToken(),0);
    }
    public static void showKeyboard(Context context,EditText editText){
        InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
