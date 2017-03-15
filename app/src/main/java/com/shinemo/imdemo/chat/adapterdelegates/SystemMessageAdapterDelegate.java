package com.shinemo.imdemo.chat.adapterdelegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shinemo.imdemo.R;
import com.shinemo.openim.service.msg.model.IMMessage;

import java.util.List;

public class SystemMessageAdapterDelegate extends BaseAdapterDelegate {

    private LayoutInflater inflater;

    public SystemMessageAdapterDelegate(Activity activity) {
        super(activity);
        inflater = activity.getLayoutInflater();
    }

    @Override
    protected boolean isForViewType(@NonNull List<IMMessage> items, int position) {
        IMMessage imMessage = items.get(position);
        return imMessage.getMsgType() == IMMessage.MSG_TYPE_SYSTEM;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new SystemViewHolder(inflater.inflate(R.layout.chat_system, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<IMMessage> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        SystemViewHolder textSendViewHolder = (SystemViewHolder) holder;
        IMMessage imMessage = items.get(position);
        if(!TextUtils.isEmpty(imMessage.getContent())) {
            textSendViewHolder.content.setText(imMessage.getContent());
        } else {
            textSendViewHolder.content.setVisibility(View.GONE);
        }

    }

    static class SystemViewHolder extends BaseViewHolder {

        public TextView content;

        public SystemViewHolder(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.chat_system_text);
        }
    }
}
