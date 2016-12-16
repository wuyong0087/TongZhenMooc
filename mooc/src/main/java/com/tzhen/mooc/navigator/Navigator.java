package com.tzhen.mooc.navigator;

import android.content.Context;

import com.tzhen.mooc.activities.ForgotPwdActivity_;
import com.tzhen.mooc.activities.LaunchActivity;
import com.tzhen.mooc.activities.LoginActivity_;
import com.tzhen.mooc.activities.MainActivity_;
import com.tzhen.mooc.activities.SignUpActivity;
import com.tzhen.mooc.activities.SignUpActivity_;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/12/11.
 */
public class Navigator {
    @Inject
    public void Navigator() {
    }

    public void toForgotPwd(Context context) {
        ForgotPwdActivity_.intent(context).start();
    }

    public void toSignUp(Context context) {
        SignUpActivity_.intent(context).start();
    }

    public void toMain(Context context) {
        MainActivity_.intent(context).start();
    }

    public void toLogin(Context context) {
        LoginActivity_.intent(context).start();
    }
}
