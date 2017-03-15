package com.shinemo.imdemo.chat.adapter.EmojView;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.shinemo.imdemo.R;
import com.shinemo.utils.SizeUtils;
import com.shinemo.utils.SmileUtils;


/**
 * Created by zale on 16/1/21.
 */
public class EmojSmileFragment extends BaseEmojFragment {

    int position;

    private int columns = 7;
    private int rows = 4;
    private int mScreenWidth;
    private int totalheight;

    private String[] mText;
    private int[] mDrawables = SmileUtils.smile;

    private OnSmileClick onClick;

    public static EmojSmileFragment newInstance(int position, OnSmileClick listener, int height) {
        EmojSmileFragment f = new EmojSmileFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("height", height);
        f.setArguments(args);
        f.setOnClickListener(listener);
        return f;
    }

    public void init() {
        int size = mDrawables.length;
        mText = new String[size];
        int i = 0;
        for (int id : mDrawables) {
            mText[i] = SmileUtils.mFaceMap1_2.get(id);
            i++;
        }
    }

    public interface OnSmileClick {
        void onSmileClick(String text);

        void onDeleteClick();
    }

    public void setOnClickListener(OnSmileClick listener) {
        this.onClick = listener;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = (int) getArguments().get("position");
        totalheight = getArguments().getInt("height");
        totalheight = totalheight - SizeUtils.dip2px(getActivity(), 50);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createPagerItemView(position, getActivity());
        return view;
    }

    public int getCount() {
        int count = mDrawables.length / (columns * rows) + 1;
        return (mDrawables.length + count) / (columns * rows) + 1;
    }

    public RecyclerView createPagerItemView(int pageIndex, Context context) {
        init();
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int width = mScreenWidth / columns;
        int height = totalheight / rows;
        RecyclerView recyclerView = new RecyclerView(context);
        RecycleAdapter recycleAdapter = new RecycleAdapter(width, height);
        recycleAdapter.pageIndex = pageIndex;
        recyclerView.setAdapter(recycleAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, columns);
        recyclerView.setLayoutManager(gridLayoutManager);
        return recyclerView;
    }

    protected class RecycleAdapter extends RecyclerView.Adapter implements View.OnClickListener {
        private int mItemWidth; // GridView元素的宽度
        private int mItemHeight; // GridView元素的高度
        private int pageIndex; // 表示第几页

        public RecycleAdapter(int width, int height) {
            this.mItemWidth = width;
            this.mItemHeight = height;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new ImageView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder) {
                ImageView imageView = (ImageView) holder.itemView;
                imageView.setFocusable(false);
                imageView.setFocusableInTouchMode(false);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setBackgroundResource(R.drawable.white_item_click_transparent);
                if (position == rows * columns - 1) {
                    imageView.setImageResource(R.drawable.smile_delete);
                } else {
                    int p = rows * columns * pageIndex + position - pageIndex;
                    if (p < mDrawables.length) {
                        imageView.setImageResource(mDrawables[p]);
                        imageView.setTag(R.id.action_bar, mText[p]);
                    }
                }
                imageView.setTag(position);
                imageView.setOnClickListener(this);
                imageView.setLayoutParams(new GridView.LayoutParams(mItemWidth,
                        mItemHeight));
            }

        }

        @Override
        public int getItemCount() {
            return columns * rows;
        }

        @Override
        public void onClick(View v) {
            if (onClick == null) return;
            int position = (Integer) v.getTag();
            if (position == rows * columns - 1) {
                onClick.onDeleteClick();
                return;
            }
            String str = (String) v.getTag(R.id.action_bar);
            if (!TextUtils.isEmpty(str)) {
                int pos = rows * columns * pageIndex + position - pageIndex + 1;
                onClick.onSmileClick(str);
            }
        }

       private class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
