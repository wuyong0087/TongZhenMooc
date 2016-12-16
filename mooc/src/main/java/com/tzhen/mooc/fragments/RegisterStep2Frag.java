package com.tzhen.mooc.fragments;

import android.widget.EditText;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;
import com.tzhen.mooc.activities.SignUpActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wuyong on 16/12/2.
 */
@EFragment(R.layout.fragment_register_step_second)
public class RegisterStep2Frag extends BaseFragment<BaseInfo> {

    @ViewById(R.id.et_username) EditText etUsername;

    @ViewById(R.id.et_password) EditText etPassword;

    @ViewById(R.id.et_confirm_password) EditText etConfirmPassword;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
    }

    @Click(R.id.btn_next)
    public void signUp(){
        ((SignUpActivity)getActivity()).attachFragment(SignUpActivity.STEP_2);
    }
}
