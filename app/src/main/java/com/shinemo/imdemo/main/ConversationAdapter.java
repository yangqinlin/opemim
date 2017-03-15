package com.shinemo.imdemo.main;

import android.content.Context;

import com.shinemo.imdemo.R;
import com.shinemo.openim.service.msg.model.Conversation;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.utils.SmileUtils;
import com.shinemo.utils.TimeUtil;
import com.shinemo.widget.rvhelper.adapter.CommonAdapter;
import com.shinemo.widget.rvhelper.base.ViewHolder;

import java.util.List;

public class ConversationAdapter extends CommonAdapter<Conversation> {

    public ConversationAdapter(Context context, int layoutId, List<Conversation> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Conversation conversation) {
        if(conversation.getChatType() == IMMessage.CHAT_TYPE_GROUP) {
            holder.setImageResource(R.id.img_head, R.drawable.avatar_group);
        } else {
            holder.setImageResource(R.id.img_head, R.drawable.avatar_def);
        }
        holder.setText(R.id.tv_name, conversation.getDisplayName());
        holder.setText(R.id.tv_content, SmileUtils.getSmiledText(mContext, conversation.getContent()));
        holder.setText(R.id.tv_time, TimeUtil.getSimpleDateString(conversation.getModifyTime()));
        if(conversation.isMute()) {
            holder.setVisible(R.id.img_notify, true);
        } else {
            holder.setVisible(R.id.img_notify, false);
        }

        if(conversation.getUnreadCount() > 0) {
            if(conversation.isMute()) {
                holder.setVisible(R.id.img_dot, true);
                holder.setVisible(R.id.tv_unread_count, false);
            } else {
                holder.setVisible(R.id.img_dot, false);
                holder.setVisible(R.id.tv_unread_count, true);
                if (conversation.getUnreadCount() > 99) {
                    holder.setText(R.id.tv_unread_count, "99+");
                } else {
                    holder.setText(R.id.tv_unread_count, String.valueOf(conversation.getUnreadCount()));
                }
            }
        } else {
            holder.setVisible(R.id.tv_unread_count, false);
            holder.setVisible(R.id.img_dot, false);
        }

        IMMessage imMessage = conversation.getLastMessage();
        if(imMessage != null) {
            if (imMessage.getMsgStatus() == IMMessage.STATUS_SENDING || imMessage.getMsgStatus() == IMMessage.STATUS_FAIL) {
                holder.setTextViewLeftDrawable(R.id.tv_content, mContext.getResources().getDrawable(R.drawable.xx_ic_list_zzfs));
            } else {
                holder.setTextViewLeftDrawable(R.id.tv_content, null);
            }
        }
    }
}
