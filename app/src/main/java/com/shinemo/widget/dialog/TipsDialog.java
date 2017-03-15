package com.shinemo.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.shinemo.imdemo.R;


/**
 * 提示dialog
 * Created by Yetwish on 2015/8/20.
 */
public class TipsDialog extends Dialog {

    private TextView tvTipsLine1;
    private TextView tvTipsLine2;
    private TextView tvTipsNum;

    /**
     * 当前对话框所用样式
     */
    private int mStyle;

    /**
     * 三种样式
     */
    public static final int STYLE_LINE1 = 0x01;
    public static final int STYLE_LINE2 = 0x02;
    public static final int STYLE_NUM = 0x03;

    public TipsDialog(Context context, int style) {
        super(context, R.style.tips_dialog);
        confirmValueValid(style);
        initView();
        setStyle(style);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        setContentView(R.layout.dialog_tips);
        tvTipsLine1 = (TextView) findViewById(R.id.tv_line1_tips);
        tvTipsLine2 = (TextView) findViewById(R.id.tv_line2_tips);
        tvTipsNum = (TextView) findViewById(R.id.tv_num_tips);
    }

    public int getStyle(){
        return mStyle;
    }

    /**
     * 设置样式
     *
     * @param style 样式 {@link #STYLE_LINE1} {@link #STYLE_LINE2} {@link #STYLE_NUM} 三种样式之一
     */
    public synchronized void setStyle(int style) {
        confirmValueValid(style);
        if (mStyle == style) return;
        mStyle = style;
        initVisibility();
        if (mStyle == STYLE_LINE1)
            tvTipsLine1.setVisibility(View.VISIBLE);
        if (mStyle == STYLE_LINE2)
            tvTipsLine2.setVisibility(View.VISIBLE);
        if (mStyle == STYLE_NUM)
            tvTipsNum.setVisibility(View.VISIBLE);
    }

    /**
     * 将所有view的visibility设为gone
     */
    private void initVisibility() {
        tvTipsLine1.setVisibility(View.GONE);
        tvTipsLine2.setVisibility(View.GONE);
        tvTipsNum.setVisibility(View.GONE);
    }

    /**
     * 设置显示文本
     * @param text 要显示的文本
     * @return
     */
    public TipsDialog setContent(String text) {
        if (mStyle == STYLE_LINE1)
            tvTipsLine1.setText(text);
        if (mStyle == STYLE_LINE2)
            tvTipsLine2.setText(text);
        if (mStyle == STYLE_NUM)
            tvTipsNum.setText(text);
        return this;
    }

    /**
     * 保证输入的style值 在有效范围，如果超出有效范围 则抛出异常
     *
     * @param style
     */
    private void confirmValueValid(int style) {
        if (style > STYLE_NUM || style < STYLE_LINE1)
            throw new IllegalArgumentException("Invalid style");
    }


}
