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
public class QuestionAskUseCase extends BaseUseCase<BaseInfo> {
    private final SessionRepository sessionRepository;
    private int cid;
    private String uid;
    private String question;
    private String img;

    @Inject
    protected QuestionAskUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<BaseInfo> buildUseCaseObservable() {
        return sessionRepository.question_ask(cid, uid, question, img);
    }

    public void signParams(int cid, String uid, String question, String img){
        this.cid = cid;
        this.uid = uid;
        this.question = question;
        this.img = img;
    }
}
