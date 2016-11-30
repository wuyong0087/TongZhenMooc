package com.tongzhen.common.presenters.subscribers;

import com.tongzhen.common.views.BaseView;

public class BaseViewSubscriber <V extends BaseView, T> extends BaseUseCaseSubscriber<T> {

    protected final V view;

    public BaseViewSubscriber(V view) {
        this.view = view;
    }

    @Override
    public void onError(Throwable e) {
        view.logError(e);
        if(e instanceof java.net.ConnectException || e instanceof java.net.UnknownHostException) {
            view.onNetworkError();
        } else {
            view.onError(e.getMessage(), e);
        }
    }

    @Override public void onNext(T response) {
        view.onSuccess(response);
    }
}
