package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.AnswerListInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class AnswerListUseCase extends BaseUseCase<AnswerListInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private int qid;

    @Inject
    protected AnswerListUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<AnswerListInfo> buildUseCaseObservable() {
        return sessionRepository.answer_list(uid, qid);
    }

    public void signParam(String uid, int qid){
        this.uid = uid;
        this.qid = qid;
    }
}
