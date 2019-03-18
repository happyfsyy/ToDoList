package com.example.todolist.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import com.example.todolist.R;
import com.example.todolist.activity.MainActivity;
import com.example.todolist.activity.MyApplication;
import com.example.todolist.activity.RingAct;
import com.example.todolist.bean.DayStatus;
import com.example.todolist.db.DayStatusDao;
import com.example.todolist.db.ListItemDao;
import com.example.todolist.utils.DateUtil;
import com.example.todolist.utils.LogUtil;
import com.example.todolist.utils.ToastUtil;
import com.google.android.material.snackbar.Snackbar;

import java.security.acl.Acl;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private static final int REFRESH_CODE=10000;
    private NotificationManager manager;
    public static final int NOTIFICATION_ID=666;
    public static final String SERVICE_CHANNEL_ID="serviceChannelId";
    public static final String SERVICE_CHANNEL_NAME="serviceChannelName";
    public MyService() {
        super();
    }
    @Override
    public void onCreate() {
        LogUtil.e("onCreate()");
        super.onCreate();
    }
    private Notification getNotification(){
        Notification.Builder builder=new Notification.Builder(MyApplication.getContext())
                .setSmallIcon(R.drawable.add)
                .setContentText("ddd")
                .setContentTitle("zzzz")
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(MyApplication.getContext(),111,
                        new Intent(MyApplication.getContext(), MainActivity.class),0));
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            builder.setChannelId(SERVICE_CHANNEL_ID);
        }
        return builder.build();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(SERVICE_CHANNEL_ID,SERVICE_CHANNEL_NAME,NotificationManager.IMPORTANCE_MIN);
            manager.createNotificationChannel(channel);
        }
        startForeground(NOTIFICATION_ID,getNotification());
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.e("service开启了");
                Calendar tempCalendar=Calendar.getInstance();
                tempCalendar.add(Calendar.DAY_OF_MONTH,-1);
                String time= DateUtil.getYearMonthDayNumberic(tempCalendar.getTime());
                LogUtil.e("日期是"+time);
                int status=DayStatusDao.queryStatus(time);
                LogUtil.e("status="+status);
                if(status==-1){
                    DayStatus dayStatus=ListItemDao.updateNoRecord(time);
                    if(dayStatus!=null){
                        DayStatusDao.insertDayStatus(dayStatus);
                    }
                }
                stopForeground(true);
            }
        }).start();
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        long triggerTime=calendar.getTimeInMillis();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        Intent tempIntent=new Intent(this,RefreshReceiver.class);
        tempIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,REFRESH_CODE,tempIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setExact(AlarmManager.RTC_WAKEUP,triggerTime,pendingIntent);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        LogUtil.e("我是服务，我被杀死了");
        Intent intent=new Intent(this,RefreshReceiver.class);
        sendBroadcast(intent);
        super.onDestroy();
    }
}
