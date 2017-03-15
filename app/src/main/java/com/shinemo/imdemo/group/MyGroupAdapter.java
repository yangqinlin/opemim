package com.shinemo.imdemo.group;

import android.content.Context;

import com.shinemo.imdemo.R;
import com.shinemo.openim.service.group.model.Group;
import com.shinemo.widget.rvhelper.adapter.CommonAdapter;
import com.shinemo.widget.rvhelper.base.ViewHolder;

import java.util.List;

/**
 * Created by yangqinlin on 17/3/6.
 */

public class MyGroupAdapter extends CommonAdapter<Group> {

    public MyGroupAdapter(Context context, int layoutId, List<Group> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Group group) {
        holder.setText(R.id.tv_title, group.getGroupName());
        holder.setImageResource(R.id.img_avatar, R.drawable.avatar_group);
    }
}
