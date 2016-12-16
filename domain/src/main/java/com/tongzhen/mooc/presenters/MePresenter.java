package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.interactors.MyInfoUseCase;
import com.tongzhen.mooc.views.MeView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/16.
 */
public class MePresenter implements Presenter<MeView> {
    private MyInfoUseCase userInfoUseCase;

    @Inject
    public MePresenter(MyInfoUseCase userInfoUseCase) {
        this.userInfoUseCase = userInfoUseCase;
    }

    @Override
    public void attachView(MeView view) {
        userInfoUseCase.execute(new BaseProgressViewSubscriber<MeView, UserInfo>(view) {});
    }

    public void attachView(MeView view, String uid) {
        userInfoUseCase.setUid(uid);
        attachView(view);
    }

    @Override
    public void destroy() {
        userInfoUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
