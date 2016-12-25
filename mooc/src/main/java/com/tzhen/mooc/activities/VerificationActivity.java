package com.tzhen.mooc.activities;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wuyong on 2016/12/26.
 */
@EActivity(R.layout.activity_verification)
public class VerificationActivity extends BaseActivity<BaseInfo> {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.tv_title)
    TextView tvTitle;

    @ViewById(R.id.tv_institute) TextView tvInstitute;

    @ViewById(R.id.tv_country)
    TextView tvCountry;

    @ViewById(R.id.tv_city)
    TextView tvCity;

    @ViewById(R.id.tv_address)
    TextView tvAddress;

    @ViewById(R.id.tv_email)
    TextView tvEmail;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        initToolBar(toolbar, true);
        setToolbarTitle(getString(R.string.open_new_course));
    }

    @Click({R.id.ll_title, R.id.ll_name, R.id.ll_institute, R.id.ll_country, R.id.ll_city,
    R.id.ll_address, R.id.ll_email, R.id.ll_verify_image, R.id.btn_submit})
    public void onViewsClick(View v){
        switch (v.getId()){
            case R.id.ll_title:

                break;
            case R.id.ll_name:

                break;
            case R.id.ll_institute:

                break;
            case R.id.ll_country:

                break;
            case R.id.ll_city:

                break;
            case R.id.ll_address:

                break;
            case R.id.ll_email:

                break;
            case R.id.ll_verify_image:

                break;
            case R.id.btn_submit:

                break;
        }
    }
}
