package com.shinemo.imdemo.chat.adapterdelegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shinemo.imdemo.R;
import com.shinemo.openim.service.msg.model.IMMessage;

import java.util.List;

/**
 * Created by yangqinlin on 17/3/1.
 */

public class ChatUnknowDelegate extends BaseReceivedAdapterDelegate {

    private LayoutInflater inflater;

    public ChatUnknowDelegate(Activity activity) {
        super(activity);
        inflater = activity.getLayoutInflater();
    }

    @Override
    protected boolean isForViewType(@NonNull List<IMMessage> items, int position) {
        return true;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.chat_receive_unknow, parent, false);
        return new ChatUnkonwViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull List<IMMessage> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        super.onBindViewHolder(items, position, holder, payloads);
        ChatUnkonwViewHolder viewHolder = (ChatUnkonwViewHolder) holder;
        viewHolder.root.setBackgroundResource(R.drawable.xx_qp_wz);
        viewHolder.nameView.setText(mContext.getString(R.string.msg_not_support));
        viewHolder.descView.setText(mContext.getString(R.string.install_new));
        viewHolder.titleView.setText(mContext.getString(R.string.click_up));
    }

    class ChatUnkonwViewHolder extends BaseReceivedViewHolder {

        public View root;
        public TextView nameView;
        public TextView descView;
        public TextView titleView;

        public ChatUnkonwViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.message_receive_unknow);
            nameView = (TextView) itemView.findViewById(R.id.chat_qp_name);
            descView = (TextView) itemView.findViewById(R.id.chat_qp_desc);
            titleView = (TextView) itemView.findViewById(R.id.chat_qp_title);
        }
    }
}
