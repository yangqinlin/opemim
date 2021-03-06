package com.shinemo.imdemo.chat.adapterdelegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shinemo.imdemo.R;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.utils.SmileUtils;

import java.util.List;

/**
 * Created by yangqinlin on 17/3/1.
 */

public class TextReceivedAdapterDelegate extends BaseReceivedAdapterDelegate {

    private LayoutInflater inflater;

    public TextReceivedAdapterDelegate(Activity activity) {
        super(activity);
        inflater = activity.getLayoutInflater();
    }

    @Override
    protected boolean isForViewType(@NonNull List<IMMessage> items, int position) {
        IMMessage imMessage = items.get(position);
        return imMessage.getMsgType() == com.shinemo.openim.service.msg.model.IMMessage.MSG_TYPE_TEXT
                && !imMessage.getFromId().equals(AccountHelper.getInstance().getUserId());
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new TextReceivedViewHolder(inflater.inflate(R.layout.chat_received_message, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<IMMessage> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        super.onBindViewHolder(items, position, holder, payloads);
        TextReceivedViewHolder textSendViewHolder = (TextReceivedViewHolder) holder;
        IMMessage imMessage = items.get(position);
        textSendViewHolder.content.setText(SmileUtils.getSmiledText(mContext, imMessage.getContent()));
        textSendViewHolder.content.setTextColor(mContext.getResources().getColor(R.color.s_text_main_color));
        textSendViewHolder.layout.setBackgroundResource(R.drawable.xx_qp_br_ta);
    }

    static class TextReceivedViewHolder extends BaseReceivedViewHolder {

        public TextView content;
        public View layout;

        public TextReceivedViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.message_receive_layout);
            content = (TextView) itemView.findViewById(R.id.message_receive_content);

        }
    }
}
