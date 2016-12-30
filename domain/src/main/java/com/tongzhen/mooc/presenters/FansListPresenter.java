package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.UserListInfo;
import com.tongzhen.mooc.interactors.MyFansUseCase;
import com.tongzhen.mooc.views.UserListView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/30.
 */
public class FansListPresenter implements Presenter<UserListView> {
    private MyFansUseCase myFansUseCase;

    @Inject
    public FansListPresenter(MyFansUseCase myFansUseCase) {
        this.myFansUseCase = myFansUseCase;
    }

    @Override
    public void attachView(UserListView view) {
        myFansUseCase.execute(new BaseProgressViewSubscriber<UserListView, UserListInfo>(view) {
        });
    }
    public void attachView(UserListView view, String uid) {
        myFansUseCase.signParams(uid);
        attachView(view);
    }

    @Override
    public void destroy() {
        myFansUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
