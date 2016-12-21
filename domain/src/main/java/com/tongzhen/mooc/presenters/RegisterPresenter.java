package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.RegisterInfo;
import com.tongzhen.mooc.entities.params.RegisterParams;
import com.tongzhen.mooc.interactors.RegisterUseCase;
import com.tongzhen.mooc.views.RegisterView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/2.
 */
public class RegisterPresenter implements Presenter<RegisterView> {
    private final RegisterUseCase useCase;

    @Inject
    public RegisterPresenter(RegisterUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void attachView(RegisterView view) {
        useCase.execute(new BaseProgressViewSubscriber<RegisterView, RegisterInfo>(view) {});
    }

    public void attachView(RegisterView view, RegisterParams params) {
        useCase.signParams(params);
        attachView(view);
    }

    @Override
    public void destroy() {
        useCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
