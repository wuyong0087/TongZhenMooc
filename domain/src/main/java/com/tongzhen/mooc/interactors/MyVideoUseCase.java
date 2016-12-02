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
public class MyVideoUseCase extends BaseUseCase<WorksListInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private String type;
    private int page;
    private int max;

    @Inject
    protected MyVideoUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<WorksListInfo> buildUseCaseObservable() {
        return sessionRepository.my_video(uid, type, page + "", max + "");
    }

    public void signParams(String uid, String type, int page, int max){
        this.uid = uid;
        this.type = type;
        this.page = page;
        this.max = max;
    }
}
