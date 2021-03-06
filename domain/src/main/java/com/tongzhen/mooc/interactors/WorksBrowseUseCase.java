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
public class WorksBrowseUseCase extends BaseUseCase<BaseInfo> {
    private final SessionRepository sessionRepository;
    private int vid;
    private String uid;

    @Inject
    protected WorksBrowseUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<BaseInfo> buildUseCaseObservable() {
        return sessionRepository.works_browse(vid, uid);
    }

    public void signParams(int vid, String uid){
        this.vid = vid;
        this.uid = uid;
    }
}
