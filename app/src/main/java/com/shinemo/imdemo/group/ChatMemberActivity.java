package com.shinemo.imdemo.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shinemo.imdemo.BaseActivity;
import com.shinemo.imdemo.R;
import com.shinemo.openim.ApiCallback;
import com.shinemo.openim.ImClient;
import com.shinemo.openim.service.group.model.GroupMember;
import com.shinemo.openim.utils.Handlers;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatMemberActivity extends BaseActivity implements GroupMemberAdapter.MoreAction {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.group_member_edit)
    TextView editBtn;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.list)
    RecyclerView list;

    private GroupMemberAdapter adapter;
    private ArrayList<GroupMember> groupMembers;
    private long mCid;
    private boolean isCreate;
    public static final int EDIT_MEMBER = 1003;
    public static final String MEMBER_DATA = "data";

    public static void startActivity(Activity context, long cid, boolean isCreate, ArrayList<GroupMember> list) {
        Intent intent = new Intent(context, ChatMemberActivity.class);
        intent.putExtra("cid", cid);
        intent.putExtra("list", list);
        intent.putExtra("isCreate", isCreate);
        context.startActivityForResult(intent, EDIT_MEMBER);
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_group_member;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mCid = getIntent().getLongExtra("cid", -1);
        isCreate = getIntent().getBooleanExtra("isCreate", false);
        groupMembers = (ArrayList<GroupMember>)getIntent().getSerializableExtra("list");


        if (isCreate) {
            editBtn.setVisibility(View.VISIBLE);
        } else {
            editBtn.setVisibility(View.GONE);
        }
        list.setLayoutManager(new GridLayoutManager(this, 5));
        adapter = new GroupMemberAdapter(this, groupMembers);
        list.setAdapter(adapter);

        title.setText(getString(R.string.group_members, adapter.getItemCount()));
    }

    @OnClick({R.id.group_member_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_member_edit:
                adapter.setEdit(!adapter.idEdit());
                if (adapter.idEdit()) {
                    editBtn.setText(R.string.complete);
                } else {
                    editBtn.setText(R.string.edit);
                }
                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(MEMBER_DATA, adapter.getGroupMembers());
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void delMember(GroupMember groupMember) {
        ImClient.getInstance().getGroupService().kickoutMember(mCid, groupMember.getAccount(), new ApiCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        adapter.rmMember(groupMember);
                        title.setText(getString(R.string.group_members, adapter.getItemCount()));
                    }
                });
            }

            @Override
            public void onFail(int code, String desc) {

            }
        });
    }

}
