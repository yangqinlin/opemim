package com.shinemo.imdemo.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shinemo.imdemo.BaseActivity;
import com.shinemo.imdemo.R;
import com.shinemo.imdemo.chat.ChatActivity;
import com.shinemo.imdemo.member.SelectMemberActivity;
import com.shinemo.openim.ApiCallback;
import com.shinemo.openim.ImClient;
import com.shinemo.openim.service.group.model.Group;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.openim.utils.CollectionUtil;
import com.shinemo.openim.utils.Handlers;
import com.shinemo.widget.rvhelper.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyGroupActivity extends BaseActivity {

    @BindView(R.id.rv_my_group)
    RecyclerView mRvMyGroup;

    private MyGroupAdapter mAdapter;
    private List<Group> mGroupList = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyGroupActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_group;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        mRvMyGroup.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyGroupAdapter(this, R.layout.simple_item, mGroupList);
        mRvMyGroup.setAdapter(mAdapter);
        queryMyGroups();

        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener<Group>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Group group, int position) {
                ChatActivity.startActivity(view.getContext(), group.getGroupName(), IMMessage.CHAT_TYPE_GROUP, String.valueOf(group.getGroupId()));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Group group, int position) {
                return false;
            }
        });

    }


    @OnClick(R.id.group_add)
    public void createGroup() {
        SelectMemberActivity.createGroup(this);
    }

    private void queryMyGroups() {
        ImClient.getInstance().getGroupService().queryMyGroupsFromDB(new ApiCallback<List<Group>>() {
            @Override
            public void onSuccess(List<Group> result) {
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        mGroupList.clear();
                        if(CollectionUtil.isNotEmpty(result)) {
                            mGroupList.addAll(result);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFail(int code, String desc) {

            }
        });
    }

}
