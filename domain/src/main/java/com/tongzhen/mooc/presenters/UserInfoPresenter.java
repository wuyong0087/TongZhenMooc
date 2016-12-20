package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.interactors.UserFollowUseCase;
import com.tongzhen.mooc.interactors.UserInfoUseCase;
import com.tongzhen.mooc.views.UserInfoView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/19.
 */
public class UserInfoPresenter implements Presenter<UserInfoView> {
    private UserInfoUseCase userInfoUseCase;
    private UserFollowUseCase followUseCase;

    @Inject
    public UserInfoPresenter(UserInfoUseCase userInfoUseCase, UserFollowUseCase followUseCase) {
        this.userInfoUseCase = userInfoUseCase;
        this.followUseCase = followUseCase;
    }

    @Override
    public void attachView(UserInfoView view) {
        userInfoUseCase.execute(new BaseProgressViewSubscriber<UserInfoView, UserInfo>(view) {
        });
    }

    public void attachView(UserInfoView view, String uid, String oid) {
        userInfoUseCase.signParams(uid, oid);
        attachView(view);
    }

    @Override
    public void destroy() {
        userInfoUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }

    public void follow(UserInfoView view, String oid, String uid, int val) {
        followUseCase.signParams(uid, oid, val);
        followUseCase.execute(new BaseProgressViewSubscriber<UserInfoView, BaseInfo>(view) {
            @Override
            public void onNext(BaseInfo baseInfo) {
                view.onFollow(baseInfo);
            }
        });
    }
}
