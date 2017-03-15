package com.shinemo.imdemo.group;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinemo.imdemo.R;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.openim.service.group.model.GroupMember;
import com.shinemo.openim.utils.CollectionUtil;
import com.shinemo.widget.image.CircleImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by panjiejun on 2017/3/9.
 */

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<GroupMember> groupMembers;
    private LayoutInflater inflater;
    private MoreAction moreAction;

    private boolean isEdit = false;

    GroupMemberAdapter(MoreAction moreAction, ArrayList<GroupMember> groupMembers) {
        this.mContext = (Context)moreAction;
        this.groupMembers = groupMembers;
        this.moreAction = moreAction;
        inflater = LayoutInflater.from(mContext);
    }

    public void rmMember(GroupMember groupMember){
        if (!CollectionUtil.isEmpty(groupMembers)) {
            groupMembers.remove(groupMember);
            notifyDataSetChanged();
        }
    }

    public ArrayList<GroupMember> getGroupMembers() {
        return groupMembers;
    }

    public boolean idEdit() {
        return isEdit;
    }

    public void setEdit(boolean del) {
        isEdit = del;
        notifyDataSetChanged();
    }

    @Override
    public GroupMemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.avatar_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupMemberAdapter.ViewHolder holder, int position) {
        GroupMember groupMember = groupMembers.get(position);
        holder.userAvatar.setImageResource(R.drawable.avatar_def);
        holder.userName.setText(groupMember.getNick());

        if (isEdit && !AccountHelper.getInstance().getUserId().equals(groupMember.getAccount())) {
            holder.deletebtn.setVisibility(View.VISIBLE);
        } else {
            holder.deletebtn.setVisibility(View.GONE);
        }
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moreAction != null) {
                    moreAction.delMember(groupMember);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (!CollectionUtil.isEmpty(groupMembers)) {
            return groupMembers.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.user_avatar)
        CircleImageView userAvatar;
        @BindView(R.id.group_member_delete)
        ImageView deletebtn;
        @BindView(R.id.user_name)
        TextView userName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    interface MoreAction{
        void delMember(GroupMember groupMember);
    }
}
