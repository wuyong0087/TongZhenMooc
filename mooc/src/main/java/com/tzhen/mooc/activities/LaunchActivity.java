package com.tzhen.mooc.activities;

import android.text.TextUtils;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.mooc.R;
import com.tzhen.mooc.navigator.Navigator;
import com.tzhen.mooc.storage.Persistence;

import org.androidannotations.annotations.EActivity;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/16.
 */
@EActivity(R.layout.activity_launch)
public class LaunchActivity extends BaseActivity<BaseInfo> {

    @Inject
    Persistence persistence;

    @Inject
    Navigator navigator;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        setupLogin();
    }

    private void setupLogin() {
        String uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);
        if (TextUtils.isEmpty(uid)) {
            navigator.toLogin(this);
        } else {
            navigator.toMain(this);
        }
    }
}
