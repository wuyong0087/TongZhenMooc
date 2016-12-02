package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.UserVerifyInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class UserVerifyInfoUseCase extends BaseUseCase<UserVerifyInfo> {
    private final SessionRepository sessionRepository;
    private String uid;

    @Inject
    protected UserVerifyInfoUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<UserVerifyInfo> buildUseCaseObservable() {
        return sessionRepository.user_verify_info(uid);
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
