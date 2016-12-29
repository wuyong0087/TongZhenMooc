package com.tzhen.mooc.main.q_a;

import com.tongzhen.mooc.presenters.ListedQAListPresenter;
import com.tzhen.mooc.fragments.QAListFragment;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/29.
 */
public class ListedQAListFrag extends QAListFragment {

    @Inject
    ListedQAListPresenter presenter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }
}
