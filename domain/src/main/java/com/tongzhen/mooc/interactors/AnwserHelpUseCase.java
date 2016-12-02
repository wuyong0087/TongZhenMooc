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
public class AnwserHelpUseCase extends BaseUseCase<BaseInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private int qid;
    private int val;

    @Inject
    protected AnwserHelpUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<BaseInfo> buildUseCaseObservable() {
        return sessionRepository.anwser_help(uid, qid, val);
    }

    public void signParams(int qid, int val, String uid){
        this.qid = qid;
        this.val = val;
        this.uid = uid;
    }
}
