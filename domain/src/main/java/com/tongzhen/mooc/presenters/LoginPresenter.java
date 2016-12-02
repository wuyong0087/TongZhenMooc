package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.interactors.LoginUseCase;
import com.tongzhen.mooc.views.LoginView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/2.
 */
public class LoginPresenter implements Presenter<LoginView> {
    private final LoginUseCase loginUseCase;

    @Inject
    public LoginPresenter(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    public void attachView(LoginView view) {
        loginUseCase.execute(new BaseProgressViewSubscriber<LoginView, UserInfo>(view) {});
    }

    public void attachView(LoginView view, String username, String password) {
        loginUseCase.signParams(username, password);
        attachView(view);
    }

    @Override
    public void destroy() {
        loginUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
