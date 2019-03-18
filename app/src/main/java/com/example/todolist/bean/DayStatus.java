package com.example.todolist.bean;

import java.util.Date;

public class DayStatus {
    public static final int BAD=111;
    public static final int ORDINARY=112;
    public static final int GOOD=113;
    private int id;
    private String time;
    private int status;
    private int listNum;
    private int finishNum;
    private int unFinishNum;
    private float ratio;
    private int year;
    /**这里的月份是实际的月份，例如现在2019/03/18，月份就是3 */
    private int month;
    private int day;
    public DayStatus(String time,int status,float ratio,int listNum,int finishNum,int unFinishNum,int year,int month,int day){
        this.time=time;
        this.status=status;
        this.ratio=ratio;
        this.listNum=listNum;
        this.finishNum=finishNum;
        this.unFinishNum=unFinishNum;
        this.year=year;
        this.month=month;
        this.day=day;
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
    public int getListNum() {
        return listNum;
    }
    public void setListNum(int listNum) {
        this.listNum = listNum;
    }
    public int getFinishNum() {
        return finishNum;
    }
    public void setFinishNum(int finishNum) {
        this.finishNum = finishNum;
    }
    public int getUnFinishNum() {
        return unFinishNum;
    }
    public void setUnFinishNum(int unFinishNum) {
        this.unFinishNum = unFinishNum;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
