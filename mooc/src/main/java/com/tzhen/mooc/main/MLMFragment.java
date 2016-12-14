package com.tzhen.mooc.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.adapters.MLMAdapter;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wuyong on 16/12/13.
 */
@EFragment(R.layout.fragment_mlm)
public class MLMFragment extends BaseFragment<BaseInfo> implements TabLayout.OnTabSelectedListener {
    @ViewById(R.id.tab_top)
    TabLayout tabTop;

    @ViewById(R.id.vp_container)
    ViewPager vpContainer;

    private MLMAdapter mlmAdapter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        initTabs();

    }

    private void initTabs() {
        mlmAdapter = new MLMAdapter(getFragmentManager(), getContext());
        vpContainer.setAdapter(mlmAdapter);
        vpContainer.setOffscreenPageLimit(2);

        tabTop.setupWithViewPager(vpContainer);
        tabTop.setOnTabSelectedListener(this);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
