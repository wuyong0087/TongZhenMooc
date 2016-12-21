package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.WorksInfo;
import com.tongzhen.mooc.entities.params.WorksUpLoadParams;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class VideoUploadUseCase extends BaseUseCase<WorksInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private WorksUpLoadParams params;

    @Inject
    protected VideoUploadUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<WorksInfo> buildUseCaseObservable() {
        return sessionRepository.video_upload(uid, params);
    }

    public void signParams(String uid, WorksUpLoadParams params){
        this.uid = uid;
        this.params = params;
    }
}
