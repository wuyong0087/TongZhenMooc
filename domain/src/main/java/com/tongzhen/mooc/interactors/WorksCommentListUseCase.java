package com.tongzhen.mooc.interactors;

import com.tongzhen.common.interactors.BaseUseCase;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.entities.CommentListInfo;
import com.tongzhen.mooc.repositories.SessionRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by wuyong on 16/12/2.
 */
public class WorksCommentListUseCase extends BaseUseCase<CommentListInfo> {
    private final SessionRepository sessionRepository;
    private int vid;
    private int page;
    private int max;

    @Inject
    protected WorksCommentListUseCase(SubscribeOn subscribeOn, ObserveOn observeOn, SessionRepository sessionRepository) {
        super(subscribeOn, observeOn);
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected Observable<CommentListInfo> buildUseCaseObservable() {
        return sessionRepository.works_comment_list(vid, page, max);
    }

    public void signParams(int vid, int page, int max){
        this.vid = vid;
        this.page = page;
        this.max = max;
    }
}
