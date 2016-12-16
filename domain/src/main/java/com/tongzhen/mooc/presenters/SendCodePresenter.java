package com.tongzhen.mooc.presenters;

import com.tongzhen.common.presenters.Presenter;
import com.tongzhen.common.presenters.subscribers.BaseProgressViewSubscriber;
import com.tongzhen.mooc.entities.SendCodeInfo;
import com.tongzhen.mooc.interactors.SendCodeUseCase;
import com.tongzhen.mooc.views.SendCodeView;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/16.
 */
public class SendCodePresenter implements Presenter<SendCodeView> {
    private SendCodeUseCase sendCodeUseCase;

    @Inject
    public SendCodePresenter(SendCodeUseCase sendCodeUseCase) {
        this.sendCodeUseCase = sendCodeUseCase;
    }

    @Override
    public void attachView(SendCodeView view) {
        sendCodeUseCase.execute(new BaseProgressViewSubscriber<SendCodeView, SendCodeInfo>(view) {
        });
    }


    public void attachView(SendCodeView view, String username, String type) {
        sendCodeUseCase.signParams(username, type);
        attachView(view);
    }

    @Override
    public void destroy() {
        sendCodeUseCase.unsubscribe();
    }

    @Override
    public void retry() {

    }
}
