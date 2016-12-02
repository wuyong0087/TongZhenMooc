package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class CourseInfoWorksUseCase extends BaseUseCase<WorksListInfo> {
    private final SessionRepository sessionRepository;
    private int cid;
    private String uid;
    private int is_teacher;

    @Inject
    protected CourseInfoWorksUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<WorksListInfo> buildUseCaseObservable() {
        return sessionRepository.course_info_works(cid, uid, is_teacher);
    }

    public void signParams(int cid, String uid, int is_teacher){
        this.cid = cid;
        this.uid = uid;
        this.is_teacher = is_teacher;
    }
}
