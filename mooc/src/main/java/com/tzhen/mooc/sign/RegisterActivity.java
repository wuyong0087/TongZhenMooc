package com.tzhen.mooc.sign;

import com.tzhen.mooc.R;
import com.tzhen.mooc.commen.activity.BaseActivity;

import org.androidannotations.annotations.EActivity;

/**
 * Created by wuyong on 16/12/2.
 */
@EActivity(R.layout.activity_register_step_first)
public class RegisterActivity extends BaseActivity {

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
    }
}
