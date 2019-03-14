package com.example.todolist.adapter;

import java.util.List;

public class WheelAdapter {
    private List<String> dataList;
    public WheelAdapter(List<String> dataList){
        this.dataList=dataList;
    }
    public int getItemCount(){
        return dataList.size();
    }
    public String getItem(int index){
        return dataList.get(index);
    }
    public int indexOf(String item){
        return dataList.indexOf(item);
    }
}
