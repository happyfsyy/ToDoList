package com.example.todolist.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class CalendarPagerAdapter extends PagerAdapter {
    private View[] views;
    public CalendarPagerAdapter(View[] views){
        this.views=views;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views[position]);
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(views[position]);
        return views[position];
    }
    @Override
    public int getCount() {
        return views.length;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
