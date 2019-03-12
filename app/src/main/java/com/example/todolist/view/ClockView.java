package com.example.todolist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.todolist.utils.DisplayUtil;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

public class ClockView extends View {
    private float width;
    private float height;
    private Paint mPaint;
    private float mStrokeWidth;
    private int shortLineColor;
    private int longLineColor;
    private float textHeight;
    private float descent;
    private Calendar calendar;
    private float circleX;
    private float circleY;
    private float hourPointerWidth;
    private float minutePointerWidth;
    private float secondPointerWidth;
    private float pointerEndLength;
    public ClockView(Context context) {
        this(context,null);
    }
    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        width= DisplayUtil.getScreenWidth()*0.7f;
        height=DisplayUtil.getScreenWidth()*0.7f;

        mStrokeWidth=5;
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(DisplayUtil.sp2px(16));
        Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
        textHeight=fontMetrics.bottom-fontMetrics.top;
        descent=fontMetrics.descent;

        hourPointerWidth=DisplayUtil.dp2px(5);
        minutePointerWidth=DisplayUtil.dp2px(3);
        secondPointerWidth=DisplayUtil.dp2px(2);
        pointerEndLength=DisplayUtil.dp2px(10);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int)width,(int)height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        circleX=width/2;
        circleY=height/2;
        float radius=circleX-mStrokeWidth/2;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(circleX,circleY,radius,mPaint);
        for(int i=0;i<60;i++){
            if(i%5==0){
                //todo setLineColor,setStrokeWidth
                mPaint.setStrokeWidth(5);
                canvas.drawLine(circleX,0,circleX,60,mPaint);
                String text=String.valueOf(i/5);
                if(text.equals("0"))
                    text="12";
                float x=(width-mPaint.measureText(text))/2;
                canvas.drawText(text,x,60+textHeight-descent,mPaint);
            }else{
                mPaint.setStrokeWidth(3);
                canvas.drawLine(circleX,0,circleX,30,mPaint);
            }
            canvas.rotate(6,circleX,circleY);
        }
        canvas.save();
        canvas.translate(circleX,circleY);
        drawPointer(canvas);
        canvas.restore();
        postInvalidateDelayed(1000);
    }
    private void drawPointer(Canvas canvas){
        calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        int second=calendar.get(Calendar.SECOND);
        float secondAngle=6*second;
        float minuteAngle=6*minute+6*secondAngle/360;
        float hourAngle=30*hour+30*minute/60.0f;

        canvas.save();
        canvas.rotate(hourAngle);
        RectF rectF=new RectF(-hourPointerWidth/2,-circleX*3/5,hourPointerWidth/2,pointerEndLength);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF,DisplayUtil.dp2px(10),DisplayUtil.dp2px(10),mPaint);
        canvas.restore();
        canvas.save();
        canvas.rotate(minuteAngle);
        RectF minuteRectF=new RectF(-minutePointerWidth/2,-circleX*3.5f/5,minutePointerWidth/2,pointerEndLength);
        canvas.drawRoundRect(minuteRectF,DisplayUtil.dp2px(10),DisplayUtil.dp2px(10),mPaint);
        canvas.restore();
        canvas.save();
        canvas.rotate(secondAngle);
        RectF secondRectF=new RectF(-secondPointerWidth/2,-circleX+20,secondPointerWidth/2,pointerEndLength);
        canvas.drawRoundRect(secondRectF,DisplayUtil.dp2px(10),DisplayUtil.dp2px(10),mPaint);
        canvas.restore();
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(0,0,hourPointerWidth,mPaint);
    }
}
