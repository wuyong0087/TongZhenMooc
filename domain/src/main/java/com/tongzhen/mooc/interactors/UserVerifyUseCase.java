package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.params.UserVerifyParams;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class UserVerifyUseCase extends BaseUseCase<BaseInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private UserVerifyParams params;

    @Inject
    protected UserVerifyUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<BaseInfo> buildUseCaseObservable() {
        return sessionRepository.user_verify(uid, params);
    }

    public void signParams(String uid, UserVerifyParams params){
        this.uid = uid;
        this.params = params;
    }
}
