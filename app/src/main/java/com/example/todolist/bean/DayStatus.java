package com.example.todolist.bean;

import java.util.Date;

public class DayStatus {
    public static final int BAD=111;
    public static final int ORDINARY=112;
    public static final int GOOD=113;
    private String time;
    private int status;
    private float ratio;
    private int year;
    private int month;
    private int day;
    public DayStatus(String time,int status,float ratio,int year,int month,int day){
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
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public float getRatio() {
        return ratio;
    }
    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
}
