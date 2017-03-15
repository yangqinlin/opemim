package com.shinemo.imdemo.chat;

import android.content.Context;

import com.shinemo.imdemo.BasePresenter;
import com.shinemo.openim.ApiCallback;
import com.shinemo.openim.ImClient;
import com.shinemo.openim.db.entity.ConversationEntity;
import com.shinemo.openim.db.manager.ConversationDBManager;
import com.shinemo.openim.db.manager.MessageDBManager;
import com.shinemo.openim.helper.IMMapper;
import com.shinemo.openim.service.msg.MsgService;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.openim.utils.CollectionUtil;
import com.shinemo.openim.utils.Handlers;
import com.shinemo.openim.utils.NetworkUtil;

import java.util.List;

/**
 * Created by yangqinlin on 17/3/2.
 */

public class ChatPresenter extends BasePresenter<ChatView> {

    private static final int PAGESIZE = 20;
    private MsgService mMsgService;

    public ChatPresenter(ChatView view) {
        super(view);
        mMsgService = ImClient.getInstance().getMsgService();
    }

    public void queryFromDBForView(String account, int chatType) {
        mMsgService.queryMessageListFromDB(account, chatType, 0, PAGESIZE, new ApiCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(final List<IMMessage> result) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        mView.showInitData(result);
                    }
                });

            }

            @Override
            public void onFail(int code, String desc) {

            }
        });
    }

    public void queryLatestData(String account, int chatType) {
        mMsgService.queryMessageList(account, chatType, 0, PAGESIZE, new ApiCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(final List<IMMessage> result) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        mView.showLatestData(result);
                    }
                });
            }

            @Override
            public void onFail(int code, String desc) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideRefreshLayout();
                    }
                });
            }
        });
    }

    public void loadMoreMessages(String account, int chatType, long startMsgId) {
        if (NetworkUtil.isNetworkAvailable((Context) mView)) {
            mMsgService.queryMessageList(account, chatType, startMsgId, PAGESIZE, new ApiCallback<List<IMMessage>>() {
                @Override
                public void onSuccess(final List<IMMessage> result) {
                    Handlers.postMain(new Runnable() {
                        @Override
                        public void run() {
                            mView.notifyDataChange(result);
                            mView.hideRefreshLayout();
                        }
                    });
                }

                @Override
                public void onFail(int code, String desc) {
                    Handlers.postMain(new Runnable() {
                        @Override
                        public void run() {
                            mView.hideRefreshLayout();
                        }
                    });
                }
            });
        } else {
            mMsgService.queryMessageListFromDB(account, chatType, startMsgId, PAGESIZE, new ApiCallback<List<IMMessage>>() {
                @Override
                public void onSuccess(final List<IMMessage> result) {
                    Handlers.postMain(new Runnable() {
                        @Override
                        public void run() {
                            Handlers.postMain(new Runnable() {
                                @Override
                                public void run() {
                                    mView.notifyDataChange(result);
                                    mView.hideRefreshLayout();
                                }
                            });
                        }
                    });

                }

                @Override
                public void onFail(int code, String desc) {
                    Handlers.postMain(new Runnable() {
                        @Override
                        public void run() {
                            mView.hideRefreshLayout();
                        }
                    });
                }
            });
        }
    }

    public void sendMessage(final IMMessage imMessage) {
        saveToDB(imMessage);

        ImClient.getInstance().getMsgService().sendMessage(imMessage, new ApiCallback<IMMessage>() {
            @Override
            public void onSuccess(final IMMessage result) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        mView.sendSuccess(result);
                    }
                });
            }

            @Override
            public void onFail(int code, String desc) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        mView.sendFail(imMessage);
                    }
                });
            }
        });
    }

    private void saveToDB(IMMessage imMessage) {
        MessageDBManager.getInstance().refresh(IMMapper.INSTANCE.imMessageToDB(imMessage));
        ConversationEntity conversationEntity = ConversationDBManager.getInstance().getConversation(imMessage.getCid(), imMessage.getChatType());
        conversationEntity.setContent(imMessage.getContent());
        conversationEntity.setModifyTime(System.currentTimeMillis());
        ConversationDBManager.getInstance().refresh(conversationEntity);
    }

    public void queryNewMessages(long startId, String cid, int chatType) {
        List<IMMessage> imMessages = mMsgService.queryNewMessages(startId, cid, chatType);
        if(CollectionUtil.isNotEmpty(imMessages)) {
            mView.loadNewMessages(imMessages);
        }
    }
}
