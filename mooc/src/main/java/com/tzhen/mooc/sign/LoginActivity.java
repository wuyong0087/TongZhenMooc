package com.tzhen.mooc.sign;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tzhen.mooc.R;
import com.tzhen.mooc.commen.activity.BaseActivity;

/**
 * Created by wuyong on 2016/11/26.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(R.layout.activity_login);
    }
}
