package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.WorksInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class VideoInfoUseCase extends BaseUseCase<WorksInfo> {
    private final SessionRepository sessionRepository;
    private int vid;
    private String uid;

    @Inject
    protected VideoInfoUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<WorksInfo> buildUseCaseObservable() {
        return sessionRepository.video_info(vid, uid);
    }

    public void signParams(){
        this.vid = vid;
        this.uid = uid;
    }
}
