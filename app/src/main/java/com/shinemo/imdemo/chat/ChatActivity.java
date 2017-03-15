package com.shinemo.imdemo.chat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rockerhieu.emojicon.EmojiconEditText;
import com.shinemo.imdemo.BaseActivity;
import com.shinemo.imdemo.R;
import com.shinemo.imdemo.chat.view.InputBarView;
import com.shinemo.imdemo.event.Events;
import com.shinemo.imdemo.group.GroupInfoActivity;
import com.shinemo.openim.ListenerContainer;
import com.shinemo.openim.MessageListener;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.openim.helper.ApplicationContext;
import com.shinemo.openim.http.filemanage.FileManage;
import com.shinemo.openim.http.model.FileBean;
import com.shinemo.openim.service.msg.MessageBuilder;
import com.shinemo.openim.service.msg.attachment.AudioAttachment;
import com.shinemo.openim.service.msg.attachment.ImageAttachment;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.openim.utils.CollectionUtil;
import com.shinemo.openim.utils.Handlers;
import com.shinemo.utils.FileUtils;
import com.shinemo.utils.img.CameraUtils;
import com.shinemo.utils.img.PictureUtil;
import com.shinemo.widget.fonticon.FontIcon;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchRootLinearLayout;
import rx.Subscriber;


/**
 * Created by yangqinlin on 17/3/1.
 */

public class ChatActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, TextWatcher, MessageListener, ChatView, InputBarView.MoreAction {

    public static final String PARAM_TITLE = "title";
    public static final String PARAM_CID = "cid";
    public static final String PARAM_CHAT_TYPE = "chatType";

    @BindView(R.id.back)
    FontIcon mBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.srl_content)
    SwipeRefreshLayout mSrlContent;
    @BindView(R.id.chat_detail_voice)
    FontIcon mChatDetailVoice;
    @BindView(R.id.chat_detail_text)
    EmojiconEditText mChatDetailText;
    @BindView(R.id.chat_smile_btn)
    FontIcon mChatSmileBtn;
    @BindView(R.id.chat_detail_add)
    FontIcon mChatDetailAdd;
    @BindView(R.id.chat_detail_send)
    Button mChatDetailSend;
    @BindView(R.id.rootView)
    KPSwitchRootLinearLayout mRootView;

    @BindView(R.id.chat_detail_right)
    FontIcon mChatDetailRight;
    @BindView(R.id.input_bar)
    InputBarView inputBar;

    private int mChatType;
    private String mCid;
    private ChatAdapter mChatAdapter;
    private List<IMMessage> mImMessages = new ArrayList<>();
    private long startMsgId;
    private ChatPresenter mChatPresenter;
    protected Uri mPictureUri;
    private static String currentCid;


    public static void startActivityForResult(Fragment fragment, String title, int chatType, String cid, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), ChatActivity.class);
        intent.putExtra(PARAM_TITLE, title);
        intent.putExtra(PARAM_CHAT_TYPE, chatType);
        intent.putExtra(PARAM_CID, cid);
        fragment.startActivityForResult(intent, requestCode);
    }


    public static void startActivity(Context context, String title, int chatType, String cid) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(PARAM_TITLE, title);
        intent.putExtra(PARAM_CHAT_TYPE, chatType);
        intent.putExtra(PARAM_CID, cid);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mChatType = getIntent().getIntExtra(PARAM_CHAT_TYPE, 1);
        mCid = getIntent().getStringExtra(PARAM_CID);
        inputBar.init(mRvContent, mChatType, mCid, getSupportFragmentManager(), this);
        KeyboardUtil.attach(this, inputBar.getPanelRoot(),
                new KeyboardUtil.OnKeyboardShowingListener() {
                    @Override
                    public void onKeyboardShowing(boolean isShowing) {
                        if (isShowing && mChatAdapter.getItemCount() > 1) {
                            mRvContent.scrollToPosition(mChatAdapter.getItemCount() - 1);
                        }

                    }
                });

        mTvTitle.setText(getIntent().getStringExtra(PARAM_TITLE));

        mSrlContent.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvContent.setLayoutManager(layoutManager);
        mRvContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KPSwitchConflictUtil.hidePanelAndKeyboard(inputBar.getPanelRoot());
                }
                return false;
            }
        });

        mChatAdapter = new ChatAdapter(this, mImMessages);
        mRvContent.setAdapter(mChatAdapter);

        mChatPresenter = new ChatPresenter(this);
        mChatPresenter.queryFromDBForView(mCid, mChatType);

        mChatDetailText.addTextChangedListener(this);
        ListenerContainer.getInstance().addMessageListener(this);

        if(mChatType == IMMessage.CHAT_TYPE_GROUP) {
            mChatDetailRight.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentCid = buildCurrentCid(mChatType, mCid);
    }

    @Override
    public void onStop() {
        super.onStop();
        currentCid = null;
    }

    @OnClick(R.id.chat_detail_right)
    public void clickRight() {
        GroupInfoActivity.startActivity(this, mCid);

    }


    @OnClick(R.id.back)
    public void onBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (inputBar.getPanelRoot().getVisibility() == View.VISIBLE) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(inputBar.getPanelRoot());
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onRefresh() {
        mChatPresenter.loadMoreMessages(mCid, mChatType, startMsgId);
    }

    @Override
    public void showInitData(List<IMMessage> result) {
        if (!CollectionUtil.isEmpty(result)) {
            mImMessages.addAll(result);
            mChatAdapter.notifyDataSetChanged();
            mRvContent.scrollToPosition(mChatAdapter.getItemCount() - 1);
            startMsgId = result.get(0).getMsgId();
        }
        mChatPresenter.queryLatestData(mCid, mChatType);
    }

    @Override
    public void showLatestData(List<IMMessage> result) {
        if (!CollectionUtil.isEmpty(result)) {
            mImMessages.clear();
            mImMessages.addAll(result);
            mChatAdapter.notifyDataSetChanged();
            mRvContent.scrollToPosition(mChatAdapter.getItemCount() - 1);
            startMsgId = result.get(0).getMsgId();
        }
    }

    @Override
    public void notifyDataChange(List<IMMessage> result) {
        if (!CollectionUtil.isEmpty(result)) {
            mImMessages.addAll(0, result);
            mChatAdapter.notifyDataSetChanged();
            mRvContent.scrollToPosition(result.size());
            startMsgId = result.get(0).getMsgId();
        }
    }

    @Override
    public void hideRefreshLayout() {
        if (mSrlContent.isRefreshing()) {
            mSrlContent.setRefreshing(false);
        }
    }

    @Override
    public void sendSuccess(IMMessage imMessage) {
        int index = getMessageIndex(imMessage);
        if (index >= 0 && index < mImMessages.size()) {
            IMMessage item = mImMessages.get(index);
            item.setMsgStatus(IMMessage.STATUS_SUCCESS);
            mChatAdapter.notifyItemChanged(index);
        }
    }

    @Override
    public void sendFail(IMMessage imMessage) {
        int index = getMessageIndex(imMessage);
        if (index >= 0 && index < mImMessages.size()) {
            IMMessage item = mImMessages.get(index);
            item.setMsgStatus(IMMessage.STATUS_FAIL);
            mChatAdapter.notifyItemChanged(index);
        }
    }

    @Override
    public void loadNewMessages(List<IMMessage> newMessages) {
        mImMessages.addAll(newMessages);
        mChatAdapter.notifyDataSetChanged();
        mRvContent.scrollToPosition(mChatAdapter.getItemCount() - 1);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            mChatDetailAdd.setVisibility(View.VISIBLE);
            mChatDetailSend.setVisibility(View.GONE);
        } else {
            mChatDetailAdd.setVisibility(View.GONE);
            mChatDetailSend.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onMessageReceived(final IMMessage message) {
        if (message.getChatType() == mChatType && mCid.equals(message.getCid())) {
            Handlers.postMain(new Runnable() {
                @Override
                public void run() {
                    mImMessages.add(message);
                    mChatAdapter.notifyItemInserted(mImMessages.size() - 1);
                    mRvContent.scrollToPosition(mChatAdapter.getItemCount() - 1);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ListenerContainer.getInstance().removeMessageListener(this);
    }


    @Override
    public void sendAudio(AudioAttachment attachment) {
        IMMessage imMessage = MessageBuilder.createAudioMessage(mChatType, AccountHelper.getInstance().getName(), AccountHelper.getInstance().getUserId(), mCid, attachment, getLastMsgId());
        preSend(imMessage);

        FileManage.getInstance().uploadFile(imMessage.getAttachment()).subscribe(new Subscriber<FileBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("ppppp", "onError " + e.getMessage());
            }

            @Override
            public void onNext(FileBean bean) {
                ((AudioAttachment)imMessage.getAttachment()).setUrl(bean.getFileUrl());
                imMessage.setContent(bean.getFileUrl());
                send(imMessage);
            }
        });
    }

    @Override
    public void sendText(String content) {
        IMMessage imMessage = MessageBuilder.createTextMessage(mChatType, AccountHelper.getInstance().getName(), AccountHelper.getInstance().getUserId(), mCid, content, getLastMsgId());
        preSend(imMessage);
        send(imMessage);
    }

    @Override
    public void takePhoto() {
        mPictureUri = CameraUtils.openCapture(this, FileUtils.newCameraPath(), CameraUtils.REQUEST_IMAGE_CAPTURE);
    }

    private void send(IMMessage imMessage) {
        mChatPresenter.sendMessage(imMessage);
    }

    public void preSend(IMMessage imMessage) {
        mChatDetailText.setText("");
        mImMessages.add(imMessage);
        mChatAdapter.notifyItemInserted(mImMessages.size() - 1);
        mRvContent.scrollToPosition(mChatAdapter.getItemCount() - 1);
    }

    @Override
    public void setListEnd() {
        mRvContent.scrollToPosition(mChatAdapter.getItemCount()-1);
    }

    private int getMessageIndex(IMMessage imMessage) {
        int index = -1;
        for (int i = 0; i < mImMessages.size(); i++) {
            IMMessage message = mImMessages.get(i);
            if (message.getSeqId() == imMessage.getSeqId()) {
                index = i;
            }
        }
        return index;
    }

    private long getLastMsgId() {
        if(CollectionUtil.isNotEmpty(mImMessages)) {
            return mImMessages.get(mImMessages.size() - 1).getMsgId();
        }
        return 0;
    }

    private void sendImage() {
        ImageAttachment picture = PictureUtil.compressAndRotateToBitmapThumbFile(this, mPictureUri);
        mPictureUri = null;
        IMMessage imMessage = MessageBuilder.createImageMessage(mChatType, AccountHelper.getInstance().getName(), AccountHelper.getInstance().getUserId(), mCid, picture, getLastMsgId());
        preSend(imMessage);
        FileManage.getInstance().uploadFile(picture).subscribe(new Subscriber<FileBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("ppppp", "onError " + e.getMessage());
            }

            @Override
            public void onNext(FileBean bean) {
                ((ImageAttachment)imMessage.getAttachment()).setUrl(bean.getFileUrl());
                imMessage.setContent(bean.getFileUrl());
                send(imMessage);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CameraUtils.REQUEST_IMAGE_CAPTURE:
                    if (mPictureUri != null) {
                        saveToDb(ApplicationContext.getInstance(), mPictureUri.getPath());
                        sendImage();
                    }
                    break;
            }
        }
    }

    public static Uri saveToDb(Context context, String filepath) {
        try{
            int index = filepath.lastIndexOf("/");
            if(index > 0){
                String root = filepath.substring(0, index);
                String name = filepath.substring(index+1, filepath.length());
                return saveToDb(context, root, name);
            }
        }catch (Exception e){}
        return null;
    }

    public static Uri saveToDb(Context context, String directory, String filename) {
        String filePath = directory + "/" + filename;
        ContentValues values = new ContentValues(7);
        values.put(MediaStore.Images.Media.TITLE, filename);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, filePath);
        try {
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String getCurrentCid() {
        return currentCid;
    }

    public static String buildCurrentCid(int chatType, String cid) {
        return chatType + "," + cid;
    }

    @Override
    protected void registEventBus() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void unRegistEventBus() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupNameChanged(Events.GroupNameChangeEvent event) {
        mTvTitle.setText(event.name);
        mChatPresenter.queryFromDBForView(mCid, mChatType);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOfflineMessage(Events.OffMessageEvent event) {
        long startId = 0;
        if(CollectionUtil.isNotEmpty(mImMessages)) {
            startId = mImMessages.get(mImMessages.size() - 1).getMsgId();
        }
        mChatPresenter.queryNewMessages(startId, mCid, mChatType);
    }

}
