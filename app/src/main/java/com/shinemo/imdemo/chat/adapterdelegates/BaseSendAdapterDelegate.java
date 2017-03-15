package com.shinemo.imdemo.chat.adapterdelegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shinemo.imdemo.R;
import com.shinemo.imdemo.chat.ChatView;
import com.shinemo.openim.ApiCallback;
import com.shinemo.openim.ImClient;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.openim.utils.Handlers;
import com.shinemo.widget.dialog.CommonDialog;

import java.util.List;

/**
 * Created by yangqinlin on 17/3/7.
 */
public abstract class BaseSendAdapterDelegate extends BaseAdapterDelegate {

    protected CommonDialog mResendDialog;
    protected Context mContext;

    public BaseSendAdapterDelegate(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull List<IMMessage> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        super.onBindViewHolder(items, position, holder, payloads);
        IMMessage imMessage = items.get(position);
        BaseSendViewHolder baseSendViewHolder = (BaseSendViewHolder) holder;
        baseSendViewHolder.mSendAvs.setImageResource(R.drawable.avatar_def);
        baseSendViewHolder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResendDialog = new CommonDialog(mContext, new CommonDialog.onConfirmListener() {
                    @Override
                    public void onConfirm() {
                        baseSendViewHolder.progressBar.setVisibility(View.VISIBLE);
                        baseSendViewHolder.status.setVisibility(View.GONE);
                        reSend(imMessage);
                    }
                });
                mResendDialog.setTitle(mContext.getString(R.string.if_resend));
                mResendDialog.show();
            }
        });
        baseSendViewHolder.status.setTag(imMessage);
        switch (imMessage.getMsgStatus()) {
            case IMMessage.STATUS_SUCCESS:
                baseSendViewHolder.progressBar.setVisibility(View.GONE);
                baseSendViewHolder.status.setVisibility(View.GONE);
                break;
            case IMMessage.STATUS_FAIL:
                baseSendViewHolder.progressBar.setVisibility(View.GONE);
                baseSendViewHolder.status.setVisibility(View.VISIBLE);
                break;
            case IMMessage.STATUS_SENDING:
                baseSendViewHolder.status.setVisibility(View.GONE);
                baseSendViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void reSend(final IMMessage imMessage) {
        ImClient.getInstance().getMsgService().sendMessage(imMessage, new ApiCallback<IMMessage>() {
            @Override
            public void onSuccess(IMMessage result) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        ((ChatView)mContext).sendSuccess(result);
                    }
                });
            }

            @Override
            public void onFail(int code, String desc) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        ((ChatView)mContext).sendFail(imMessage);
                    }
                });
            }
        });
    }

    static class BaseSendViewHolder extends BaseAdapterDelegate.BaseViewHolder {

        public ProgressBar progressBar;
        public ImageView status;
        public TextView unreadCount;
        public ImageView mSendAvs;

        public BaseSendViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.send_progress);
            status = (ImageView) itemView.findViewById(R.id.send_status);
            unreadCount = (TextView) itemView.findViewById(R.id.unread_count);
            mSendAvs = (ImageView) itemView.findViewById(R.id.send_avatar);
        }
    }
}
