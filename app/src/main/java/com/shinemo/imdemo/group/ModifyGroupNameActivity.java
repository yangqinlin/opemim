package com.shinemo.imdemo.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.shinemo.imdemo.BaseActivity;
import com.shinemo.imdemo.R;
import com.shinemo.imdemo.event.Events;
import com.shinemo.openim.ApiCallback;
import com.shinemo.openim.ImClient;
import com.shinemo.openim.utils.Handlers;
import com.shinemo.openim.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyGroupNameActivity extends BaseActivity {

    public static final String PARAM_GROUP_ID = "groupId";
    public static final String PARAM_NAME = "name";
    @BindView(R.id.group_name_text)
    EditText mGroupNameText;
    private long mGroupId;
    private String mName;

    public static void startAcitivity(Activity activity, long groupId, String name, int requestCode) {
        Intent intent = new Intent(activity, ModifyGroupNameActivity.class);
        intent.putExtra(PARAM_GROUP_ID, groupId);
        intent.putExtra(PARAM_NAME, name);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_modify_group_name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        mGroupId = getIntent().getLongExtra(PARAM_GROUP_ID, -1);
        mName = getIntent().getStringExtra(PARAM_NAME);
        mGroupNameText.setText(mName);
    }

    @OnClick(R.id.group_name_clear)
    public void clearName() {
        mGroupNameText.setText("");
    }

    @OnClick(R.id.group_name_save)
    public void saveName() {
        String name = mGroupNameText.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            ToastUtil.show(this, getString(R.string.group_name_not_null));
            return;
        }
        if(mName.equals(name)) return;

        modifyGroupName(name);
    }

    private void modifyGroupName(String name) {
        ImClient.getInstance().getGroupService().modifyGroupName(mGroupId, name, new ApiCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                EventBus.getDefault().post(new Events.ConversationUpdateEvent());
                EventBus.getDefault().post(new Events.GroupNameChangeEvent(name));
                Handlers.postMain(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra(PARAM_NAME, name);
                        ModifyGroupNameActivity.this.setResult(RESULT_OK, intent);
                        finish();
                    }
                });


            }

            @Override
            public void onFail(int code, String desc) {

            }
        });
    }
}
