package com.tzhen.mooc.main;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.EFragment;

/**
 * Created by wuyong on 16/12/13.
 */
@EFragment(R.layout.fragment_me)
public class MeFragment extends BaseFragment<BaseInfo> {

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
