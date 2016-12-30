package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.UserListInfo;
import com.tongzhen.mooc.interactors.MyFansUseCase;
import com.tongzhen.mooc.interactors.MyFollowsUseCase;
import com.tongzhen.mooc.views.UserListView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/30.
 */
public class FollowsListPresenter implements Presenter<UserListView> {
    private MyFollowsUseCase myFollowsUseCase;

    @Inject
    public FollowsListPresenter(MyFollowsUseCase myFollowsUseCase) {
        this.myFollowsUseCase = myFollowsUseCase;
    }

    @Override
    public void attachView(UserListView view) {
        myFollowsUseCase.execute(new BaseProgressViewSubscriber<UserListView, UserListInfo>(view) {
        });
    }
    public void attachView(UserListView view, String uid) {
        myFollowsUseCase.signParams(uid);
        attachView(view);
    }

    @Override
    public void destroy() {
        myFollowsUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
