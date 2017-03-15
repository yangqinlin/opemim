package com.shinemo.imdemo.chat.adapterdelegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shinemo.imdemo.R;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.openim.service.msg.attachment.AudioAttachment;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.utils.audio.AudioManagers;
import com.shinemo.utils.audio.OnPlayListener;
import com.shinemo.widget.audio.progress.RecordProgressView;

import java.io.File;
import java.util.List;

/**
 * Created by yangqinlin on 17/3/1.
 */

public class AudioSendAdapterDelegate extends BaseSendAdapterDelegate {

    private LayoutInflater inflater;

    public AudioSendAdapterDelegate(Activity activity) {
        super(activity);
        inflater = activity.getLayoutInflater();
    }

    @Override
    protected boolean isForViewType(@NonNull List<IMMessage> items, int position) {
        IMMessage imMessage = items.get(position);
        return imMessage.getMsgType() == IMMessage.MSG_TYPE_AUDIO
                && imMessage.getFromId().equals(AccountHelper.getInstance().getUserId());
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.chat_send_audio, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<IMMessage> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        super.onBindViewHolder(items, position, holder, payloads);
        ViewHolder viewHolder = (ViewHolder) holder;
        IMMessage imMessage = items.get(position);
        viewHolder.mLayoutView.setDeleteViewVisible(false);
        AudioAttachment audio = (AudioAttachment)(imMessage.getAttachment());
        String path = imMessage.getContent();
        if(audio != null) {
            if (!TextUtils.isEmpty(audio.getPath())) {
                File file = new File(audio.getPath());
                if (file.exists()) {
                    path = audio.getPath();
                }
            }
        }
        OnPlayListener listener = new OnPlayListener() {
            @Override
            public void onPlayStateListener(String url, int state) {
                viewHolder.mLayoutView.setState(state);
            }

            @Override
            public void onProgressListener(String url, int progress) {
                viewHolder.mLayoutView.setProgress(progress);
            }

            @Override
            public void onPlayErrorListener(String url, int error) {
                viewHolder.mLayoutView.setProgressGone();
            }
        };
        String url = AudioManagers.getInstance().mCurUrl;
        if(!TextUtils.isEmpty(url) && url.equals(path)){
            AudioManagers.getInstance().mCurPlayListener = listener;
        }
        viewHolder.mLayoutView.loadRecordUrl(position, path, audio.getDuration(), audio.getVoice(), listener);
    }

    static class ViewHolder extends BaseSendViewHolder {
        RecordProgressView mLayoutView;
        View bgView;

        ViewHolder(View view) {
            super(view);
            mLayoutView = (RecordProgressView) view.findViewById(R.id.voice_bg);
            bgView = view.findViewById(R.id.record_background);
        }
    }
}
