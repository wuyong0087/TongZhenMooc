package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.UserListInfo;
import com.tongzhen.mooc.interactors.MyFriendsUseCase;
import com.tongzhen.mooc.views.UserListView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/30.
 */
public class FriendsListPresenter implements Presenter<UserListView> {
    private MyFriendsUseCase myFriendsUseCase;

    @Inject
    public FriendsListPresenter(MyFriendsUseCase myFriendsUseCase) {
        this.myFriendsUseCase = myFriendsUseCase;
    }

    @Override
    public void attachView(UserListView view) {
        myFriendsUseCase.execute(new BaseProgressViewSubscriber<UserListView, UserListInfo>(view) {
        });
    }
    public void attachView(UserListView view, String uid) {
        myFriendsUseCase.signParams(uid);
        attachView(view);
    }

    @Override
    public void destroy() {
        myFriendsUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
