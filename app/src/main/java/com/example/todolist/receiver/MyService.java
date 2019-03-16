package com.example.todolist.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import com.example.todolist.activity.RingAct;
import com.example.todolist.utils.LogUtil;

import androidx.annotation.Nullable;

public class MyService extends Service {
    public MyService() {
        super();
    }
    @Override
    public void onCreate() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(1,new Notification());
//        }
        super.onCreate();

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        LogUtil.e("我的Service开启了");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String type=intent.getStringExtra("type");
                LogUtil.e("type="+type);
                if(!type.equals("alarm"))
                    return;
                Intent intent1=new Intent(MyService.this,RingAct.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }
        }).start();

        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour=60*60*1000;
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,MyService.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,i,0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        LogUtil.e("我的Service被杀掉了，快救救我");
        super.onDestroy();
    }
}
