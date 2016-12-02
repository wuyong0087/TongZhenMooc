package com.tzhen.mooc.sign;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tzhen.mooc.R;

/**
 * Created by wuyong on 16/12/2.
 */
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(R.layout.activity_register_step_first);
    }
}
