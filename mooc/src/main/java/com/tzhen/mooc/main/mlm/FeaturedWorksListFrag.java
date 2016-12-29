package com.tzhen.mooc.main.mlm;

import com.tongzhen.mooc.presenters.FeaturedWorksPresenter;
import com.tzhen.commen.config.AppConfig;
import com.tzhen.mooc.R;

import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/29.
 */
@EFragment(R.layout.fragment_works_list)
public class FeaturedWorksListFrag extends WorksListFragment {

    @Inject FeaturedWorksPresenter presenter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    public void loadData() {
        presenter.attachView(this, uid, currentPage, AppConfig.PAGE_MAX_ITEM);
    }
}
