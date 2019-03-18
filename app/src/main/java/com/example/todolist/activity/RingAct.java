package com.example.todolist.activity;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.todolist.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RingAct extends AppCompatActivity {
    private Button stop;
    private MediaPlayer mediaPlayer;
    private TextView note;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        initMediaPlayer();
        initViews();
    }
    private void initMediaPlayer(){
        mediaPlayer=MediaPlayer.create(this,R.raw.alarm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
    private void initViews(){
        note=findViewById(R.id.ring_note);
        stop=findViewById(R.id.stop_alarm);
        note.setText(getIntent().getStringExtra("note"));
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });
    }
}
