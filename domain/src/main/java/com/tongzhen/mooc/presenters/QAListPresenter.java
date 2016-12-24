package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.interactors.QuestionListUseCase;
import com.tongzhen.mooc.views.QAListView;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/12/24.
 */
public class QAListPresenter implements Presenter<QAListView> {
    private QuestionListUseCase questionListUseCase;

    @Inject
    public QAListPresenter(QuestionListUseCase questionListUseCase) {
        this.questionListUseCase = questionListUseCase;
    }

    @Override
    public void attachView(QAListView view) {
        questionListUseCase.execute(new BaseProgressViewSubscriber<QAListView, QuestionListInfo>(view) {
        });
    }

    public void attachView(QAListView view, int page, int max) {
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
