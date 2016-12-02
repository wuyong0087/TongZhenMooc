package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class CourseEnrollUseCase extends BaseUseCase<BaseInfo> {
    private final SessionRepository sessionRepository;
    private int cid;
    private String uid;
    private int val;

    @Inject
    protected CourseEnrollUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<BaseInfo> buildUseCaseObservable() {
        return sessionRepository.course_enroll(cid, uid, val);
    }

    public void signPrams(int cid, String uid, int val){
        this.cid = cid;
        this.uid = uid;
        this.val = val;
    }
}
