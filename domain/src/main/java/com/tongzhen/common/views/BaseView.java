package com.tongzhen.common.views;

public interface BaseView<T> extends View<T> {
    void onError(String message, Throwable e);
    void onSuccess(T value);
}
