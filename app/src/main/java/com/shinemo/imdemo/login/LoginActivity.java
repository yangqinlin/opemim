package com.shinemo.imdemo.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.shinemo.imdemo.BaseActivity;
import com.shinemo.imdemo.DefaultCallback;
import com.shinemo.imdemo.InitPresenter;
import com.shinemo.imdemo.R;
import com.shinemo.imdemo.main.MainActivity;
import com.shinemo.openim.ImClient;
import com.shinemo.widget.textview.PhoneEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.userHead)
    ImageView userHead;
    @BindView(R.id.tvPhone)
    PhoneEditText tvPhone;
    @BindView(R.id.clear_phone_number)
    ImageView clearPhoneNumber;
    @BindView(R.id.tvPasswd)
    EditText tvPasswd;
    @BindView(R.id.clear_password_content)
    ImageView clearPasswordContent;
    @BindView(R.id.ib_submit)
    Button ibSubmit;

    private InitPresenter mInitPresenter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        tvPhone.setOnFocusChangeListener(new MFocusLinster(tvPhone, clearPhoneNumber, getString(R.string.hint_plz_enter_phone_number)));
        tvPhone.addTextChangedListener(new MPhoneWatcher(clearPhoneNumber));
        tvPasswd.setOnFocusChangeListener(new MFocusLinster(tvPasswd, clearPasswordContent, getString(R.string.hint_plz_enter_passwd)));
        tvPasswd.addTextChangedListener(new MPhoneWatcher(clearPasswordContent));
        tvPasswd.setText("a12345678");
    }


    @OnClick({R.id.clear_phone_number, R.id.clear_password_content, R.id.ib_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_submit:
                login();
                break;
        }
    }

    private void login(){
        ImClient.getInstance().getLoginService().loginForTest(tvPhone.getPhoneNumber(), new DefaultCallback(this) {
            @Override
            protected void onDataSuccess(Object data) {
                mInitPresenter = new InitPresenter(LoginActivity.this);
                mInitPresenter.initData();
                mInitPresenter.syncConversations();
                MainActivity.startActivity(LoginActivity.this);
                finish();
            }
        });
    }

    class MFocusLinster implements View.OnFocusChangeListener {
        private EditText editText;
        private View clearBtn;
        private CharSequence hint;

        public MFocusLinster(EditText editText, View clearBtn, CharSequence hint){
            this.editText = editText;
            this.clearBtn = clearBtn;
            this.hint = hint;
            this.clearBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText("");
                }
            });
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus && editText.getText().toString().trim().length() > 0) {
                clearBtn.setVisibility(View.VISIBLE);
            } else {
                clearBtn.setVisibility(View.GONE);
            }
            if (hasFocus) {
                editText.setHint("");
            } else {
                editText.setHint(hint);
            }
        }
    };

    private void checkLoginBtnClickable() {
        int phoneLength = tvPhone.getPhoneNumber().length();
        int passwordLength = tvPasswd.getText().toString().trim().length();
        if (phoneLength > 10 && passwordLength > 0) {
            ibSubmit.setEnabled(true);
        } else {
            ibSubmit.setEnabled(false);
            ibSubmit.setTextColor(getResources().getColor(R.color.btn_enter_text));
        }
    }

    class MPhoneWatcher implements TextWatcher{
        private View clearBtn;

        public MPhoneWatcher(View clearBtn){
            this.clearBtn = clearBtn;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (charSequence.length() > 0) {
                clearBtn.setVisibility(View.VISIBLE);
            } else {
                clearBtn.setVisibility(View.GONE);
            }
            checkLoginBtnClickable();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
