package com.shinemo.imdemo.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shinemo.contacts.model.UserVo;
import com.shinemo.contacts.model.mapper.ContactsMapper;
import com.shinemo.imdemo.BaseActivity;
import com.shinemo.imdemo.R;
import com.shinemo.imdemo.member.SelectMemberActivity;
import com.shinemo.openim.ApiCallback;
import com.shinemo.openim.ImClient;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.openim.service.group.model.Group;
import com.shinemo.openim.service.group.model.GroupMember;
import com.shinemo.openim.utils.CollectionUtil;
import com.shinemo.openim.utils.Handlers;
import com.shinemo.openim.utils.ToastUtil;
import com.shinemo.widget.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class GroupInfoActivity extends BaseActivity {

    public static final String PARAM_CID = "cid";
    public static final int REQUEST_CODE_MODIFY_NAME = 1001;
    @BindView(R.id.tv_member_count)
    TextView mTvMemberCount;
    @BindView(R.id.tv_group_name)
    TextView mTvGroupName;
    @BindView(R.id.msg_not_notify)
    SwitchButton mMsgNotNotify;
    @BindView(R.id.tv_quit_group)
    TextView mTvQuitGroup;
    @BindView(R.id.avatar_layout)
    LinearLayout avatarLayout;
    @BindView(R.id.add_new_member)
    ImageView addMember;

    private long groupId;
    private String groupName;
    private List<GroupMember> groupMembers;
    private boolean isCreated = false;

    public static void startActivity(Context context, String cid) {
        Intent intent = new Intent(context, GroupInfoActivity.class);
        intent.putExtra(PARAM_CID, cid);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_group_info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        groupId = Long.valueOf(getIntent().getStringExtra(PARAM_CID));
        getGroupInfo(groupId);
    }

    @OnCheckedChanged(R.id.msg_not_notify)
    public void clickMute(CompoundButton buttonView, boolean isChecked) {
        ImClient.getInstance().getGroupService().setGroupMute(groupId, isChecked, new ApiCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        mMsgNotNotify.setChecked(isChecked);
                    }
                });
            }

            @Override
            public void onFail(int code, String desc) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        mMsgNotNotify.setChecked(!isChecked);
                    }
                });
            }
        });
    }

    @OnClick(R.id.tv_group_name)
    public void clickGroupName() {
        ModifyGroupNameActivity.startAcitivity(this, groupId, groupName, REQUEST_CODE_MODIFY_NAME);

    }

    @OnClick(R.id.member_title)
    public void goToMemberList() {
        if(CollectionUtil.isEmpty(groupMembers)) {
            return;
        }
        ChatMemberActivity.startActivity(this, groupId, isCreated, new ArrayList<>(groupMembers));
    }

    @OnClick(R.id.add_new_member)
    public void addClick() {
        SelectMemberActivity.startActivity(this);
    }

    @OnClick(R.id.tv_quit_group)
    public void quitGroup() {
        showProgressDialog();
        if(isCreated) {
            ImClient.getInstance().getGroupService().destroyGroup(groupId, new ApiCallback<Long>() {
                @Override
                public void onSuccess(Long result) {
                    hideProgressDialog();
                    finish();
                }

                @Override
                public void onFail(int code, String desc) {
                    hideProgressDialog();
                }
            });
        } else {
            ImClient.getInstance().getGroupService().quitGroup(groupId, new ApiCallback<Long>() {
                @Override
                public void onSuccess(Long result) {
                    hideProgressDialog();
                    finish();
                }

                @Override
                public void onFail(int code, String desc) {
                    hideProgressDialog();
                }
            });
        }
    }

    private void getGroupInfo(long groupId) {
        ImClient.getInstance().getGroupService().getGroupInfo(groupId, new ApiCallback<Group>() {
            @Override
            public void onSuccess(Group result) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        mTvMemberCount.setText(getString(R.string.group_members, result.getMemberCount()));
                        groupName = result.getGroupName();
                        mTvGroupName.setText(groupName);
                        groupMembers = result.getGroupMemberList();
                        buildMemberView(groupMembers);
                        isCreated = AccountHelper.getInstance().getUserId().equals(result.getCreatorId());
                        if (isCreated) {
                            mTvQuitGroup.setText(R.string.remove_group);
                        } else {
                            mTvQuitGroup.setText(R.string.quit_group);
                        }
                        mMsgNotNotify.setChecked(result.isMute());
                    }
                });
            }

            @Override
            public void onFail(int code, String desc) {

            }
        });
    }

    private void addMember(List<GroupMember> members){
        if (CollectionUtil.isEmpty(members)) {
            return;
        }
        ImClient.getInstance().getGroupService().addMembers(groupId, members, new ApiCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(GroupInfoActivity.this, "添加成功");
                        groupMembers.addAll(members);
                        buildMemberView(groupMembers);
                    }
                });
            }

            @Override
            public void onFail(int code, String desc) {
                ToastUtil.show(GroupInfoActivity.this, desc);
            }
        });
    }

    private void buildMemberView(List<GroupMember> groupMembers){
        if (CollectionUtil.isEmpty(groupMembers)) {

        } else {
            avatarLayout.removeAllViews();
            for (int i = 0; (i < groupMembers.size() && i < 4); i++) {
                View avatarItem = LayoutInflater.from(this).inflate(R.layout.avatar_item, avatarLayout, false);
                ImageView header = (ImageView)avatarItem.findViewById(R.id.user_avatar);
                TextView name = (TextView)avatarItem.findViewById(R.id.user_name);
                header.setImageResource(R.drawable.avatar_def);
                name.setText(groupMembers.get(i).getNick());
                avatarLayout.addView(avatarItem);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_MODIFY_NAME:
                    groupName = data.getStringExtra(ModifyGroupNameActivity.PARAM_NAME);
                    mTvGroupName.setText(groupName);
                    break;
                case SelectMemberActivity.SELECT_MEMBER:
                    ArrayList<UserVo> members = (ArrayList<UserVo>)data.getSerializableExtra(SelectMemberActivity.SELECT_DATA);
                    addMember(ContactsMapper.INSTANCE.userVoToGroupMembers(members));
                    break;
                case ChatMemberActivity.EDIT_MEMBER:
                    groupMembers = (ArrayList<GroupMember>)data.getSerializableExtra(ChatMemberActivity.MEMBER_DATA);
                    buildMemberView(groupMembers);
                    break;
            }
        }
    }
}
