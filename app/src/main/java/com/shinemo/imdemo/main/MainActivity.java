package com.shinemo.imdemo.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.shinemo.imdemo.BaseActivity;
import com.shinemo.imdemo.R;
import com.shinemo.widget.bottombar.BottomBar;
import com.shinemo.widget.fragnav.FragNavController;

public class MainActivity extends BaseActivity implements FragNavController.RootFragmentListener {

    private final int INDEX_MESSAGE = FragNavController.TAB1;
    private final int INDEX_CONTACTS = FragNavController.TAB2;
    private final int INDEX_TEST = FragNavController.TAB3;

    BottomBar mBottomBar;
    private FragNavController mNavController;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mNavController = new FragNavController(savedInstanceState, getSupportFragmentManager(), R.id.container, this, 3, INDEX_MESSAGE);

        mBottomBar.setOnTabSelectListener(new BottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_message:
                        mNavController.switchTab(INDEX_MESSAGE);
                        break;
                    case R.id.tab_contacts:
                        mNavController.switchTab(INDEX_CONTACTS);
                        break;
                    case R.id.tab_setting:
                        mNavController.switchTab(INDEX_TEST);
                        break;
                }
            }
        });
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case INDEX_MESSAGE:
                return new MessageFragment();
            case INDEX_CONTACTS:
                return new ContactsFragment();
            case INDEX_TEST:
                return new SettingFragment();
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
