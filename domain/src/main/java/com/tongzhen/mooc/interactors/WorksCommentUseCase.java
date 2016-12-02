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
public class WorksCommentUseCase extends BaseUseCase<BaseInfo> {
    private final SessionRepository sessionRepository;
    private int vid;
    private String uid;
    private String content;

    @Inject
    protected WorksCommentUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<BaseInfo> buildUseCaseObservable() {
        return sessionRepository.works_comments(vid, uid, content);
    }

    public void signParams(int vid, String uid, String content){
        this.vid = vid;
        this.uid = uid;
        this.content = content;
    }
}
