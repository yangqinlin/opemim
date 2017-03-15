package com.shinemo.widget.bottombar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shinemo.imdemo.R;


/**
 * Created by yangqinlin on 17/1/6.
 */

public class BottomBarTab extends RelativeLayout {

    private ImageView iconView;
    private TextView titleView;
    private TextView iconBadge;
    private ImageView dotView;

    private int iconNormalResId;
    private int iconSelectedResId;
    private String title;
    private int inActiveColor;
    private int activeColor;
    private int count;
    private int indexInContainer;
    private boolean isActive;

    public BottomBarTab(Context context) {
        super(context);
        intView();
    }

    private void intView() {
        View rootView = inflate(getContext(), R.layout.bottom_bar_item, this);
        iconView = (ImageView) rootView.findViewById(R.id.tab_icon);
        titleView = (TextView) rootView.findViewById(R.id.tab_title);
        iconBadge = (TextView) rootView.findViewById(R.id.tab_badge);
        dotView = (ImageView) rootView.findViewById(R.id.tab_dot);

    }

    public void setIcon(int iconResId) {
        iconView.setBackgroundResource(iconResId);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setTitleColor(int color) {
        titleView.setTextColor(color);
    }

    public int getIconNormalResId() {
        return iconNormalResId;
    }

    public void setIconNormalResId(int iconNormalResId) {
        this.iconNormalResId = iconNormalResId;
    }

    public int getIconSelectedResId() {
        return iconSelectedResId;
    }

    public void setIconSelectedResId(int iconSelectedResId) {
        this.iconSelectedResId = iconSelectedResId;
    }

    public int getInActiveColor() {
        return inActiveColor;
    }

    public void setInActiveColor(int inActiveColor) {
        this.inActiveColor = inActiveColor;
    }

    public int getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(int activeColor) {
        this.activeColor = activeColor;
    }

    public int getIndexInContainer() {
        return indexInContainer;
    }

    public void setIndexInContainer(int indexInContainer) {
        this.indexInContainer = indexInContainer;
    }

    public void select() {
        isActive = true;

        setIcon(getIconSelectedResId());
        setTitleColor(getActiveColor());
    }

    public void deselect() {
        isActive = false;

        setIcon(getIconNormalResId());
        setTitleColor(getInActiveColor());
    }

    public boolean isActive() {
        return isActive;
    }
}
