package com.shinemo.imdemo.chat.adapterdelegates;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.shinemo.imdemo.R;
import com.shinemo.openim.service.msg.attachment.ImageAttachment;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.utils.SizeUtils;
import com.shinemo.utils.TimeUtil;

import java.util.List;

/**
 * Created by yangqinlin on 17/3/7.
 */
public abstract class BaseAdapterDelegate extends AdapterDelegate<List<IMMessage>> {

    protected Context mContext;

    BaseAdapterDelegate(Context context) {
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull List<IMMessage> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        if(baseViewHolder.historyDevide != null) {
            baseViewHolder.historyDevide.setVisibility(View.GONE);
        }
        if(baseViewHolder.time != null) {
            setTime(baseViewHolder.time, items, position);
        }
    }

    private void setTime(TextView time, List<IMMessage> items, int position) {
        if (position == 0) {
            time.setText(TimeUtil.getDetailDateString(items.get(0).getSendTime()));
            time.setVisibility(View.VISIBLE);
        } else {
            // 两条消息时间离得如果稍长，显示时间
            if (TimeUtil.isCloseEnough(items.get(position).getSendTime(),
                    items.get(position - 1).getSendTime())) {
                time.setVisibility(View.GONE);
            } else {
                time.setText(TimeUtil.getDetailDateString(items.get(position).getSendTime()));
                time.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void setImageLayout(ImageAttachment picture, ImageView image, View mask) {
        if (picture == null) {
            return;
        }
        int width = picture.getWidth();
        int height = picture.getHeight();
        if (width == 0 || height == 0) {
            return;
        }
        int size = mContext.getResources().getDimensionPixelSize(
                R.dimen.chat_picture_size);
        int min_size = mContext.getResources().getDimensionPixelSize(
                R.dimen.chat_picture_min_size);
        ViewGroup.LayoutParams params = image.getLayoutParams();
        int pw = size;
        int ph = size;
        if (width > height) {
            ph = size * height / width;
            if (ph < min_size) {
                ph = min_size;
            }
        } else {
            pw = width * size / height;
            if (pw < min_size) {
                pw = min_size;
            }
        }
        params.width = pw;
        params.height = ph;
        image.setLayoutParams(params);

        ViewGroup.LayoutParams param2 = mask.getLayoutParams();
        param2.width = pw;
        param2.height = ph;
        mask.setLayoutParams(params);
    }

    protected void setPicture(String url, SimpleDraweeView imageView) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (url.contains("qiniucdn.com") && !url.contains("?")) {
            int width = imageView.getLayoutParams().width;
            if (width <= 0) {
                width = mContext.getResources().getDisplayMetrics().widthPixels - SizeUtils.dip2px(mContext, 20);
            }
            int height = imageView.getLayoutParams().height;
            if (height <= 0) {
                height = SizeUtils.dip2px(mContext, 150);
            }
            url += "?imageView2/1/w/" + width + "/h/" + height;
        }

        Uri uri = Uri.parse(url);
        imageView.getHierarchy().setPlaceholderImage(R.drawable.chat_picture_holder);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();

        imageView.setController(controller);
    }

    static class BaseViewHolder extends RecyclerView.ViewHolder {

        public TextView time;
        public View historyDevide;

        public BaseViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.timestamp);
            historyDevide = itemView.findViewById(R.id.chat_history_devide);
        }
    }
}
