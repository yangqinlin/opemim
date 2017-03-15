package com.shinemo.imdemo;

import com.shinemo.contacts.data.ContactsManager;
import com.shinemo.imdemo.event.Events;
import com.shinemo.openim.ApiCallback;
import com.shinemo.openim.ImClient;
import com.shinemo.openim.service.group.GroupService;
import com.shinemo.openim.service.group.model.Group;
import com.shinemo.openim.service.msg.model.Conversation;
import com.shinemo.openim.utils.CollectionUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * Created by yangqinlin on 17/2/27.
 */

public class InitPresenter extends BasePresenter<BaseView> {

    private GroupService mGroupService;
    private ContactsManager mContactsManager;

    public InitPresenter(BaseView view) {
        super(view);
        mGroupService = ImClient.getInstance().getGroupService();
        mContactsManager = ContactsManager.getInstance();
    }

    public void initData() {
        mGroupService.queryMyGroups(new ApiCallback<List<Group>>() {
            @Override
            public void onSuccess(List<Group> result) {
            }
            @Override
            public void onFail(int code, String desc) {
            }
        });
        mContactsManager.syncContacts();
    }

    public void syncConversations() {
        ImClient.getInstance().getMsgService().queryAllConversationsFromNet(new ApiCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> result) {
                if (CollectionUtil.isNotEmpty(result)) {
                    EventBus.getDefault().post(new Events.ConversationUpdateEvent());
                }
            }

            @Override
            public void onFail(int code, String desc) {

            }
        });
    }
}
