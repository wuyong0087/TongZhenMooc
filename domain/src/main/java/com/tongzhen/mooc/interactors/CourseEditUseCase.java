package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.CourseEditInfo;
import com.tongzhen.mooc.entities.params.CourseEditParams;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class CourseEditUseCase extends BaseUseCase<CourseEditInfo> {
    private final SessionRepository sessionRepository;
    private int cid;
    private CourseEditParams params;

    @Inject
    protected CourseEditUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<CourseEditInfo> buildUseCaseObservable() {
        return sessionRepository.course_edit(cid, params);
    }

    public void signParams(int cid, CourseEditParams params){
        this.cid = cid;
        this.params = params;
    }
}
