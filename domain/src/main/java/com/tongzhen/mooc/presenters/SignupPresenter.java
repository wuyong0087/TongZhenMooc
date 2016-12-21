package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.RegisterInfo;
import com.tongzhen.mooc.entities.params.RegisterParams;
import com.tongzhen.mooc.interactors.RegisterUseCase;
import com.tongzhen.mooc.views.SignUpView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/21.
 */
public class SignUpPresenter implements Presenter<SignUpView> {
    private RegisterUseCase registerUseCase;

    @Inject
    public SignUpPresenter(RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    @Override
    public void attachView(SignUpView view) {
        registerUseCase.execute(new BaseProgressViewSubscriber<SignUpView, RegisterInfo>(view) {
        });
    }

    public void attachView(SignUpView view, RegisterParams params) {
        registerUseCase.signParams(params);
        attachView(view);
    }

    @Override
    public void destroy() {
        registerUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
