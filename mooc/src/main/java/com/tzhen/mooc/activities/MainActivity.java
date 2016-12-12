package com.tzhen.mooc.activities;

import android.content.Context;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.commen.tabs.TabEntity;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity<BaseInfo> {


    @ViewById(R.id.tl_1) CommonTabLayout mTabLayout_1;

    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    private int[] mIconUnselectIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private int[] mIconSelectIds = {
            R.drawable.logo, R.drawable.logo,
            R.drawable.logo, R.drawable.logo};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mTabLayout_1.setTabData(mTabEntities);

        //显示未读红点
        mTabLayout_1.showDot(2);
    }

}
