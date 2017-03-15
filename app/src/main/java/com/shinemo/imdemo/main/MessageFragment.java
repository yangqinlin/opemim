package com.shinemo.imdemo.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shinemo.imdemo.BaseFragment;
import com.shinemo.imdemo.R;
import com.shinemo.imdemo.chat.ChatActivity;
import com.shinemo.imdemo.event.Events;
import com.shinemo.openim.ApiCallback;
import com.shinemo.openim.ImClient;
import com.shinemo.openim.service.msg.model.Conversation;
import com.shinemo.openim.utils.Handlers;
import com.shinemo.widget.rvhelper.adapter.CommonAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangqinlin on 17/2/27.
 * 消息tab
 */
public class MessageFragment extends BaseFragment implements CommonAdapter.OnItemClickListener<Conversation>{

    public static final int REQUEST_CHAT = 1001;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rv_contacts)
    RecyclerView mRvContacts;

    private ConversationAdapter mAdapter;
    private List<Conversation> mConversations = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_message, null);
        ButterKnife.bind(this, view);

        mTvTitle.setText(R.string.tab_message);
        mRvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ConversationAdapter(getActivity(), R.layout.conversation_item, mConversations);
        mRvContacts.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        queryConversationsFromDB();

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConversationUpdate(Events.ConversationUpdateEvent event) {
        queryConversationsFromDB();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOfflineMessage(Events.OffMessageEvent event) {
        queryConversationsFromDB();
    }

    private void notifyUpdate(final List<Conversation> conversations) {
        Handlers.postMain(new Runnable() {
            @Override
            public void run() {
                mConversations.clear();
                mConversations.addAll(conversations);
                if(mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Conversation o, int position) {
        markAsRead(o.getCid(), o.getChatType());
        ChatActivity.startActivityForResult(this, o.getDisplayName(), o.getChatType(), o.getCid(), REQUEST_CHAT);
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Conversation o, int position) {
        String[] items = {"删除"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ImClient.getInstance().getMsgService().deleteConversation(o.getCid(), o.getChatType(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        Handlers.postMain(new Runnable() {
                            @Override
                            public void run() {
                                mConversations.remove(o);
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    }

                    @Override
                    public void onFail(int code, String desc) {

                    }
                });
                dialog.dismiss();
            }
        });
        alertDialog.show();

        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHAT:
                    queryConversationsFromDB();
                    break;
            }
        }

    }

    private void queryConversationsFromDB() {
        ImClient.getInstance().getMsgService().queryAllConversationsFromDB(new ApiCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> result) {
                notifyUpdate(result);
            }

            @Override
            public void onFail(int code, String desc) {

            }
        });
    }

    private void markAsRead(String account, int chatType) {
        ImClient.getInstance().getMsgService().markAsRead(account, chatType, new ApiCallback() {
            @Override
            public void onSuccess(Object result) {
            }

            @Override
            public void onFail(int code, String desc) {
            }
        });
    }


}
