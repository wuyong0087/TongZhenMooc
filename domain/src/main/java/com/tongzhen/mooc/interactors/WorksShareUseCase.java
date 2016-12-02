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
public class WorksShareUseCase extends BaseUseCase<BaseInfo> {
    private final SessionRepository sessionRepository;
    private int cid;
    private int vid;
    private String uid;
    private String reason;

    @Inject
    protected WorksShareUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<BaseInfo> buildUseCaseObservable() {
        return sessionRepository.works_share(cid, vid, uid, reason);
    }

    public void signParams(int cid, int vid, String uid, String reason){
        this.cid = cid;
        this.vid = vid;
        this.uid = uid;
        this.reason = reason;
    }
}
