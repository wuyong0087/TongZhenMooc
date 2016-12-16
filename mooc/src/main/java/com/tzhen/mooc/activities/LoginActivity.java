package com.tzhen.mooc.activities;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.LoginPresenter;
import com.tongzhen.mooc.views.LoginView;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.commen.utils.StringUtils;
import com.tzhen.mooc.R;
import com.tzhen.mooc.navigator.Navigator;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/11/26.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity<UserInfo> implements LoginView {

    @ViewById(R.id.et_username) EditText etUsername;

    @ViewById(R.id.et_password) EditText etPassword;

    @ViewById(R.id.tv_copyright)
    TextView tvCopyRight;

    @Inject
    Navigator navigator;

    @Inject
    LoginPresenter presenter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        setupCopyRight();
    }

    private void setupCopyRight() {
        Calendar c = Calendar.getInstance();
        tvCopyRight.setText(getString(R.string.copy_right, c.get(Calendar.YEAR)));
    }

    @Click({R.id.btn_next, R.id.tv_sign_up, R.id.tv_reset_pwd})
    public void onViewsClick(View v){
        switch (v.getId()){
            case R.id.tv_sign_up:
                signUp();
                break;
            case R.id.btn_next:
                signIn();
                break;
            case R.id.tv_reset_pwd:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        navigator.toForgotPwd(this);
    }

    private void signIn() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (!StringUtils.isValidUsername(username)){
            showMsg(getString(R.string.invalid_username));
            return;
        }

        if (!StringUtils.isValidPwd(password)){
            showMsg(getString(R.string.invalid_password));
            return;
        }

        presenter.attachView(this, username, password);
    }

    private void signUp() {
        navigator.toSignUp(this);
    }

    @Override
    public void onSuccess(UserInfo value) {
        if (ResultCodes.OK == value.getResult()){
            navigator.toMain(this);
        } else{
            showMsg(value.getErrorMsg());
        }
    }
}
