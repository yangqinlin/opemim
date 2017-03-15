package com.shinemo.imdemo;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by yangqinlin on 17/2/27.
 * 基础Activity
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected abstract int getContentViewId();

    private View back;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        initBack();
        registEventBus();

        if (BuildConfig.DEBUG) {
            getClassName();
        }
    }

    private void initBack() {
        back = findViewById(R.id.back);
        if(back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private String getClassName(){
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Log.e("getClassName", "@@@@ActivityName:"+cn.getClassName());
        return cn.getClassName();
    }

    @Override
    public void showError(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegistEventBus();
    }

    protected void registEventBus(){
        //子类如果需要注册eventbus，则重写此方法
        //EventBus.getDefault().register(this);
    }

    protected void unRegistEventBus(){
        //子类如果需要注销eventbus，则重写此方法
        //EventBus.getDefault().unregister(this);
    }

    public void showToast(String content) {
        Toast toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToast(String content, Context context) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showLongToast(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        Toast toast = Toast.makeText(this, content, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showProgressDialog(boolean cancelable) {
        showProgressDialog(getString(R.string.loading), cancelable);
    }

    public void showProgressDialog() {
        showProgressDialog(getString(R.string.loading));
    }

    public void showProgressDialog(String info) {
        showProgressDialog(info, true);
    }

    public void showProgressDialog(String info, boolean cancelable) {
        showProgressDialog("", info, cancelable);
    }

    public void showProgressDialog(String title, String info,
                                   boolean cancelable) {
        if (!isFinishing()) {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(this, title, info, true,
                        cancelable, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                mProgressDialog.dismiss();
                            }
                        });
            } else {
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
            }

        }
    }

    public void hideProgressDialog() {
        if (!isFinished() && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected boolean isFinished() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return isDestroyed();
        }
        return isFinishing();
    }
}
