package com.shinemo.widget.bottombar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shinemo.imdemo.R;

import java.util.List;

/**
 * Created by yangqinlin on 17/1/6.
 */

public class BottomBar extends LinearLayout implements View.OnClickListener {

    private LinearLayout tabContainer;
    private List<BottomBarTab> tabs;
    private int currentTabPosition;
    private int tabXmlResource;
    private OnTabSelectListener onTabSelectListener;


    public BottomBar(Context context) {
        super(context);
        init(context, null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.BottomBar, 0, 0);
        tabXmlResource = ta.getResourceId(R.styleable.BottomBar_tabXmlResource, 0);
        ta.recycle();

        View rootView = inflate(context, R.layout.bottom_bar, this);
        tabContainer = (LinearLayout) rootView.findViewById(R.id.bottom_bar_item_container);
        setItems(null);
    }

    public void setItems(List<BottomBarTab> tabs) {
        if(tabs != null) {
            this.tabs = tabs;
        } else {
            TabParser parser = new TabParser(getContext(), tabXmlResource);
            this.tabs = parser.getTabs();
        }

        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        for (BottomBarTab tab : this.tabs) {
            if(tab.getIndexInContainer() == currentTabPosition) {
                tab.select();
            } else {
                tab.deselect();
            }
            tab.setLayoutParams(layoutParams);
            tab.setOnClickListener(this);
            tabContainer.addView(tab);
        }

    }

    @Override
    public void onClick(View v) {
        handleClick(v);
    }

    private void handleClick(View v) {
        BottomBarTab oldTab = getCurrentTab();
        BottomBarTab newTab = (BottomBarTab) v;

        if(newTab.getIndexInContainer() != currentTabPosition) {
            oldTab.deselect();
            newTab.select();
            currentTabPosition = newTab.getIndexInContainer();

            if(onTabSelectListener != null) {
                onTabSelectListener.onTabSelected(newTab.getId());
            }
        }
    }

    public BottomBarTab getCurrentTab() {
        return getTabAtPosition(getCurrentTabPosition());
    }

    public int getCurrentTabPosition() {
        return currentTabPosition;
    }

    public BottomBarTab getTabAtPosition(int position) {
        return (BottomBarTab) tabContainer.getChildAt(position);
    }

    public void setOnTabSelectListener(@Nullable OnTabSelectListener listener) {
        onTabSelectListener = listener;

        if (onTabSelectListener != null) {
            listener.onTabSelected(getCurrentTab().getId());
        }
    }

    public interface OnTabSelectListener {
        void onTabSelected(@IdRes int tabId);
    }
}
