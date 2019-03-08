package com.example.todolist.bean;

import java.util.Date;

public class DayStatus {
    public static final int BAD=111;
    public static final int ORDINARY=112;
    public static final int GOOD=113;
    private String time;
    private int status;
    public DayStatus(String time,int status){
        this.time=time;
        this.status=status;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
