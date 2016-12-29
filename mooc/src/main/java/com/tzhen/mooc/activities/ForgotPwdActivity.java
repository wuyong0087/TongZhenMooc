package com.tzhen.mooc.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.mooc.R;
import com.tzhen.mooc.fragments.SendCodeFragment_;
import com.tzhen.mooc.fragments.SetNewPwdFragment_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wuyong on 16/12/16.
 */
@EActivity(R.layout.activity_forgot_pwd)
public class ForgotPwdActivity extends BaseActivity<BaseInfo> {

    public static final int SEND_CODE = 0;
    public static final int RESET_PWD = 1;

    @ViewById(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        initToolBar(toolbar, true);
        setToolbarTitle(getString(R.string.forgot_pwd));
    }

    public void attachFragment(int type, String... username){
        Fragment frag = null;
        switch (type){
            case SEND_CODE:
                frag = SendCodeFragment_.builder().build();
                break;
            case RESET_PWD:
                frag = SetNewPwdFragment_.builder().username(username[0]).build();
                break;
        }

        if (frag != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl_container, frag).commit();
        }
    }
}
