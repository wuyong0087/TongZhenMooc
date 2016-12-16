package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.interactors.ForgotPwdUseCase;
import com.tongzhen.mooc.views.ForgotPwdView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/16.
 */
public class ForgotPwdPresenter implements Presenter<ForgotPwdView> {

    private ForgotPwdUseCase forgotPwdUseCase;

    @Inject
    public ForgotPwdPresenter(ForgotPwdUseCase forgotPwdUseCase) {
        this.forgotPwdUseCase = forgotPwdUseCase;
    }

    @Override
    public void attachView(ForgotPwdView view) {
        forgotPwdUseCase.execute(new BaseProgressViewSubscriber<ForgotPwdView, BaseInfo>(view) {
        });
    }

    public void attachView(ForgotPwdView view, String username, String newPwd) {
        forgotPwdUseCase.signParams(username, newPwd);
        attachView(view);
    }

    @Override
    public void destroy() {
        forgotPwdUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
