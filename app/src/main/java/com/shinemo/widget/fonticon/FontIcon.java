package com.shinemo.widget.fonticon;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.shinemo.imdemo.R;
import com.shinemo.openim.helper.ApplicationContext;


/**
 * 用iconfont图标代替常规的ImageView
 * 即在TextView的基础上，所有text都用 strings_icon_font.xml 指定的字符串资源
 */
public class FontIcon extends TextView {
    private static Typeface typeface;

    static Typeface getFace(){
        if(typeface == null || typeface == Typeface.DEFAULT){
            typeface = loadTypeface();
        }
        return typeface;
    }
    private boolean needAddBold = false;

    private static Typeface loadTypeface() {
        try {
            return Typeface.createFromAsset(ApplicationContext.getInstance().getAssets(), "font/iconfont.ttf");
        } catch (Throwable e) {

        }
        return Typeface.DEFAULT;
    }

    public FontIcon(Context context) {
        this(context, null);
    }

    public FontIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        getAttrs(context, attrs);
    }

    public FontIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(getFace());
        this.setIncludeFontPadding(true);
        getAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FontIcon(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setTypeface(getFace());
        this.setIncludeFontPadding(true);
        getAttrs(context, attrs);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FontIcon);
        needAddBold = ta.getBoolean(R.styleable.FontIcon_bold, false);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (needAddBold) {
            TextPaint tp = getPaint();
            tp.setFakeBoldText(true);
        }
        super.onDraw(canvas);
    }
}
