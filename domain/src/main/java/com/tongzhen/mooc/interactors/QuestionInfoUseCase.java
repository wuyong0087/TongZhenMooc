package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.QuestionInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class QuestionInfoUseCase extends BaseUseCase<QuestionInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private int qid;

    @Inject
    protected QuestionInfoUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<QuestionInfo> buildUseCaseObservable() {
        return sessionRepository.question_info(uid, qid);
    }

    public void signParams(String uid, int qid){
        this.uid = uid;
        this.qid = qid;
    }
}
