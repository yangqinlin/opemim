package com.shinemo.imdemo.chat;

import com.shinemo.imdemo.BaseView;
import com.shinemo.openim.service.msg.model.IMMessage;

import java.util.List;

/**
 * Created by yangqinlin on 17/3/2.
 */

public interface ChatView extends BaseView {

    void showInitData(List<IMMessage> result);

    void showLatestData(List<IMMessage> result);

    void notifyDataChange(List<IMMessage> result);

    void hideRefreshLayout();

    void sendSuccess(IMMessage imMessage);

    void sendFail(IMMessage imMessage);

    void loadNewMessages(List<IMMessage> newMessages);

}
