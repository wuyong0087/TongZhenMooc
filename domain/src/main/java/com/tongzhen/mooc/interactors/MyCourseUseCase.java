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
public class MyCourseUseCase extends BaseUseCase<CourseListInfo> {
    private final SessionRepository sessionRepository;
    private String uid;

    @Inject
    protected MyCourseUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<CourseListInfo> buildUseCaseObservable() {
        return sessionRepository.my_course(uid);
    }

    public void signParams(String uid) {
        this.uid = uid;
    }
}
