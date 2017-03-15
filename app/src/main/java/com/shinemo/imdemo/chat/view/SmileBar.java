package com.shinemo.imdemo.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.shinemo.imdemo.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 表情底部栏
 * Created by Yetwish on 2015/9/5.
 */
public class SmileBar extends LinearLayout implements View.OnClickListener {

    private Context mContext;

    private LinearLayout llContainer;

    private int mCurPosition = -1;

    private SmileItemSelectedListener mListener;

    private Map<SmileItem, Integer> mViewsMap = new HashMap<>();

    public SmileBar(Context context) {
        super(context);
        this.mContext = context;
        initViews();
    }

    public SmileBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews();
    }


    private void initViews() {
        LayoutInflater.from(mContext).inflate(R.layout.smile_bar, this);
        llContainer = (LinearLayout) findViewById(R.id.smileItemContainer);
    }

    public void setSmileSelectListener(SmileItemSelectedListener listener) {
        this.mListener = listener;
    }

    public void setResIds(int resIds[]) {
        for (int i = 0; i < resIds.length; i++) {
            SmileItem item = new SmileItem(mContext);
            item.setResId(resIds[i]);
            item.setOnClickListener(this);
            mViewsMap.put(item, i);
            llContainer.addView(item);
            if (i == 0)
                item.performClick();
        }
    }


    @Override
    public void onClick(View v) {
        int position = mViewsMap.get(v);
        if (position == mCurPosition)
            return;
        setCurSelectedItem(position);
        if (mListener != null) {
            mListener.onItemSelected(position);
        }
    }

    public void setCurSelectedItem(int position) {
        mCurPosition = position;
        for (SmileItem item : mViewsMap.keySet()) {
            if (mViewsMap.get(item) == position)
                item.setBackground(R.color.smile_item_check);
            else item.setBackground(R.drawable.smile_item_selector);
        }
    }

    public int getmCurPosition() {
        return mCurPosition;
    }


    public interface SmileItemSelectedListener {

        void onItemSelected(int position);
    }
}
