package com.shinemo.widget.audio.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.shinemo.imdemo.R;


/**
 * Created by zale on 2015/7/24.
 */
public class RoundProgressBar extends View {

    private Paint paint;//画笔引用
    private int roundColor;//圆环的颜色
    private int roundProgressColor;//圆环进度的颜色
    private int textColor;//文字的颜色
    private float textSize;//文字的大小
    private float roundWidth;//圆环的宽度
    private  int max;//最大的进度
    private  int progress;//当前进度
    private int style;//进度的风格，实心或者空心

    public static final int STROKE = 0;
    public  static final int  FILL = 1;

    public RoundProgressBar(Context context) {
        super(context,null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, getResources().getColor(R.color.transparent));
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_centerTextSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 0);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_type, 0);
        mTypedArray.recycle();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画出最外层的大圆环
         */
        int center = getWidth()/2;//圆心坐标
        int radius = (int)(center - roundWidth/2)-2;//圆环半径
        paint.setColor(roundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roundWidth);
        paint.setAntiAlias(true);
        canvas.drawCircle(center, center, radius, paint);


        /**
         * 画出百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        /**
         * 画圆弧 ，画圆环的进度
         */
        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(center - radius-1, center - radius-1, center
                        + radius+1, center + radius+1);  //用于定义的圆弧的形状和大小的界限
        switch (style) {
            case STROKE:{
                paint.setStyle(Paint.Style.STROKE);
                if (max ==0){
                    canvas.drawArc(oval, -90, 0, false, paint);  //根据进度画圆弧
                }else {
                    canvas.drawArc(oval, -90, 360 * (max - progress) / max, false, paint);  //根据进度画圆弧
                }

                break;
            }
            case FILL:{
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if(max ==0){
                    canvas.drawArc(oval, -90, 0, true, paint);  //根据进度画圆弧
                }else {
                    canvas.drawArc(oval, -90, 360 * (max - progress) / max, true, paint);  //根据进度画圆弧
                }

                break;
            }
        }

    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     * @param max
     */
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress >= max){
            setVisibility(View.GONE);
            return;
        }
        if(progress == 0){
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getRoundProgressColor() {
        return roundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
}
