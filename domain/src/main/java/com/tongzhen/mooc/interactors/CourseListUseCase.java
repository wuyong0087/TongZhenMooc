package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.CourseListInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class CourseListUseCase extends BaseUseCase<CourseListInfo> {
    private final SessionRepository sessionRepository;
    private int subject;
    private int page;
    private int max;

    @Inject
    protected CourseListUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<CourseListInfo> buildUseCaseObservable() {
        return sessionRepository.course_list(subject, page, max);
    }

    public void signParams(int subject, int page, int max){
        this.subject = subject;
        this.page = page;
        this.max = max;
    }
}
