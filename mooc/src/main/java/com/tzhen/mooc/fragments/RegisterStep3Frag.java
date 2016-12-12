package com.tzhen.mooc.fragments;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

/**
 * Created by wuyong on 16/12/12.
 */
@EFragment(R.layout.fragment_register_step_three)
public class RegisterStep3Frag extends BaseFragment<BaseInfo> {

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Click(R.id.btn_sign_in)
    public void signIn(){

    }
}
