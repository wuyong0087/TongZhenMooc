package com.tzhen.mooc.navigator;

import android.content.Context;

import com.tzhen.mooc.activities.ChatActivity_;
import com.tzhen.mooc.activities.CourseCenterActivity_;
import com.tzhen.mooc.activities.CourseInfoActivity_;
import com.tzhen.mooc.activities.ForgotPwdActivity_;
import com.tzhen.mooc.activities.LoginActivity_;
import com.tzhen.mooc.activities.MainActivity_;
import com.tzhen.mooc.activities.SignUpActivity_;
import com.tzhen.mooc.activities.UserInfoActivity_;
import com.tzhen.mooc.activities.WorksInfoActivity_;
import com.tzhen.mooc.countrylist.CountryListActivity_;

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

    public void toWorksInfo(Context context, int vid) {
        WorksInfoActivity_.intent(context).vid(vid).start();
    }

    public void toUserInfo(Context context, String oid) {
        UserInfoActivity_.intent(context).oid(oid).start();
    }

    public void toChat(Context context, String oid) {
        ChatActivity_.intent(context).oid(oid).start();
    }

    public void toCourseInfo(Context context, int cid) {
        CourseInfoActivity_.intent(context).cid(cid).start();
    }

    public void toCourseCenter(Context context) {
        CourseCenterActivity_.intent(context).start();
    }

    public void toCountryList(Context context) {
        CountryListActivity_.intent(context).start();
    }
}
