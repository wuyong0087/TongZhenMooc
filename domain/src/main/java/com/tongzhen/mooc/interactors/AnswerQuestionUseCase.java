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
public class AnswerQuestionUseCase extends BaseUseCase<BaseInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private int qid;
    private int atype;
    private String content;
    private int vid;

    @Inject
    protected AnswerQuestionUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<BaseInfo> buildUseCaseObservable() {
        return sessionRepository.answer_question(uid, qid, atype, content, vid);
    }

    public void signParams(int qid, int atype, int vid, String uid, String content){
        this.qid = qid;
        this.vid = vid;
        this.atype = atype;
        this.uid = uid;
        this.content = content;
    }
}
