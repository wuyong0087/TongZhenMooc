package com.tzhen.mooc.main;

import android.view.View;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by wuyong on 16/12/17.
 */
@EActivity(R.layout.activity_create_success)
public class CreateSuccessActivity extends BaseActivity<BaseInfo> {

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

    }

    @Click({R.id.tv_click_to_view, R.id.tv_share_to_course, R.id.tv_create_new,
            R.id.ll_wechat, R.id.ll_moments, R.id.ll_qq, R.id.ll_weibo, R.id.ll_email})
    public void onViewsClick(View v){
        switch (v.getId()){
            case R.id.tv_click_to_view:

                break;
            case R.id.tv_share_to_course:

                break;
            case R.id.tv_create_new:

                break;
            case R.id.ll_wechat:

                break;
            case R.id.ll_moments:

                break;
            case R.id.ll_qq:

                break;
            case R.id.ll_weibo:

                break;
            case R.id.ll_email:

                break;
        }
    }
}
