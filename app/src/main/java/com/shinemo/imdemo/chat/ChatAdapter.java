package com.shinemo.imdemo.chat;

import android.app.Activity;

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;
import com.shinemo.imdemo.chat.adapterdelegates.AudioReceivedAdapterDelegate;
import com.shinemo.imdemo.chat.adapterdelegates.AudioSendAdapterDelegate;
import com.shinemo.imdemo.chat.adapterdelegates.ChatUnknowDelegate;
import com.shinemo.imdemo.chat.adapterdelegates.PictureReceivedAdapterDelegate;
import com.shinemo.imdemo.chat.adapterdelegates.PictureSendAdapterDelegate;
import com.shinemo.imdemo.chat.adapterdelegates.SystemMessageAdapterDelegate;
import com.shinemo.imdemo.chat.adapterdelegates.TextReceivedAdapterDelegate;
import com.shinemo.imdemo.chat.adapterdelegates.TextSendAdapterDelegate;
import com.shinemo.openim.service.msg.model.IMMessage;

import java.util.List;

/**
 * Created by yangqinlin on 17/3/1.
 */

public class ChatAdapter extends ListDelegationAdapter<List<IMMessage>> {

    public ChatAdapter(Activity activity, List<IMMessage> items) {
        this.delegatesManager.addDelegate(new TextSendAdapterDelegate(activity));
        this.delegatesManager.addDelegate(new TextReceivedAdapterDelegate(activity));
        this.delegatesManager.addDelegate(new AudioSendAdapterDelegate(activity));
        this.delegatesManager.addDelegate(new AudioReceivedAdapterDelegate(activity));
        this.delegatesManager.addDelegate(new PictureSendAdapterDelegate(activity));
        this.delegatesManager.addDelegate(new PictureReceivedAdapterDelegate(activity));
        this.delegatesManager.addDelegate(new SystemMessageAdapterDelegate(activity));
        this.delegatesManager.setFallbackDelegate(new ChatUnknowDelegate(activity));

        setItems(items);
    }
}
