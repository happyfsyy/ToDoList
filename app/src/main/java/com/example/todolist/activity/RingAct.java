package com.example.todolist.activity;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.todolist.R;

import androidx.annotation.Nullable;

public class RingAct extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        RingtoneManager manager=new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_ALARM);
        manager.getCursor();
        Uri ringUri=manager.getRingtoneUri(1);

        MediaPlayer mediaPlayer=new MediaPlayer();
        mediaPlayer.reset();
        try{
            mediaPlayer.setDataSource(this,ringUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer.stop();
    }
}
