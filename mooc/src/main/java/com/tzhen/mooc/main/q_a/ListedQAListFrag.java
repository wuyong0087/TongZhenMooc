package com.tzhen.mooc.main.q_a;

import com.tongzhen.mooc.presenters.ListedQAListPresenter;
import com.tzhen.commen.config.AppConfig;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/29.
 */
public class ListedQAListFrag extends QAListFrag {

    @Inject
    ListedQAListPresenter presenter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    public void loadData() {
        presenter.attachView(this, currentPage, AppConfig.PAGE_MAX_ITEM);
    }
}
