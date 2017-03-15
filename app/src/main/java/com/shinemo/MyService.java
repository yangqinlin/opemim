package com.shinemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.shinemo.imdemo.R;
import com.shinemo.imdemo.chat.ChatActivity;
import com.shinemo.imdemo.event.Events;
import com.shinemo.imdemo.login.LoginActivity;
import com.shinemo.openim.Account;
import com.shinemo.openim.ApiCallback;
import com.shinemo.openim.ConnectListener;
import com.shinemo.openim.ImClient;
import com.shinemo.openim.ListenerContainer;
import com.shinemo.openim.MessageListener;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.openim.service.login.LoginInfo;
import com.shinemo.openim.service.msg.model.Conversation;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.openim.utils.CollectionUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Notification.DEFAULT_SOUND;

public class MyService extends Service implements MessageListener, ConnectListener {
    public static boolean isLogin;
    private Map<String, Integer> notifyIdMap = new HashMap<>();
    private int notifyId;
    private String notifyKey;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ListenerContainer.getInstance().addMessageListener(this);
        ListenerContainer.getInstance().addConnectListener(this);
        ImClient.getInstance().init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onMessageReceived(IMMessage message) {
        Log.e("shinemo", "@@@@MyService receive: " + message.getContent());
        notifyKey = ChatActivity.buildCurrentCid(message.getChatType(), message.getCid());
        if (!isChatting()) {
            notify(message);
        } else {
            ImClient.getInstance().getMsgService().markAsRead(message.getCid(), message.getChatType(), null);
        }

        EventBus.getDefault().post(new Events.ConversationUpdateEvent());
    }

    private void notify(IMMessage message) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, ChatActivity.class);

        intent.putExtra(ChatActivity.PARAM_CID, message.getCid());
        intent.putExtra(ChatActivity.PARAM_CHAT_TYPE, message.getChatType());
        if (message.getChatType() == IMMessage.CHAT_TYPE_GROUP) {
            String groupName = ImClient.getInstance().getGroupService().getGroupName(Long.valueOf(message.getCid()));
            intent.putExtra(ChatActivity.PARAM_TITLE, groupName);
        } else {
            intent.putExtra(ChatActivity.PARAM_TITLE, message.getFromNick());
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ChatActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("新的消息")
                .setContentText(message.getContent())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(DEFAULT_SOUND)
                .build();


        if (notifyIdMap.get(notifyKey) == null) {
            notifyId = getNotificationId();
            notifyIdMap.put(notifyKey, notifyId);
        } else {
            notifyId = notifyIdMap.get(notifyKey);
        }
        Log.e("shinemo", "@@@@notifyId: " + notifyId + " notifyKey: " + notifyKey);
        notificationManager.notify(notifyId, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ListenerContainer.getInstance().removeMessageListener(this);
        ListenerContainer.getInstance().removeConnectListener(this);
    }

    @Override
    public void onConnected() {
        Log.e("shinemo", "@@@@service 连接建立");
        Account account = AccountHelper.getInstance().getAccount();
        if (account != null) {
            Log.e("shinemo", "@@@@service自动重登开始");
            autoLogin(account);
        }
    }

    private void autoLogin(Account account) {
        ImClient.getInstance().getLoginService().login(new LoginInfo(account.getMobile(), account.getOpenImToken())
                , new ApiCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        isLogin = true;
                        syncConversations();
                    }

                    @Override
                    public void onFail(int code, String desc) {
                        goToLogin();
                    }
                });
    }

    private void syncConversations() {
        ImClient.getInstance().getMsgService().queryAllConversationsFromNet(new ApiCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> result) {
                if (CollectionUtil.isNotEmpty(result)) {
                    syncMessages(result);
                }
            }

            @Override
            public void onFail(int code, String desc) {

            }
        });
    }

    private void syncMessages(List<Conversation> result) {
        ImClient.getInstance().getMsgService().syncMessages(result, new ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if(result) {
                    EventBus.getDefault().post(new Events.OffMessageEvent());
                }
            }

            @Override
            public void onFail(int code, String desc) {

            }
        });
    }

    @Override
    public void onDisConnected() {
        isLogin = false;
    }

    @Override
    public void onKickout() {
        Log.e("shinemo", "@@@@踢下线");
        goToLogin();
    }

    private void goToLogin() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    private int getNotificationId() {
        if (notifyId >= Integer.MAX_VALUE - 100) {
            notifyId = 100;
        }
        notifyId++;
        return notifyId;
    }

    private boolean isChatting() {
        return notifyKey.equals(ChatActivity.getCurrentCid());
    }
}
