package com.tongzhen.common.presenters;

import com.tongzhen.common.views.View;

public interface Presenter<T extends View> {
    void attachView(T view);
    void destroy();
    /**
     * The retry method is defined for a Presenter such that it can handle behaviour for when
     * UseCases need to be 'retried' i.e. exectuted again.
     *
     * The implementation of this method lets the developer define how exactly the presenter should
     * re-run their use cases
     */
    void retry();
}
