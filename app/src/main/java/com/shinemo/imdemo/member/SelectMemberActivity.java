package com.shinemo.imdemo.member;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shinemo.contacts.data.local.DbContactsManager;
import com.shinemo.contacts.model.UserVo;
import com.shinemo.contacts.model.mapper.ContactsMapper;
import com.shinemo.imdemo.BaseActivity;
import com.shinemo.imdemo.R;
import com.shinemo.imdemo.chat.ChatActivity;
import com.shinemo.imdemo.member.adapter.SelectMemberAdapter;
import com.shinemo.openim.ApiCallback;
import com.shinemo.openim.ImClient;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.openim.service.group.model.GroupMember;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.openim.utils.CollectionUtil;
import com.shinemo.openim.utils.Handlers;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectMemberActivity extends BaseActivity{


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.list)
    RecyclerView list;

    private SelectMemberAdapter adapter;
    private int type;

    public static final int SELECT_MEMBER = 1002;
    public static final int CREATE_GROUP = 1;
    public static final String SELECT_DATA = "data";
    public static final String PARAM_TYPE = "type";

    public static void startActivity(Activity context) {
        Intent intent = new Intent(context, SelectMemberActivity.class);
        context.startActivityForResult(intent, SELECT_MEMBER);
    }

    public static void createGroup(Context context) {
        Intent intent = new Intent(context, SelectMemberActivity.class);
        intent.putExtra(PARAM_TYPE, CREATE_GROUP);
        context.startActivity(intent);
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_select_member;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        list.setLayoutManager(new LinearLayoutManager(this));
        List<Long> deptId = DbContactsManager.getInstance().queryUserDeptId(AccountHelper.getInstance().getUserId());
        List<UserVo> allMembers = null;
        if (!CollectionUtil.isEmpty(deptId)) {
            allMembers = ContactsMapper.INSTANCE.dbToUserVos(DbContactsManager.getInstance().queryUsersByDeptId(deptId.get(0)));
        }
        adapter = new SelectMemberAdapter(this, allMembers);
        list.setAdapter(adapter);

        type = getIntent().getIntExtra(PARAM_TYPE, -1);
        if(CREATE_GROUP == type) {
            adapter.selectMyself();
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.btn_confirm})
    public void confirm() {
        List<UserVo> userVos = adapter.getSelect();
        if(CollectionUtil.isEmpty(userVos)) return;

        if(type == CREATE_GROUP) {
            createGroup(userVos);
        } else {
            Intent intent = new Intent();
            intent.putExtra(SELECT_DATA, (Serializable)userVos);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private void createGroup(List<UserVo> userVos) {
        List<GroupMember> groupMebers = ContactsMapper.INSTANCE.userVoToGroupMembers(userVos);
        showProgressDialog(getString(R.string.create_group_loading));
        ImClient.getInstance().getGroupService().createGroup(getString(R.string.no_name_group), groupMebers, new ApiCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        ChatActivity.startActivity(SelectMemberActivity.this, getString(R.string.no_name_group), IMMessage.CHAT_TYPE_GROUP, String.valueOf(result));
                        finish();
                    }
                });
            }

            @Override
            public void onFail(int code, String desc) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                    }
                });
            }
        });
    }

}
