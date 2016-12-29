package com.tzhen.mooc.main.mlm;

import com.tongzhen.mooc.presenters.MyCollectionWorksPresenter;
import com.tzhen.commen.config.AppConfig;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/29.
 */
@EFragment(R.layout.fragment_works_list)
public class MyCollectionWorksListFrag extends WorksListFragment {

    @Inject MyCollectionWorksPresenter presenter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
    }

    @Override
    public void loadData() {
        presenter.attachView(this, uid, currentPage, AppConfig.PAGE_MAX_ITEM);
    }
}
