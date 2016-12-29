package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.interactors.WorksGoodListUseCase;
import com.tongzhen.mooc.interactors.WorksShareListUseCase;
import com.tongzhen.mooc.views.WorksListView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/29.
 */
public class SharedWorksPresenter implements Presenter<WorksListView> {
    private WorksShareListUseCase worksShareListUseCase;

    @Inject
    public SharedWorksPresenter(WorksShareListUseCase worksShareListUseCase) {
        this.worksShareListUseCase = worksShareListUseCase;
    }

    @Override
    public void attachView(WorksListView view) {
        worksShareListUseCase.execute(new BaseProgressViewSubscriber<WorksListView, WorksListInfo>(view) {});
    }

    public void attachView(WorksListView view, String uid, int page, int max) {
        worksShareListUseCase.signParams(uid, page, max);
        attachView(view);
    }

    @Override
    public void destroy() {
        worksShareListUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
