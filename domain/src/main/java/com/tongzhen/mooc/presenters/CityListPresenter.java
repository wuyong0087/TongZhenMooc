package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.CityListInfo;
import com.tongzhen.mooc.interactors.CityUseCase;
import com.tongzhen.mooc.views.CityListView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/23.
 */
public class CityListPresenter implements Presenter<CityListView> {
    private CityUseCase cityUseCase;

    @Inject
    public CityListPresenter(CityUseCase cityUseCase) {
        this.cityUseCase = cityUseCase;
    }

    @Override
    public void attachView(CityListView view) {
        cityUseCase.execute(new BaseProgressViewSubscriber<CityListView, CityListInfo>(view) {
        });
    }

    @Override
    public void destroy() {
        cityUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
