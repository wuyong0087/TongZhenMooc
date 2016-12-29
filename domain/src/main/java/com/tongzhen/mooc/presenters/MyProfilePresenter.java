package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.interactors.MyInfoUseCase;
import com.tongzhen.mooc.views.MyProfileView;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/12/25.
 */
public class MyProfilePresenter implements Presenter<MyProfileView> {
    private MyInfoUseCase useCase;

    @Inject
    public MyProfilePresenter(MyInfoUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void attachView(MyProfileView view) {
        useCase.execute(new BaseProgressViewSubscriber<MyProfileView, UserInfo>(view) {
        });
    }

    @Override
    public void destroy() {
        useCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
