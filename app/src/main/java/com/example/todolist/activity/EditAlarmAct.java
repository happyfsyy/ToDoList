package com.example.todolist.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.adapter.WheelAdapter;
import com.example.todolist.utils.SoftKeyboardUtil;
import com.example.todolist.utils.ToastUtil;
import com.example.todolist.view.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class EditAlarmAct extends BaseActivity{
    private int requestCode;
    private int selectedHourIndex;
    private int selectedMinuteIndex;
    private String note;
    private Toolbar toolbar;
    private ImageView confirm;
    private WheelView hourWheel;
    private List<String> hourText=new ArrayList<>();
    private WheelAdapter hourAdapter;
    private WheelView minuteWheel;
    private List<String> minuteText=new ArrayList<>();
    private WheelAdapter minuteAdapter;
    private View noteLayout;
    private TextView noteTextView;
    private TextView delAlarm;
    private AlertDialog dialog;
    private EditText dialogEditText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        initParams();
        initViews();
    }
    private void initParams(){
        requestCode=this.getIntent().getIntExtra("requestCode",0);
        if(requestCode==AlarmFragment.ADD_ALARM){
            Calendar calendar=Calendar.getInstance();
            selectedHourIndex=calendar.get(Calendar.HOUR_OF_DAY);
            selectedMinuteIndex=calendar.get(Calendar.MINUTE);
        }else{
            selectedHourIndex=this.getIntent().getIntExtra("hour",0);
            selectedMinuteIndex=this.getIntent().getIntExtra("minute",0);
            note=this.getIntent().getStringExtra("note");
        }
    }
    private void initViews(){
        toolbar=findViewById(R.id.alarm_toolbar);
        confirm=findViewById(R.id.alarm_confirm);
        hourWheel=findViewById(R.id.hour_wheel);
        minuteWheel=findViewById(R.id.minute_wheel);
        noteLayout=findViewById(R.id.note_layout);
        noteTextView=findViewById(R.id.note_textview);
        delAlarm=findViewById(R.id.delete_alarm);
        initToolbar();
        initNote();
        initDel();
        initWheels();
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
        if(requestCode==AlarmFragment.ADD_ALARM){
            toolbar.setTitle(getString(R.string.create_alarm));
        }else if(requestCode==AlarmFragment.EDIT_ALARM){
            toolbar.setTitle(getString(R.string.edit_alarm));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data=new Intent();
                data.putExtra("type",AlarmFragment.EDIT_ALARM);
                data.putExtra("hour",hourWheel.getSelectedIndex());
                data.putExtra("minute",minuteWheel.getSelectedIndex());
                data.putExtra("note",noteTextView.getText().toString());
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }
    private void initDel(){
        if(requestCode==AlarmFragment.ADD_ALARM){
            delAlarm.setVisibility(View.GONE);
        }else{
            delAlarm.setVisibility(View.VISIBLE);
            delAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data=new Intent();
                    data.putExtra("type",AlarmFragment.DEL_ALARM);
                    setResult(RESULT_OK,data);
                    finish();
                }
            });
        }
    }
    private void initNote(){
        if(requestCode== AlarmFragment.ADD_ALARM){
            noteTextView.setText(getString(R.string.alarm));
        }else{
            noteTextView.setText(note);
        }
        noteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog==null){
                    dialogEditText=(EditText)LayoutInflater.from(EditAlarmAct.this).inflate(R.layout.dialog_view,null);
                    dialogEditText.setText(noteTextView.getText());
                    dialog=new AlertDialog.Builder(EditAlarmAct.this)
                            .setTitle(getResources().getString(R.string.alarm_name))
                            .setView(dialogEditText)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    noteTextView.setText(dialogEditText.getText());
                                    dialog.dismiss();                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    dialog.setCanceledOnTouchOutside(false);
                    dialogEditText.setSelection(noteTextView.getText().length());
                    dialogEditText.requestFocus();
                }else{
                    dialog.show();
                }

            }
        });
    }
    private void initWheels(){
        for(int i=0;i<24;i++){
            hourText.add(String.format(getString(R.string.hour_or_minute),i));
        }
        hourAdapter=new WheelAdapter(hourText);
        hourWheel.setAdapter(hourAdapter);
        hourWheel.setCurrentItem(selectedHourIndex);

        for(int i=0;i<60;i++){
            minuteText.add(String.format(getString(R.string.hour_or_minute),i));
        }
        minuteAdapter=new WheelAdapter(minuteText);
        minuteWheel.setAdapter(minuteAdapter);
        minuteWheel.setCurrentItem(selectedMinuteIndex);

    }
}
