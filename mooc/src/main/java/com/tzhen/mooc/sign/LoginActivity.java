package com.tzhen.mooc.sign;

import android.view.View;
import android.widget.EditText;

import com.tzhen.mooc.R;
import com.tzhen.mooc.commen.activity.BaseActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wuyong on 2016/11/26.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.et_username)
    EditText etUsername;
    @ViewById(R.id.et_password)
    EditText etPassword;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
    }

    @Click({R.id.btn_sign_in, R.id.tv_sign_up, R.id.tv_reset_pwd})
    public void onViewsClick(View v){
        switch (v.getId()){
            case R.id.tv_sign_up:
                signUp();
                break;
            case R.id.btn_sign_in:
                signIn();
                break;
            case R.id.tv_reset_pwd:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {

    }

    private void signIn() {

    }

    private void signUp() {

    }
}
