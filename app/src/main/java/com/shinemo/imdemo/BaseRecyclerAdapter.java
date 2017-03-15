package com.shinemo.imdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.shinemo.openim.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by panjiejun on 2017/3/9.
 */

public abstract class BaseRecyclerAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    protected List<T> mList = new ArrayList<T>();
    protected Context mContext;

    public BaseRecyclerAdapter(Context context, List<T> list){
        mContext = context;
        mList = list;
    }

    @Override
    public abstract V onCreateViewHolder(ViewGroup parent, int viewType);
//
//    @Override
//    public abstract void onBindViewHolder(V a, int position);


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindView((V)holder, position);
    }

    public abstract void onBindView(V a, int position);

    @Override
    public int getItemCount() {
        if (!CollectionUtil.isEmpty(mList)) {
            return mList.size();
        }
        return 0;
    }
}
