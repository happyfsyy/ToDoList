package com.example.todolist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.todolist.utils.DisplayUtil;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

public class CalendarView extends View {
    private int width;
    private int height;
    private float leftPadding;
    private float rightPadding;
    private float cellHeight;
    private float cellWidth;
    private Paint weekPaint;//日、一
    private int weekTextColor= Color.parseColor("#9a9a9a");
    private Paint dayPaint;//1、2
    private int dayTextColor=Color.parseColor("#616161");
    private Paint dayStatusBgPaint;//背后的阴影效果，正方形
    private int dayStatusBgColor=Color.parseColor("#f4f4f4");
    private Paint selectedBgPaint;//圆圈
    private int selectedBgColor=Color.parseColor("#99ccff");
    private int todayColor=Color.RED;
    private Calendar calendar;//这是个变量，我需要不断的用它
    private Date todayDate;//不变的，就是今天
    private Date curMonthDate;
    private Date selectedDate;
    private String[] weekText=new String[]{"日","一","二","三","四","五","六"};
    private Rect textRect=new Rect();
    private int[] dayText=new int[42];
    private int curMonthStartIndex;
    private int curMonthEndIndex;

    public CalendarView(Context context) {
        this(context,null);
    }
    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        width=DisplayUtil.getScreenWidth();
        height=DisplayUtil.getScreenHeight()/3;
        cellHeight=height/7f;
        cellWidth=cellHeight;
        //todo 如果cellWidth*7大于屏幕宽度，重新计算cellWidth，remeasure，while
        leftPadding=(width-height)/2f;
        rightPadding=leftPadding;

        weekPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        //todo 这里的textSize改为14sp试试看
        weekPaint.setTextSize(cellHeight/2);
        weekPaint.setColor(weekTextColor);

        dayPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        dayPaint.setTextSize(cellHeight/2);
        dayPaint.setColor(dayTextColor);

        dayStatusBgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        dayStatusBgPaint.setStyle(Paint.Style.FILL);
        dayStatusBgPaint.setColor(dayStatusBgColor);

        selectedBgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedBgPaint.setColor(selectedBgColor);

        calendar=Calendar.getInstance();
        todayDate=new Date();
        selectedDate=new Date();
        curMonthDate=new Date();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        float weekTextX,weekTextY;
        for(int i=0;i<weekText.length;i++){
            weekTextX=leftPadding+(cellWidth-measureWidth(weekText[i],weekPaint))/2f;
            weekTextY=(cellHeight+measureHeight(weekText[i],weekPaint))/2f;
            canvas.drawText(weekText[i],weekTextX,weekTextY,weekPaint);
        }
        //
    }
    private float measureWidth(String text,Paint paint){
        return paint.measureText(text);
    }
    private float measureHeight(String text,Paint paint){
        paint.getTextBounds(text,0,text.length(),textRect);
        return textRect.height();
    }
    private void calculateDate(){
        calendar.setTime(curMonthDate);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        curMonthStartIndex=calendar.get(Calendar.DAY_OF_WEEK);
        for(int i=0;i<curMonthStartIndex;i++){
            dayText[i]=-1;
        }
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        int dayNum=calendar.get(Calendar.DAY_OF_MONTH);
        curMonthEndIndex=curMonthStartIndex+dayNum-1;
        for(int i=1;i<=dayNum;i++){
            dayText[curMonthStartIndex+i-1]=i;
        }
        for(int i=curMonthEndIndex+1;i<42;i++){
            dayText[i]=-1;
        }

    }

}
