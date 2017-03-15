package com.shinemo.imdemo.chat.adapterdelegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinemo.imdemo.R;
import com.shinemo.openim.service.msg.model.IMMessage;

import java.util.List;

/**
 * Created by yangqinlin on 17/3/7.
 */
public abstract class BaseReceivedAdapterDelegate extends BaseAdapterDelegate {

    protected Context mContext;

    public BaseReceivedAdapterDelegate(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull List<IMMessage> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        super.onBindViewHolder(items, position, holder, payloads);
        IMMessage imMessage = items.get(position);
        BaseReceivedViewHolder baseSendViewHolder = (BaseReceivedViewHolder) holder;
        baseSendViewHolder.avatar.setImageResource(R.drawable.avatar_def);
        if(imMessage.getChatType() == IMMessage.CHAT_TYPE_GROUP) {
            baseSendViewHolder.name.setText(imMessage.getFromNick());
        } else {
            baseSendViewHolder.name.setVisibility(View.GONE);
        }

    }

    static class BaseReceivedViewHolder extends BaseViewHolder {

        public ImageView avatar;
        public TextView name;

        public BaseReceivedViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.chat_receive_name);
            avatar = (ImageView) itemView.findViewById(R.id.receive_avatar);
        }
    }
}
