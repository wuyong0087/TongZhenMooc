package com.tzhen.mooc.main.contacts;

import com.tongzhen.mooc.presenters.FollowsListPresenter;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/30.
 */
@EFragment(R.layout.fragment_contact_list)
public class FollowsListFrag extends ContactListFrag {

    @Inject
    FollowsListPresenter presenter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void loadData() {
        presenter.attachView(this, uid);
    }
}
