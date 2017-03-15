package com.shinemo.widget.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.shinemo.imdemo.R;


/**
 * Created by zale on 16/1/22.
 */
public class CommonRound extends View {

    private Paint mPaint;
    private int roundCenter;

    private int roundColor;
    private float radius;
    private int style;


    public CommonRound(Context context) {
        this(context, null);
    }

    public CommonRound(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonRound);
        roundColor = mTypedArray.getColor(R.styleable.CommonRound_commonRoundColor, Color.RED);
        style = mTypedArray.getInt(R.styleable.CommonRound_commonRoundType, 0);

        mTypedArray.recycle();
    }

    private void initial() {
        roundCenter = getWidth() / 2;
        radius = roundCenter;
    }

    private boolean isGradient = false;
    private int startColor = -1, endColor = -1;

    public void setGradient(boolean gradient) {
        isGradient = gradient;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initial();
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setColor(roundColor);
        if (style == 0) {
            mPaint.setStyle(Paint.Style.FILL);
        } else if (style == 1) {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        if (isGradient) {
            LinearGradient linearGradient = new LinearGradient(roundCenter - (radius / 2), roundCenter + (radius / 2),
                    roundCenter + (radius / 2), roundCenter - (radius / 2), startColor, endColor, Shader.TileMode.MIRROR);
            mPaint.setShader(linearGradient);
        }
        canvas.drawCircle(roundCenter, roundCenter, radius, mPaint);
    }
}
