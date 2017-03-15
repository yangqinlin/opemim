package com.shinemo.imdemo.chat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shinemo.imdemo.R;


/**
 * 表情标签项
 * Created by Yetwish on 2015/9/5.
 */
public class SmileItem extends LinearLayout {

    private Context mContext;

    private ImageView ivIcon;

    public SmileItem(Context context) {
        super(context);
        this.mContext = context;
        initViews();

    }

    public SmileItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews();
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.SmileItem);
        ivIcon.setImageResource(ta.getResourceId(R.styleable.SmileItem_resId,0));
        ta.recycle();

    }

    private void initViews() {
        LayoutInflater.from(mContext).inflate(R.layout.smile_item,this);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);

    }

    public void setResId(int resId){
        ivIcon.setImageResource(resId);
    }

    public void setBackground(int resId){
        ivIcon.setBackgroundResource(resId);
    }
}
