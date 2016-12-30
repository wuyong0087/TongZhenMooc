package com.tzhen.mooc.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.adapters.ContactListAdapter;
import com.tzhen.commen.adapters.ContactsAdapter;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by wuyong on 16/12/13.
 */
@EFragment(R.layout.fragment_qa)
public class ContactsFragment extends BaseFragment<BaseInfo> {
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

        setupViews();
    }

    private void setupViews() {
        ContactListAdapter adapter = new ContactListAdapter(getChildFragmentManager(), getContext());
        vpContainer.setOffscreenPageLimit(2);
        vpContainer.setAdapter(adapter);

        tabTop.setupWithViewPager(vpContainer);
    }
}
