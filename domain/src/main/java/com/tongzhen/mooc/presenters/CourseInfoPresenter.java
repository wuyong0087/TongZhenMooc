package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.common.presenters.subscribers.BaseUseCaseSubscriber;
import com.tongzhen.mooc.entities.CourseIntroduceInfo;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.interactors.CourseInfoIntroduceUseCase;
import com.tongzhen.mooc.interactors.CourseInfoQuestionUseCase;
import com.tongzhen.mooc.interactors.CourseInfoWorksUseCase;
import com.tongzhen.mooc.views.CourseInfoView;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/12/21.
 */
public class CourseInfoPresenter implements Presenter<CourseInfoView> {
    private CourseInfoIntroduceUseCase infoIntroduceUseCase;
    private CourseInfoWorksUseCase worksUseCase;
    private CourseInfoQuestionUseCase questionUseCase;

    @Inject
    public CourseInfoPresenter(CourseInfoIntroduceUseCase infoIntroduceUseCase, CourseInfoWorksUseCase worksUseCase, CourseInfoQuestionUseCase questionUseCase) {
        this.infoIntroduceUseCase = infoIntroduceUseCase;
        this.worksUseCase = worksUseCase;
        this.questionUseCase = questionUseCase;
    }

    @Override
    public void attachView(CourseInfoView view) {
        infoIntroduceUseCase.execute(new BaseProgressViewSubscriber<CourseInfoView, CourseIntroduceInfo>(view) {
        });
    }

    public void attachView(CourseInfoView view, int cid, String uid) {
        infoIntroduceUseCase.signParams(cid, uid);
        attachView(view);
    }

    public void getWorks(final CourseInfoView view, int cid, String uid, int is_teacher){
        worksUseCase.signParams(cid, uid, is_teacher);
        worksUseCase.execute(new BaseUseCaseSubscriber<WorksListInfo>() {
            @Override
            public void onNext(WorksListInfo worksListInfo) {
                view.onGetWorks(worksListInfo);
            }
        });
    }

    public void getQuestions(final CourseInfoView view, int cid){
        questionUseCase.signParams(cid);
        questionUseCase.execute(new BaseUseCaseSubscriber<QuestionListInfo>() {
            @Override
            public void onNext(QuestionListInfo questionListInfo) {
                view.onGetQuestions(questionListInfo);
            }
        });
    }

    @Override
    public void destroy() {
        infoIntroduceUseCase.unsubscribe();
        worksUseCase.unsubscribe();
        questionUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
