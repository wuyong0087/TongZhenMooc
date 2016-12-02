package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class CourseInfoQuestionUseCase extends BaseUseCase<QuestionListInfo> {
    private final SessionRepository sessionRepository;
    private int cid;

    @Inject
    protected CourseInfoQuestionUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<QuestionListInfo> buildUseCaseObservable() {
        return sessionRepository.course_info_question(cid);
    }

    public void signParams(int cid) {
        this.cid = cid;
    }
}
