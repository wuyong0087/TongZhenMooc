package com.tzhen.mooc.activities;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.MyProfilePresenter;
import com.tongzhen.mooc.views.MyProfileView;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/12/25.
 */
@EActivity(R.layout.activity_my_profile)
public class MyProFileActivity extends BaseActivity<UserInfo> implements MyProfileView {
    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.iv_header)
    ImageView ivHeader;

    @ViewById(R.id.tv_name)
    TextView tvName;

    @ViewById(R.id.tv_nickname) TextView tvNickname;

    @ViewById(R.id.tv_email) TextView tvEmail;

    @ViewById(R.id.tv_country) TextView tvCountry;

    @ViewById(R.id.tv_city) TextView tvCity;

    @ViewById(R.id.tv_institute) TextView tvInstitute;

    @ViewById(R.id.tv_what_up) TextView tvWhatUp;

    @Inject
    MyProfilePresenter presenter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        initToolBar(toolbar, true);
        setToolbarTitle(getString(R.string.my_profile));

        presenter.attachView(this);
    }

    @Click({R.id.ll_header, R.id.ll_name, R.id.ll_nickname, R.id.ll_email, R.id.ll_country,
    R.id.ll_city,R.id.ll_institute, R.id.ll_what_up, R.id.ll_log_out})
    public void onViewsClick(View v){
        switch (v.getId()){
            case R.id.ll_header:

                break;
            case R.id.ll_name:

                break;
            case R.id.ll_nickname:

                break;
            case R.id.ll_email:

                break;
            case R.id.ll_country:

                break;
            case R.id.ll_city:

                break;
            case R.id.ll_institute:

                break;
            case R.id.ll_what_up:

                break;
            case R.id.ll_log_out:

                break;
        }
    }

    @Override
    public void onSuccess(UserInfo value) {
        if (ResultCodes.OK == value.getResult()){
            Glide.with(this).load(value.getHead()).into(ivHeader);

            tvName.setText(value.getName());
            tvNickname.setText(value.getNickname());
            tvEmail.setText(value.getEmail());

            tvCountry.setText(value.getCity());
            tvCity.setText(value.getCity());
            tvInstitute.setText(value.getSchool());

            tvWhatUp.setText(value.getDescription());
        } else{
            showMsg(value.getErrorMsg());
        }
    }
}
