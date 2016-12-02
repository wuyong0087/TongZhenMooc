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
public class UserFollowUseCase extends BaseUseCase<BaseInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private String oid;
    private int val;

    @Inject
    protected UserFollowUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<BaseInfo> buildUseCaseObservable() {
        return sessionRepository.user_follow(uid, oid, val);
    }

    public void signParams(String uid, String oid, int val){
        this.uid = uid;
        this.oid = oid;
        this.val = val;
    }
}
