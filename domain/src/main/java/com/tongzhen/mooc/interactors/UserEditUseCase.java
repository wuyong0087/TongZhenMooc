package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.entities.params.UserInfoEditParams;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class UserEditUseCase extends BaseUseCase<UserInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private UserInfoEditParams params;

    @Inject
    protected UserEditUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<UserInfo> buildUseCaseObservable() {
        return sessionRepository.user_edit(uid, params);
    }

    public void signParams(String uid, UserInfoEditParams params){
        this.uid = uid;
        this.params = params;
    }
}
