package com.shinemo.imdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.shinemo.imdemo.login.LoginActivity;
import com.shinemo.openim.ApiCallback;
import com.shinemo.openim.utils.ToastUtil;


public abstract class DefaultCallback<T> implements ApiCallback<T> {

	protected Context mContext;
	public boolean isCancel;

	public static final int CODE_LOGOUT = 100;

	public DefaultCallback(Context context) {
		mContext = context;
	}

	public DefaultCallback(Context context, boolean isCancel) {
		mContext = context;
		this.isCancel = isCancel;
	}

	@Override
	public void onSuccess(T data) {
		if (mContext != null) {
			if (mContext instanceof Activity) {
				Activity activity = (Activity) mContext;
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
					 if(activity.isDestroyed()){
						 return;
					 }
				}
				if(activity.isFinishing()){
					return;
				}
			}
		}
		onDataSuccess(data);
	}

	@Override
	public void onFail(int code, String reason) {
		if (mContext != null && mContext instanceof Activity) {
			if (((Activity) mContext).isFinishing()) {
				return;
			}
		}
		if(code == CODE_LOGOUT){
			LoginActivity.startActivity(mContext);
			return;
		}
		if (!TextUtils.isEmpty(reason)) {
			ToastUtil.show(mContext, reason);
		}
	}

	protected abstract void onDataSuccess(T data);
}
