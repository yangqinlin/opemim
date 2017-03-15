package com.shinemo.widget.audio.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.shinemo.imdemo.R;
import com.shinemo.utils.SizeUtils;

/**
 * Created by summer on 2016/7/20.
 */
public class RingProgressView extends View {

    private Paint paint;
    private RectF oval;
    private int ringColor;
    private float ringWidth;
    private int max = 100;
    private int progress = 80;

    public RingProgressView(Context context) {
        this(context, null);
    }

    public RingProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressView);
        ringColor = typedArray.getColor(R.styleable.RingProgressView_ringColor, context.getResources().getColor(R.color.roundColor));
        ringWidth = typedArray.getDimension(R.styleable.RingProgressView_ringWidth, SizeUtils.dp2px(context, 12));
        typedArray.recycle();

        paint = new Paint();
        oval = new RectF();
        paint.setColor(ringColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ringWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centre = getWidth() / 2;
        int radius = (int) (centre - ringWidth / 2);
        oval.set(centre - radius, centre - radius, centre + radius, centre + radius);

        float sweepAngle = 360 * ((float) progress / max);
        if (sweepAngle > 0) {
            canvas.drawArc(oval, -90, sweepAngle, false, paint);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        setProgress(getMax());
        super.setVisibility(visibility);
    }

    public synchronized int getMax() {
        return max;
    }

    public synchronized void setMax(int max) {
        this.max = max;
    }

    public synchronized int getProgress() {
        return progress;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
    }
}
