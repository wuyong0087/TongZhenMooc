package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.CourseIntroduceInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class CourseInfoIntroduceUseCase extends BaseUseCase<CourseIntroduceInfo> {
    private final SessionRepository sessionRepository;
    private int cid;
    private String uid;

    @Inject
    protected CourseInfoIntroduceUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<CourseIntroduceInfo> buildUseCaseObservable() {
        return sessionRepository.course_info_introduce(cid, uid);
    }

    public void signParams(int cid, String uid){
        this.cid = cid;
        this.uid = uid;
    }
}
