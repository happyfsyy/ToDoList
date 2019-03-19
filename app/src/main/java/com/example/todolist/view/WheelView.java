package com.example.todolist.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.example.todolist.R;
import com.example.todolist.adapter.WheelAdapter;
import com.example.todolist.utils.DisplayUtil;
import com.example.todolist.utils.LogUtil;

import androidx.annotation.Nullable;

public class WheelView extends View {
    private Paint outerTextPaint;
    private Paint centerTextPaint;
    private Paint linePaint;
    private Paint.FontMetrics fontMetrics;
    private int textSize;
    private float textHeight;
    private float itemHeight;
    private int outerTextColor;
    private int centerTextColor;
    private int lineColor;
    private int lineWidth;
    private float lineSpacingMultiplier;
    private int measuredWidth;
    private int measuredHeight;
    private WheelAdapter adapter;
    private int itemCount;
    private int itemVisibleCount;
    private int initPosition;//代表居中的cell的pos
    private int curCenterCellPos;
    private String[] visibleItems;
    private float previousY;
    private float totalScrollY;
    private int itemOffset;
    private Scroller mScroller;
    private int selectedIndex;
    private float firstLineY;
    private float secondLineY;

    public WheelView(Context context) {
        this(context,null);
    }
    public WheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller=new Scroller(context);
        initPaints();
    }

    private void initPaints(){
        outerTextColor=getResources().getColor(R.color.week_text_color);
        centerTextColor=getResources().getColor(R.color.colorPrimary);
        lineColor=getResources().getColor(R.color.default_text_color);
        textSize= DisplayUtil.dp2px(16);
        lineWidth=3;
        lineSpacingMultiplier=2.0f;
        itemVisibleCount=9;

        outerTextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        outerTextPaint.setColor(outerTextColor);
        outerTextPaint.setTextSize(textSize);

        centerTextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        centerTextPaint.setColor(centerTextColor);
        centerTextPaint.setTextScaleX(1.1f);
        centerTextPaint.setTextSize(textSize);

        linePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
        initPosition=-1;
    }
    private void measureHeight(){
        fontMetrics=centerTextPaint.getFontMetrics();
        textHeight=fontMetrics.descent-fontMetrics.ascent;
        itemHeight=textHeight*lineSpacingMultiplier;
        measuredHeight=(int)((itemVisibleCount-2)*itemHeight);
    }

    private void initParams(){
        itemCount=adapter.getItemCount();
        if(initPosition==-1){
            initPosition=(itemCount+1)/2;
        }
        firstLineY=(itemVisibleCount-2)/2*itemHeight;
        secondLineY=firstLineY+itemHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measuredWidth=MeasureSpec.getSize(widthMeasureSpec);
        measureHeight();
        initParams();
        setMeasuredDimension(measuredWidth,measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw line
        canvas.drawLine(0,firstLineY,measuredWidth,firstLineY,linePaint);
        canvas.drawLine(0,secondLineY,measuredWidth,secondLineY,linePaint);

        //calculate pos
        int changeNum=(int)(totalScrollY/itemHeight);
        curCenterCellPos=initPosition+changeNum%itemCount;
        if(curCenterCellPos<0){
            curCenterCellPos=itemCount+curCenterCellPos;
        }else if(curCenterCellPos>itemCount-1){
            curCenterCellPos=curCenterCellPos-itemCount;
        }

        //calculate text
        if(visibleItems==null){
            visibleItems=new String[itemVisibleCount];
        }
        for(int i=0;i<itemVisibleCount;i++){
            int index=curCenterCellPos-(itemVisibleCount/2-i);
            int realIndex=getRealIndex(index);
            visibleItems[i]=adapter.getItem(realIndex);
        }

        //drawText
        itemOffset=(int)(totalScrollY%itemHeight);
        canvas.save();
        canvas.translate(0,-itemOffset);
        for(int i=0;i<itemVisibleCount;i++){
            float x=(measuredWidth-getTextWidth(visibleItems[i]))/2;
            float y=(i-1)*itemHeight+itemHeight/2-textHeight/2-fontMetrics.ascent;
            if(y+fontMetrics.ascent-itemOffset>=firstLineY&&y+fontMetrics.descent-itemOffset<=secondLineY){
                int index=curCenterCellPos-(itemVisibleCount/2-i);
                selectedIndex=getRealIndex(index);
                canvas.drawText(visibleItems[i],x,y,centerTextPaint);
            }else{
                canvas.drawText(visibleItems[i],x,y,outerTextPaint);
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                previousY=event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float curY=event.getRawY();
                float dy=previousY-curY;
                previousY=event.getRawY();
                totalScrollY+=dy;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                int mOffset=(int)((totalScrollY%itemHeight+itemHeight)%itemHeight);
                if((float)Math.abs(mOffset)>itemHeight/2.0f){
                    mOffset=(int)(itemHeight-mOffset);
                }else{
                    mOffset=-Math.abs(mOffset);
                }
                mScroller.startScroll(0,(int)totalScrollY,0,mOffset);
                invalidate();
                break;

        }
        return true;
    }

    private float getTextWidth(String text){
        return centerTextPaint.measureText(text);
    }

    private int getRealIndex(int index){
        if(index<0){
            return getRealIndex(index+itemCount);
        }else if(index>itemCount-1){
            return getRealIndex(index-itemCount);
        }
        return index;
    }

    public void setAdapter(WheelAdapter adapter){
        this.adapter=adapter;
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            totalScrollY=mScroller.getCurrY();
            LogUtil.e("totalScrollY="+totalScrollY+"\tfinalY="+mScroller.getFinalY());
            invalidate();
        }
    }

    public void setCurrentItem(int position){
        initPosition=position;
        selectedIndex=position;
    }
    public int getSelectedIndex(){
        return selectedIndex;
    }
}
