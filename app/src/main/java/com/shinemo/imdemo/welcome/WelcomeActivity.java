package com.shinemo.imdemo.welcome;

import android.os.Bundle;

import com.shinemo.MyService;
import com.shinemo.imdemo.BaseActivity;
import com.shinemo.imdemo.InitPresenter;
import com.shinemo.imdemo.R;
import com.shinemo.imdemo.login.LoginActivity;
import com.shinemo.imdemo.main.MainActivity;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.openim.utils.Handlers;

public class WelcomeActivity extends BaseActivity {

    private static final int DELAY_TIME = 1000;

    private InitPresenter mPresenter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new InitPresenter(this);
        if(!isLogined()) {
            goToLogin();
        } else {
            goToMain();
            if(MyService.isLogin) {
                mPresenter.initData();
            } else {
                delayInit();
            }
        }
    }

    private void delayInit() {
        Handlers.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(MyService.isLogin) {
                    mPresenter.initData();
                }
            }
        }, 500);
    }

    private void goToMain() {
        Handlers.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.startActivity(WelcomeActivity.this);
                finish();
            }
        }, DELAY_TIME);
    }

    private void goToLogin() {
        Handlers.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginActivity.startActivity(WelcomeActivity.this);
                finish();
            }
        }, DELAY_TIME);
    }

    private boolean isLogined() {
        return AccountHelper.getInstance().getAccount() != null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
