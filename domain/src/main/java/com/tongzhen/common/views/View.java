package com.tongzhen.common.views;

public interface View<T> {
    void onNetworkError();
    void showProgress();
    void hideProgress();
    void onError(String message, Throwable e);
    void logError(Throwable e);
    void onSuccess(T value);
    void showErrorView();
    void showEmptyView();
    void showDefaultView(T value);
}
