package com.shinemo.imdemo.chat.adapterdelegates;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shinemo.imdemo.R;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.openim.service.msg.attachment.ImageAttachment;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.openim.utils.HttpUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.id.message;

/**
 * Created by yangqinlin on 17/3/1.
 */

public class PictureReceivedAdapterDelegate extends BaseReceivedAdapterDelegate {

    private LayoutInflater inflater;

    public PictureReceivedAdapterDelegate(Activity activity) {
        super(activity);
        inflater = activity.getLayoutInflater();
    }

    @Override
    protected boolean isForViewType(@NonNull List<IMMessage> items, int position) {
        IMMessage imMessage = items.get(position);
        return imMessage.getMsgType() == IMMessage.MSG_TYPE_IMAGE
                && !imMessage.getFromId().equals(AccountHelper.getInstance().getUserId());
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.chat_received_picture, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<IMMessage> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        super.onBindViewHolder(items, position, holder, payloads);
        ViewHolder viewHolder = (ViewHolder) holder;
        IMMessage imageVo = items.get(position);
        setImageLayout((ImageAttachment)imageVo.getAttachment(), viewHolder.image, viewHolder.mask);
        viewHolder.image.setTag(message);
        if (!TextUtils.isEmpty(imageVo.getContent())) {
            String url = HttpUtils.getThumbleUrl(imageVo.getContent());
            setPicture(url, viewHolder.image);
        }
        viewHolder.mask.setBackgroundResource(R.drawable.xx_hh_pic);
    }


    static class ViewHolder extends BaseReceivedViewHolder {
        @BindView(R.id.picture_receive_image)
        SimpleDraweeView image;
        @BindView(R.id.picture_receive_image_mask)
        View mask;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
