package com.tongzhen.mooc.presenters;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.entities.types.WorksListType;
import com.tongzhen.mooc.interactors.MyCollectionUseCase;
import com.tongzhen.mooc.interactors.WorksGoodListUseCase;
import com.tongzhen.mooc.interactors.WorksShareListUseCase;
import com.tongzhen.mooc.views.WorksListView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/13.
 */
public class WorksListPresenter implements Presenter<WorksListView> {
    private WorksGoodListUseCase worksGoodListUseCase;
    private WorksShareListUseCase worksShareListUseCase;
    private MyCollectionUseCase myCollectionUseCase;

    private int listType;

    @Inject
    public WorksListPresenter(WorksGoodListUseCase worksGoodListUseCase, WorksShareListUseCase worksShareListUseCase, MyCollectionUseCase myCollectionUseCase) {
        this.worksGoodListUseCase = worksGoodListUseCase;
        this.worksShareListUseCase = worksShareListUseCase;
        this.myCollectionUseCase = myCollectionUseCase;
    }

    @Override
    public void attachView(WorksListView view) {
        BaseUseCase<WorksListInfo> baseUseCase = null;
        switch (listType) {
            case WorksListType.FAVORITES:
                baseUseCase = myCollectionUseCase;
                break;
            case WorksListType.SHARED:
                baseUseCase = worksShareListUseCase;
                break;
            case WorksListType.FEATURED:
                baseUseCase = worksGoodListUseCase;
                break;
        }
        if (baseUseCase != null){
            baseUseCase.execute(new BaseProgressViewSubscriber<WorksListView, WorksListInfo>(view) {
            });
        }
    }

    public void loadWorksList(WorksListView view, String uid, int page, int max, int listType) {
        this.listType = listType;
        switch (listType) {
            case WorksListType.FEATURED:
                worksGoodListUseCase.signParams(uid, page, max);
                break;
            case WorksListType.SHARED:
                worksShareListUseCase.signParams(uid, page, max);
                break;
            case WorksListType.FAVORITES:
                myCollectionUseCase.signParams(uid, page, max);
                break;
        }

        attachView(view);
    }

    @Override
    public void destroy() {
        myCollectionUseCase.unsubscribe();
        worksGoodListUseCase.unsubscribe();
        worksShareListUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
