package com.example.todolist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.todolist.R;
import com.example.todolist.utils.DisplayUtil;

import androidx.annotation.Nullable;

public class GraphView extends View {
    private int leftRightPadding;
    private float width;
    private float height;
    private Paint circlePaint;
    private Paint arcPaint;
    private Paint iconCirclePaint;
    private Paint textPaint;

    private float goodPercent;
    private float ordinaryPercent;
    private float badPercent;
    private float noRecordPercent;

    private int outerBgColor;
    private int innerBgColor;
    private int goodColor;
    private int ordinaryColor;
    private int badColor;

    private float circleX;
    private float circleY;
    private float outerRadius;
    private RectF rectF;

    private String[] text=new String[4];
    private float maxTextWidth;
    private float maxTextHeight;
    private float baseLineTranslate;
    private float iconCircleRadius;
    private float iconCircleDiameter;
    private float textSpace;

    public GraphView(Context context) {
        this(context,null);
    }
    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initParams();
    }
    private void init(){
        outerBgColor=getResources().getColor(R.color.graph_gray);
        innerBgColor= getResources().getColor(R.color.color_white);
        goodColor=getResources().getColor(R.color.graph_red);
        ordinaryColor=getResources().getColor(R.color.graph_green);
        badColor=getResources().getColor(R.color.graph_blue);

        circlePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);

        arcPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.FILL);
        arcPaint.setColor(goodColor);

        iconCirclePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        iconCirclePaint.setStyle(Paint.Style.FILL);

        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(DisplayUtil.sp2px(14));

        goodPercent=0.48f;
        ordinaryPercent=0.12f;
        badPercent=0.18f;
        noRecordPercent=0.22f;

        iconCircleRadius=DisplayUtil.dp2px(10);
        iconCircleDiameter=2*iconCircleRadius;
        textSpace=DisplayUtil.dp2px(5);
    }
    private void initParams(){
        width= DisplayUtil.getScreenWidth();
        leftRightPadding=DisplayUtil.dp2px(10);
        height=(int)(width*0.5f);

        rectF=new RectF(leftRightPadding,0,height+leftRightPadding,height);

        String maxText=getResources().getString(R.string.graph_max_length_text);
        maxTextWidth=textPaint.measureText(maxText,0,maxText.length())+iconCircleDiameter+textSpace;
        Paint.FontMetrics fontMetrics=textPaint.getFontMetrics();
        baseLineTranslate=fontMetrics.bottom;
        maxTextHeight=fontMetrics.bottom-fontMetrics.top;
        maxTextHeight=Math.max(iconCircleRadius,maxTextHeight);
    }
    private void initText(){
        text[0]=String.format(getResources().getString(R.string.graph_good),(int)(goodPercent*100+0.5f));
        text[1]=String.format(getResources().getString(R.string.graph_ordinary),(int)(ordinaryPercent*100+0.5f));
        text[2]=String.format(getResources().getString(R.string.graph_bad),(int)(badPercent*100+0.5f));
        text[3]=String.format(getResources().getString(R.string.graph_no_record),(int)(noRecordPercent*100+0.5f));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int)width,(int)height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawOuterCircle(canvas);
        drawArc(canvas);
        drawInnerCircle(canvas);
        drawText(canvas);
    }
    private void drawOuterCircle(Canvas canvas){
        circleX=leftRightPadding+height/2;
        circleY=height/2;
        outerRadius=height/2;
        circlePaint.setColor(outerBgColor);
        canvas.drawCircle(circleX,circleY,outerRadius,circlePaint);
    }
    private void drawArc(Canvas canvas){
        //draw arc
        float goodAngle=360*goodPercent;
        float ordinaryAngle=360*ordinaryPercent;
        float badAngle=360*badPercent;

        arcPaint.setColor(goodColor);
        canvas.drawArc(rectF,-90,goodAngle,true,arcPaint);
        arcPaint.setColor(ordinaryColor);
        canvas.drawArc(rectF,goodAngle-90,ordinaryAngle,true,arcPaint);
        arcPaint.setColor(badColor);
        canvas.drawArc(rectF,goodAngle+ordinaryAngle-90,badAngle,true,arcPaint);
//        arcPaint.setColor(getResources().getColor(R.color.graph_gray));
//        canvas.drawArc(rectF,goodAngle+ordinaryAngle+badAngle-90+30,320-goodAngle-ordinaryAngle-badAngle,true,arcPaint);
    }
    private void drawInnerCircle(Canvas canvas){
        //draw innercircle
        float innerRaidus=outerRadius/3;
        circlePaint.setColor(innerBgColor);
        canvas.drawCircle(circleX,circleY,innerRaidus,circlePaint);
    }
    private void drawText(Canvas canvas){
        //todo 如果maxTextWidth+outerRaidus>width，remeasureWidth，重新设置textSize
        float leftX=width-maxTextWidth-leftRightPadding;
        float topY=(height-4*maxTextHeight-3*textSpace)/2;
        float iconCircleX=leftX+iconCircleRadius;
        float iconCircleY=topY+maxTextHeight/2;
        for(int i=0;i<4;i++){
            if(i==0){
                iconCirclePaint.setColor(goodColor);
            }else if(i==1){
                iconCirclePaint.setColor(ordinaryColor);
            }else if(i==2){
                iconCirclePaint.setColor(badColor);
            }else{
                iconCirclePaint.setColor(outerBgColor);
            }
            canvas.drawCircle(iconCircleX,iconCircleY+i*maxTextHeight+i*textSpace,iconCircleRadius,iconCirclePaint);
            canvas.drawText(text[i],leftX+iconCircleDiameter+textSpace,
                    iconCircleY+baseLineTranslate+maxTextHeight*i+i*textSpace,textPaint);
        }
    }
    public void setPercent(float goodPercent,float ordinaryPercent,float badPercent,float noRecordPercent){
        this.goodPercent=goodPercent;
        this.ordinaryPercent=ordinaryPercent;
        this.badPercent=badPercent;
        this.noRecordPercent=noRecordPercent;
        initText();
        this.postInvalidate();
    }

}
