package com.tongzhen.common.presenters.subscribers;

import com.tongzhen.common.views.BaseView;

public abstract class BaseProgressViewSubscriber <V extends BaseView, T>  extends BaseViewSubscriber <V, T> {

    public BaseProgressViewSubscriber(V view) {
        super(view);
    }

    @Override
    public void onStart() {
        view.showProgress();
    }

    @Override
    public void onError(Throwable e) {
        view.hideProgress();
        super.onError(e);
    }

    @Override
    public void onNext(T response) {
        view.hideProgress();
        super.onNext(response);
    }
}