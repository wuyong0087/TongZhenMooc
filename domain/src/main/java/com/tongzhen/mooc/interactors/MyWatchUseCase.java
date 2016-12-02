package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class MyWatchUseCase extends BaseUseCase<QuestionListInfo> {
    private final SessionRepository sessionRepository;
    private String uid;
    private int page;
    private int max;

    @Inject
    protected MyWatchUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<QuestionListInfo> buildUseCaseObservable() {
        return sessionRepository.my_watch(uid, page, max);
    }

    public void signParams(String uid, int page, int max){
        this.uid = uid;
        this.page = page;
        this.max = max;
    }
}
