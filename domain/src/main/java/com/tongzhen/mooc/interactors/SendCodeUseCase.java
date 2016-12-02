package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.SendCodeInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class SendCodeUseCase extends BaseUseCase<SendCodeInfo> {
    private final SessionRepository sessionRepository;
    private String username;
    private String type;

    @Inject
    protected SendCodeUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<SendCodeInfo> buildUseCaseObservable() {
        return sessionRepository.send_code(username, type);
    }

    public void signParams(String username, String type){
        this.username = username;
        this.type = type;
    }
}
