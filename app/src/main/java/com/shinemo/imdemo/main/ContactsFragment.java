package com.shinemo.imdemo.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shinemo.imdemo.BaseFragment;
import com.shinemo.imdemo.R;
import com.shinemo.imdemo.chat.ChatActivity;
import com.shinemo.imdemo.group.MyGroupActivity;
import com.shinemo.imdemo.group.User;
import com.shinemo.openim.service.msg.model.IMMessage;
import com.shinemo.widget.rvhelper.adapter.CommonAdapter;
import com.shinemo.widget.rvhelper.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangqinlin on 17/2/27.
 */

public class ContactsFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl_group)
    RelativeLayout mRlGroup;
    @BindView(R.id.rv_contacts)
    RecyclerView mRvContacts;

    public static List<User> mockUsers = new ArrayList<>();
    static {
        //270104
        User user1 = new User("101010011036392", "严鑫伟aa", "18112250072");
        User user2 = new User("270168", "闫逍旭", "13600530623");
        User user3 = new User("270152", "吴志勇", "13634112388");
        User user4 = new User("270056", "应远日", "18958051770");
        User user5 = new User("80920", "潘杰军", "18267919771");
        User user6 = new User("269824", "吴耀波", "15867136354");
        mockUsers.add(user1);
        mockUsers.add(user2);
        mockUsers.add(user3);
        mockUsers.add(user4);
        mockUsers.add(user5);
        mockUsers.add(user6);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contacts, null);
        ButterKnife.bind(this, view);

        mTvTitle.setText(R.string.tab_contacts);
        mRvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvContacts.setAdapter(new CommonAdapter<User>(getActivity(), R.layout.simple_item, mockUsers) {

            @Override
            protected void convert(ViewHolder holder, User user) {
                holder.setText(R.id.tv_title, user.name);
                holder.setOnClickListener(R.id.root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChatActivity.startActivity(getActivity(), user.name, IMMessage.CHAT_TYPE_P2P, user.account);
                    }
                });
            }
        });

        return view;
    }

    @OnClick(R.id.rl_group)
    public void clickGroup() {
        MyGroupActivity.startActivity(getActivity());
    }
}
