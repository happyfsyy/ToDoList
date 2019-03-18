package com.example.todolist.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.todolist.utils.LogUtil;
import com.example.todolist.utils.ToastUtil;

public class RefreshReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            context.startForegroundService(new Intent(context,MyService.class));
        }else{
            context.startService(new Intent(context,MyService.class));
        }
    }
}
