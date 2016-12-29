package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.interactors.WorksGoodListUseCase;
import com.tongzhen.mooc.views.WorksListView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/29.
 */
public class FeaturedWorksPresenter implements Presenter<WorksListView> {
    private WorksGoodListUseCase worksGoodListUseCase;

    @Inject
    public FeaturedWorksPresenter(WorksGoodListUseCase worksGoodListUseCase) {
        this.worksGoodListUseCase = worksGoodListUseCase;
    }

    @Override
    public void attachView(WorksListView view) {
        worksGoodListUseCase.execute(new BaseProgressViewSubscriber<WorksListView, WorksListInfo>(view) {});
    }

    public void attachView(WorksListView view, String uid, int page, int max) {
        worksGoodListUseCase.signParams(uid, page, max);
        attachView(view);
    }

    @Override
    public void destroy() {
        worksGoodListUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
