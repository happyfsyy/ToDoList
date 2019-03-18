package com.example.todolist.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.todolist.activity.RingAct;
import com.example.todolist.utils.LogUtil;
import com.example.todolist.utils.ToastUtil;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.e("收到广播了！！！！！！");
        Intent intent1=new Intent(context,RingAct.class);
        intent1.putExtra("note",intent.getStringExtra("note"));
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
