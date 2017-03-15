package com.shinemo.widget.audio;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.shinemo.imdemo.R;
import com.shinemo.utils.SizeUtils;


/**
 * 语音view
 * Created by Yetwish on 2015/9/19.
 */
public class VoiceWaveView extends View {

    public static final int MAX_LEVEL = 10;

    private int[] mVoice;
    private Paint mPaint;

    private int mLineColor;
    private int mPlayColor;
    private int mMaxHeight;
    private int mLineWidth;
    private int mPadding;
    private float progress = -1;
    private boolean isInProgress;
    private int mLineCount;

    public VoiceWaveView(Context context) {
        this(context, null);
    }

    public VoiceWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VoiceView);
        fetchAttr(ta);
        ta.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mLineColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineWidth);
    }

    private void fetchAttr(TypedArray ta) {
        mMaxHeight = ta.getDimensionPixelSize(R.styleable.VoiceView_maxHeight, 0);
        mLineColor = ta.getColor(R.styleable.VoiceView_lineColor, getResources().getColor(R.color.voice_normal));
        mPlayColor = ta.getColor(R.styleable.VoiceView_playColor, getResources().getColor(R.color.c_brand));
        mLineWidth = ta.getDimensionPixelSize(R.styleable.VoiceView_lineWidth, getResources().getDimensionPixelSize(R.dimen.voice_line_width));
        mPadding = mLineWidth;
    }

    public void setVoiceArray(int[] voice, int duration) {

        int baseLength = SizeUtils.dp2px(getContext(), 30);
        int width = baseLength + SizeUtils.dp2px(getContext(), (int) (2 * duration));
        int maxWidht = SizeUtils.dip2px(getContext(), 130);
        if(width > maxWidht){
            width = maxWidht;
        }

        int line = mPadding + mLineWidth;
        mLineCount = width/line;

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        setLayoutParams(layoutParams);

        if(voice == null || voice.length==0){
            int len = 10*duration;
            voice = new int[len];
            for(int i=0;i<len;i++){
                voice[i] = 1;
            }
        }
        mVoice = voice;
        isInProgress = false;

        invalidate();
    }

    public void setPlayColor(int color, int lineColor){
        mPlayColor = color;
        mLineColor = lineColor;
    }

    public void onProgress(int progress) {
        this.progress = progress;
        isInProgress = true;
        invalidate();
    }

    public void onComplete() {
        isInProgress = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mVoice == null || mVoice.length == 0) {
            return;
        }
        if (mMaxHeight == 0) mMaxHeight = getHeight();

        if (isInProgress) {
            int current = (int) (progress/100*mLineCount);
            play(canvas, 0, mLineCount, mLineColor);
            play(canvas, 0, current, mPlayColor);
        }else{
            play(canvas, 0, mLineCount, mPlayColor);
        }
    }

    private void play(Canvas canvas, int begin, int end, int color){
        mPaint.setColor(color);
        for(int i = begin; i<end;i++){
            int x = i * mLineWidth + mPadding * i;
            float pos = i;
            int position = (int) ((pos/mLineCount)*mVoice.length);
            int y = (getHeight() - mVoice[position] * mMaxHeight / MAX_LEVEL) / 2;
            canvas.drawLine(x, y, x, y + mVoice[position] * mMaxHeight / MAX_LEVEL, mPaint);
        }
    }
}
