package com.example.todolist.bean;

public class AlarmItem {
    private long id;
    private String time;
    private String note;
    private boolean isOpen;
    private int type;
    public AlarmItem(int type){
        this.type=type;
    }
    public AlarmItem(String time,String note,boolean isOpen,long id){
        this(time,note,isOpen);
        this.id=id;
    }
    public AlarmItem(String time,String note,boolean isOpen){
        this.time=time;
        this.note=note;
        this.isOpen=isOpen;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public boolean isOpen() {
        return isOpen;
    }
    public void setOpen(boolean open) {
        isOpen = open;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type=type;
    }
}
