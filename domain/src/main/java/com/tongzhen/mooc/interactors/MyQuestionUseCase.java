package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class MyQuestionUseCase extends BaseUseCase<QuestionListInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private String type;

    @Inject
    protected MyQuestionUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<QuestionListInfo> buildUseCaseObservable() {
        return sessionRepository.my_question(uid, type);
    }

    public void signParams(String uid, String type){
        this.uid = uid;
        this.type = type;
    }
}
