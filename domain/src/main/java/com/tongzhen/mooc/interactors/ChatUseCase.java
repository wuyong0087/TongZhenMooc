package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.ChatListInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class ChatUseCase extends BaseUseCase<ChatListInfo> {
    private final SessionRepository sessionRepository;
    private String oid;
    private String uid;

    @Inject
    protected ChatUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<ChatListInfo> buildUseCaseObservable() {
        return sessionRepository.chat(oid, uid);
    }

    public void signParams(String oid, String uid){
        this.oid = oid;
        this.uid = uid;
    }
}
