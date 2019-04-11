package com.example.todolist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.todolist.R;
import com.example.todolist.bean.DayStatus;
import com.example.todolist.db.DayStatusDao;
import com.example.todolist.listener.OnItemSelectedListener;
import com.example.todolist.utils.DateUtil;
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
    private int weekTextColor= getResources().getColor(R.color.week_text_color);
    private Paint dayPaint;//1、2
    private int dayTextColor=getResources().getColor(R.color.day_text_color);
    private Paint dayStatusBgPaint;//背后的阴影效果，正方形
    private Paint dayStatusPointPaint;
    private int goodColor=getResources().getColor(R.color.graph_red);
    private int ordinaryColor=getResources().getColor(R.color.graph_green);
    private int badColor=getResources().getColor(R.color.graph_blue);
    private Paint selectedBgPaint;//圆圈
    private int selectedBgColor=getResources().getColor(R.color.selected_circle_color);
    private int todayColor=getResources().getColor(R.color.today_color);
    private Calendar calendar;//这就是个变量
    private Date todayDate;//不变的，就是今天
    private Date curMonthDate;
    private Date selectedDate;
    private String[] weekText=new String[]{"日","一","二","三","四","五","六"};
    private int[] dayText=new int[42];
    private int curMonthStartIndex;
    private int curMonthEndIndex;
    private int dayNum;//这个月有多少天
    private float dayTextHeight;
    private OnItemSelectedListener onItemSelectedListener;
    private int[] status=new int[42];//对应每天的list完成状况

    public CalendarView(Context context) {
        this(context,null);
    }
    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        calendar=Calendar.getInstance();
        todayDate=new Date();
        selectedDate=new Date();
        curMonthDate=new Date();

        leftPadding=DisplayUtil.dp2px(10);
        rightPadding=leftPadding;
        width=DisplayUtil.getScreenWidth();
        cellWidth=(width-2*leftPadding)/7f;
        cellHeight=cellWidth*0.8f;

        calculateStartEndIndex();
//        if(curMonthEndIndex<28){
//            height=(int)(cellHeight*5);
//        }else if(curMonthEndIndex<35){
//            height=(int)(cellHeight*6);
//        }else{
//            height=(int)(cellHeight*7);
//        }
        height=(int)(cellHeight*7);


        weekPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        weekPaint.setTextSize(DisplayUtil.sp2px(16));
        weekPaint.setColor(weekTextColor);

        dayPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        dayPaint.setStyle(Paint.Style.FILL);
        dayPaint.setTextSize(DisplayUtil.sp2px(16));
        dayPaint.setColor(dayTextColor);

        dayStatusBgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        dayStatusBgPaint.setStyle(Paint.Style.FILL);

        dayStatusPointPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        dayStatusPointPaint.setColor(getResources().getColor(R.color.graph_gray));

        selectedBgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedBgPaint.setColor(selectedBgColor);

        Rect rect=new Rect();
        dayPaint.getTextBounds("日",0,1,rect);
        dayTextHeight=rect.height();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        //画日、一、二、三、四、五、六那一行
        float weekTextX,weekTextY;
        weekTextY=cellHeight/2+dayTextHeight/2;
        for(int i=0;i<weekText.length;i++){
            weekTextX=leftPadding+i*cellWidth+(cellWidth-measureWidth(weekText[i],weekPaint))/2f;
            canvas.drawText(weekText[i],weekTextX,weekTextY,weekPaint);
        }

        //计算42个数据
        calculateDate();

        //画selected背景
        drawSelectedBg(canvas);

        //画当天状况的背景
//        drawStatusBg(canvas);
        drawPointBg(canvas);


        //画1到31号
        String curYearAndMonth=getYearAndMonth(curMonthDate);
        String todayYearAndMonth=getYearAndMonth(todayDate);
        int todayIndex=-1;
        if(curYearAndMonth.equals(todayYearAndMonth)){
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            todayIndex=curMonthStartIndex+day-1;
        }
        int row,col;
        String text;
        for(int i=0;i<42;i++){
            if(dayText[i]!=-1){
                text=dayText[i]+"";
                col=i%7;
                row=i/7+1;
                weekTextX=leftPadding+col*cellWidth+(cellWidth-measureWidth(text,dayPaint))/2f;
                weekTextY=row*cellHeight+cellHeight/2+dayTextHeight/2;
                if(todayIndex!=-1&&todayIndex==i){
                    dayPaint.setTypeface(Typeface.DEFAULT_BOLD);
                    dayPaint.setColor(todayColor);
                }else{
                    dayPaint.setTypeface(Typeface.DEFAULT);
                    dayPaint.setColor(dayTextColor);
                }
                canvas.drawText(text,weekTextX,weekTextY,dayPaint);
            }
        }
    }
    private float measureWidth(String text,Paint paint){
        return paint.measureText(text);
    }

    private void calculateStartEndIndex(){
        calendar.setTime(curMonthDate);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);//1号是星期几
        curMonthStartIndex=dayOfWeek-1;
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        dayNum=calendar.get(Calendar.DAY_OF_MONTH);
        curMonthEndIndex=curMonthStartIndex+dayNum-1;
    }
    private void calculateDate(){
        calculateStartEndIndex();
        for(int i=0;i<curMonthStartIndex;i++){
            dayText[i]=-1;
        }
        for(int i=1;i<=dayNum;i++){
            dayText[curMonthStartIndex+i-1]=i;
        }
        for(int i=curMonthEndIndex+1;i<42;i++){
            dayText[i]=-1;
        }
    }
    private void calculateStatus(){
        for(int i=0;i<curMonthStartIndex;i++){
            status[i]=-1;
        }
        for(int i=curMonthEndIndex+1;i<42;i++){
            status[i]=-1;
        }
        Date tempDate;
        calendar.setTime(curMonthDate);
        for(int i=curMonthStartIndex;i<=curMonthEndIndex;i++){
            calendar.set(Calendar.DAY_OF_MONTH,dayText[i]);
            tempDate=calendar.getTime();
            String time=DateUtil.getYearMonthDayNumberic(tempDate);
            status[i]=DayStatusDao.queryStatus(time);
        }
    }
    private void drawStatusBg(Canvas canvas){
        calculateStatus();
        for(int i=curMonthStartIndex;i<=curMonthEndIndex;i++){
            if(status[i]==-1){
                continue;
            }
            int row=getRow(i);
            int col=getCol(i);
            float left=leftPadding+col*cellWidth;
            float top=row*cellHeight;
            if(status[i]== DayStatus.BAD){
                dayStatusBgPaint.setColor(badColor);
            }else if(status[i]==DayStatus.ORDINARY){
                dayStatusBgPaint.setColor(ordinaryColor);
            }else if(status[i]==DayStatus.GOOD){
                dayStatusBgPaint.setColor(goodColor);
            }
            canvas.drawRect(left,top,left+cellWidth,top+cellHeight,dayStatusBgPaint);
        }
    }
    private void drawPointBg(Canvas canvas){
        calculateStatus();
        for(int i=curMonthStartIndex;i<=curMonthEndIndex;i++){
            if(status[i]==-1){
                continue;
            }
            int row=getRow(i);
            int col=getCol(i);
            float cx=leftPadding+col*cellWidth+0.5f*cellWidth;
            float cy=(row+1)*cellHeight-DisplayUtil.dp2px(5);
            canvas.drawCircle(cx,cy,DisplayUtil.dp2px(3),dayStatusPointPaint);
        }
    }
    private String getYearAndMonth(Date date){
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        return String.format(getResources().getString(R.string.year_and_month),year,month);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                float x=event.getX();
                float y=event.getY();
                boolean isSetSelectedDate=setSelectedDate(x,y);
                if(isSetSelectedDate){
                    invalidate();
                    onItemSelectedListener.onItemSelected(selectedDate);
                }
                break;
        }
        return true;
    }
    private boolean setSelectedDate(float x,float y){
        if(x<=leftPadding||x>=leftPadding+7*cellWidth||y<=cellHeight){
            return false;
        }
        int row=(int)((y-cellHeight)/cellHeight);
        int col=(int)((x-leftPadding)/cellWidth);
        int selectedIndex=row*7+col;
        if(selectedIndex<curMonthStartIndex||selectedIndex>curMonthEndIndex){
            return false;
        }
        calendar.setTime(curMonthDate);
        calendar.set(Calendar.DAY_OF_MONTH,dayText[selectedIndex]);
        selectedDate=calendar.getTime();
        return true;
    }

    /**
     * 获取日期的行数，1号是在第一行
     * @param index
     * @return
     */
    private int getRow(int index){
        return index/7+1;
    }
    private int getCol(int index){
        return index%7;
    }
    private int getSelectedIndex(){
        calendar.setTime(selectedDate);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int selectedIndex=curMonthStartIndex+day-1;
        return selectedIndex>curMonthEndIndex?curMonthEndIndex:selectedIndex;
    }
    private void drawSelectedBg(Canvas canvas){
        int selectedIndex=getSelectedIndex();
        int row=getRow(selectedIndex);
        int col=getCol(selectedIndex);
        float circleX=leftPadding+col*cellWidth+0.5f*cellWidth;
        float circleY=row*cellHeight+0.5f*cellHeight;
        float radius=cellHeight*0.5f;
        canvas.drawCircle(circleX,circleY,radius,selectedBgPaint);
    }
    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener){
        this.onItemSelectedListener=onItemSelectedListener;
    }
    public void setSelectedDate(Date selectedDate){
        this.selectedDate=selectedDate;
        this.postInvalidate();
    }
    public void setCurMonthAndSelectedDate(Date curMonthDate,Date selectedDate,boolean isInvalidate){
        this.curMonthDate=curMonthDate;
        this.selectedDate=selectedDate;
        if(isInvalidate){
            this.postInvalidate();
        }
    }
    public Date getSelectedDate(){
        return selectedDate;
    }
}
