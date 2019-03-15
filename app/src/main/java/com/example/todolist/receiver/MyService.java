package com.example.todolist.receiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.todolist.activity.RingAct;
import com.example.todolist.utils.LogUtil;

import androidx.annotation.Nullable;

public class MyService extends Service {
    public MyService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("我是服务，我接受到了！！！！！！");
        Intent intent1=new Intent(this, RingAct.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
        return super.onStartCommand(intent, flags, startId);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
