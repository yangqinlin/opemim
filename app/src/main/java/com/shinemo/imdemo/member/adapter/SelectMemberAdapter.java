package com.shinemo.imdemo.member.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.shinemo.contacts.model.UserVo;
import com.shinemo.imdemo.BaseRecyclerAdapter;
import com.shinemo.imdemo.R;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.widget.image.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 1/26/16.
 */
public class SelectMemberAdapter extends BaseRecyclerAdapter<UserVo, SelectMemberAdapter.ViewHolder> {

    private TreeMap<String, UserVo> map = new TreeMap<>();

    public SelectMemberAdapter(Context context, List<UserVo> list) {
        super(context, list);
    }

    @Override
    public  SelectMemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.select_member_item, null));
    }

    public List<UserVo> getSelect(){
        List<UserVo> members = new ArrayList<>();
        if (map != null && map.size() > 0) {
            members = new ArrayList<>(map.values());
        }
        return members;
    }

    @Override
    public void onBindView(ViewHolder viewHolder, int position) {
        UserVo userVo = mList.get(position);
        viewHolder.name.setText(userVo.getName());
        viewHolder.avatar.setImageResource(R.drawable.avatar_def);
        viewHolder.checkBox.setOnCheckedChangeListener(null);
        viewHolder.checkBox.setChecked(map.get(userVo.getUid()) != null ? true : false);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    map.put(userVo.getUid(), userVo);
                } else {
                    map.remove(userVo.getUid());
                }
            }
        });
    }

    public void selectMyself() {
        UserVo me = new UserVo();
        me.setUid(AccountHelper.getInstance().getUserId());
        me.setName(AccountHelper.getInstance().getName());
        map.put(me.getUid(), me);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.check_box)
        CheckBox checkBox;
        @BindView(R.id.select_member_item_avatar)
        CircleImageView avatar;
        @BindView(R.id.select_member_item_name)
        TextView name;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
