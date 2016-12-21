package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.CourseListInfo;
import com.tongzhen.mooc.interactors.CourseListUseCase;
import com.tongzhen.mooc.views.CourseListView;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/12/21.
 */
public class CourseListPresenter implements Presenter<CourseListView> {
    private CourseListUseCase courseListUseCase;

    @Inject
    public CourseListPresenter(CourseListUseCase courseListUseCase) {
        this.courseListUseCase = courseListUseCase;
    }

    @Override
    public void attachView(CourseListView view) {
        courseListUseCase.execute(new BaseProgressViewSubscriber<CourseListView, CourseListInfo>(view) {
        });
    }

    public void attachView(CourseListView view, int subject, int page, int max) {
        courseListUseCase.signParams(subject, page, max);
        attachView(view);
    }

    @Override
    public void destroy() {
        courseListUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
