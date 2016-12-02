package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class LoginUseCase extends BaseUseCase<UserInfo> {
    private final SessionRepository sessionRepository;
    private String username;
    private String password;

    @Inject
    protected LoginUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<UserInfo> buildUseCaseObservable() {
        return sessionRepository.login(username, password);
    }

    public void signParams(String username, String password){
        this.username = username;
        this.password = password;
    }
}
