package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.interactors.MyCollectionUseCase;
import com.tongzhen.mooc.views.WorksListView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/29.
 */
public class MyCollectionWorksPresenter implements Presenter<WorksListView> {
    private MyCollectionUseCase myCollectionUseCase;

    @Inject
    public MyCollectionWorksPresenter(MyCollectionUseCase myCollectionUseCase) {
        this.myCollectionUseCase = myCollectionUseCase;
    }

    @Override
    public void attachView(WorksListView view) {
        myCollectionUseCase.execute(new BaseProgressViewSubscriber<WorksListView, WorksListInfo>(view) {
        });
    }

    public void attachView(WorksListView view, String uid, int page, int max) {
        myCollectionUseCase.signParams(uid, page, max);
        attachView(view);
    }

    @Override
    public void destroy() {
        myCollectionUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
