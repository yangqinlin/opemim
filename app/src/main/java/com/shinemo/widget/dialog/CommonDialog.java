package com.shinemo.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shinemo.imdemo.R;


public class CommonDialog extends Dialog implements View.OnClickListener {

    protected TextView mCancelBtn;
    protected TextView mConfirmBtn;
    protected View mPopupBg;
    protected TextView mTitleView;
    protected TextView mDescView;
    private ViewGroup mContentView;
    private View mUserView;
    private String mTitle;
    private String mContent;
    private onConfirmListener mListener;
    private OnCancelListener mCancelListener;
    private OnCancelListener mBglistener;
    private LinearLayout mContainer;
    private int mWidth;
    private String mConfirmText = null;
    private String mCancelText;
    private boolean dismissFlag = false;
    private boolean noCancel;

    public void setDismissFlag(boolean dismissFlag) {
        this.dismissFlag = dismissFlag;
    }

    public interface onConfirmListener {
        void onConfirm();
    }

    public interface OnCancelListener {
        public void onCancel();
    }

    public CommonDialog(Context context) {
        super(context, R.style.share_dialog);
    }

    public CommonDialog(Context context, onConfirmListener click) {
        super(context, R.style.share_dialog);
        mListener = click;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    public void setView(View view) {
        mUserView = view;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }


    public void setConfirmText(String text) {
        mConfirmText = text;
    }

    public void setCancelText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mCancelText = text;
        }
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitle = title;
        }
    }

    public void setTitle(String title, String content) {
        if (!TextUtils.isEmpty(title)) {
            mTitle = title;
        }
        if (!TextUtils.isEmpty(content)) {
            mContent = content;
        }
    }

    public void setNoCancel() {
        noCancel = true;
    }

    public void setCancelListener(OnCancelListener listener) {
        mCancelListener = listener;
    }

    public void setConfirmListener(onConfirmListener listener){
        mListener = listener;
    }

    public void setBgListener(OnCancelListener listener) {
        mBglistener = listener;
    }


    protected void initView() {
        setContentView(R.layout.common_dialog);
        mPopupBg = findViewById(R.id.dialog_bg);
        mPopupBg.setOnClickListener(this);
        mCancelBtn = (TextView) findViewById(R.id.dialog_cancel);
        mCancelBtn.setOnClickListener(this);
        mConfirmBtn = (TextView) findViewById(R.id.dialog_confirm);
        mConfirmBtn.setOnClickListener(this);
        mTitleView = (TextView) findViewById(R.id.dialog_title);
        mDescView = (TextView) findViewById(R.id.dialog_desc);
        mContainer = (LinearLayout) findViewById(R.id.dialogContainer);
        mContainer.setOnClickListener(this);
        if (mWidth != 0) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mContainer.setLayoutParams(params);
        }
        if(noCancel){
            mCancelBtn.setVisibility(View.GONE);
        }
        if (mConfirmText != null) {
            mConfirmBtn.setText(mConfirmText);
            if (TextUtils.isEmpty(mConfirmText)) {
                mConfirmBtn.setEnabled(false);
            }
        }
        if (!TextUtils.isEmpty(mCancelText)) {
            mCancelBtn.setText(mCancelText);
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTitleView.setVisibility(View.GONE);
        } else {
            mTitleView.setText(mTitle);
        }
        if (!TextUtils.isEmpty(mContent)) {
            mDescView.setVisibility(View.VISIBLE);
            mDescView.setText(mContent);
        }
        mContentView = (ViewGroup) findViewById(R.id.dialog_content);
        if (mUserView != null) {
            mContentView.addView(mUserView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                dismiss();
                if (mCancelListener != null) {
                    mCancelListener.onCancel();
                }
                break;
            case R.id.dialog_bg:
                dismiss();
                if (mBglistener != null) {
                    mBglistener.onCancel();
                } else {
                    if (mCancelListener != null) {
                        mCancelListener.onCancel();
                    }
                }
                break;
            case R.id.dialog_confirm:
                if (!dismissFlag) {
                    dismiss();
                }
                if (mListener != null) {
                    mListener.onConfirm();
                }
                break;
            default:
                break;
        }
    }
}
