package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.common.presenters.subscribers.BaseUseCaseSubscriber;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CommentListInfo;
import com.tongzhen.mooc.entities.WorksInfo;
import com.tongzhen.mooc.interactors.VideoInfoUseCase;
import com.tongzhen.mooc.interactors.WorksCommentListUseCase;
import com.tongzhen.mooc.interactors.WorksCommentUseCase;
import com.tongzhen.mooc.views.WorksInfoView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/19.
 */
public class WorksInfoPresenter implements Presenter<WorksInfoView> {
    private VideoInfoUseCase videoInfoUseCase;
    private WorksCommentListUseCase worksCommentListUseCase;
    private WorksCommentUseCase worksCommentUseCase;

    @Inject
    public WorksInfoPresenter(VideoInfoUseCase videoInfoUseCase, WorksCommentListUseCase worksCommentListUseCase, WorksCommentUseCase worksCommentUseCase) {
        this.videoInfoUseCase = videoInfoUseCase;
        this.worksCommentListUseCase = worksCommentListUseCase;
        this.worksCommentUseCase = worksCommentUseCase;
    }

    @Override
    public void attachView(WorksInfoView view) {
        videoInfoUseCase.execute(new BaseProgressViewSubscriber<WorksInfoView, WorksInfo>(view) {
            @Override
            public void onNext(WorksInfo response) {
                super.onNext(response);
                loadComments(view, response.getVid(), 1, 100);
            }
        });
    }

    public void attachView(WorksInfoView view, String uid, int vid) {
        videoInfoUseCase.signParams(uid, vid);
        attachView(view);
    }

    public void loadComments(final WorksInfoView view, int vid, int page, int max){
        worksCommentListUseCase.signParams(vid, page, max);
        worksCommentListUseCase.execute(new BaseUseCaseSubscriber<CommentListInfo>() {
            @Override
            public void onNext(CommentListInfo commentListInfo) {
                view.onLoadedComments(commentListInfo);
            }
        });
    }

    public void sendComment(final WorksInfoView view, int vid, String uid, String conent){
        worksCommentUseCase.signParams(vid, uid, conent);
        worksCommentUseCase.execute(new BaseProgressViewSubscriber<WorksInfoView, BaseInfo>(view) {
            @Override
            public void onNext(BaseInfo baseInfo) {
                view.onCommentPost(baseInfo);
            }
        });
    }

    @Override
    public void destroy() {
        videoInfoUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
