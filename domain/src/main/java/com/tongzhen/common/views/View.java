package com.tongzhen.common.views;

public interface View {
    void onNetworkError();
    void showProgress();
    void hideProgress();
    void onError(String message, Throwable e);
    void logError(Throwable e);
}
