package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.interactors.QuestionListUseCase;
import com.tongzhen.mooc.views.QuestionListView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/29.
 */
public class ListedQAListPresenter implements Presenter<QuestionListView> {
    private QuestionListUseCase questionListUseCase;

    @Inject
    public ListedQAListPresenter(QuestionListUseCase questionListUseCase) {
        this.questionListUseCase = questionListUseCase;
    }

    @Override
    public void attachView(QuestionListView view) {
        questionListUseCase.execute(new BaseProgressViewSubscriber<QuestionListView, QuestionListInfo>(view) {});
    }

    public void attachView(QuestionListView view, int page, int max) {
        questionListUseCase.signParams(page, max);
        attachView(view);
    }

    @Override
    public void destroy() {
        questionListUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
