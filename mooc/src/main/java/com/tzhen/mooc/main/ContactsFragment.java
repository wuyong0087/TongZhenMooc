package com.tzhen.mooc.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wuyong on 16/12/13.
 */
@EFragment(R.layout.fragment_qa)
public class ContactsFragment extends BaseFragment<BaseInfo> implements TabLayout.OnTabSelectedListener {
    private int[] title = {R.string.friends, R.string.fans, R.string.follows};
    @ViewById(R.id.tab_top)
    TabLayout tabTop;

    @ViewById(R.id.vp_container)
    ViewPager vpContainer;

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
        for (int i = 0; i < title.length; i++) {
            tabTop.addTab(tabTop.newTab().setText(title[i]));
        }

        tabTop.setOnTabSelectedListener(this);
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
