package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class AnswerWorksListUseCase extends BaseUseCase<WorksListInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private String type;

    @Inject
    protected AnswerWorksListUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<WorksListInfo> buildUseCaseObservable() {
        return sessionRepository.answer_works_list(uid, type);
    }

    public void signParams(String uid, String type){
        this.uid = uid;
        this.type = type;
    }
}
