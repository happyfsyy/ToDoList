package com.example.todolist.bean;

public class ListItem {
    public enum ItemStatus{
        NO_CONTENT,NO_RECORD,FINISH,UNFINISH
    }
    private String content;
    private ItemStatus status;
    public ListItem(String content,ItemStatus status){
        this.content=content;
        this.status=status;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public ItemStatus getStatus() {
        return status;
    }
    public void setStatus(ItemStatus status) {
        this.status = status;
    }
}
