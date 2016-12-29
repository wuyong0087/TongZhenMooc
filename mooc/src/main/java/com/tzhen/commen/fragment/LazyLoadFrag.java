package com.tzhen.commen.fragment;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wuyong on 16/12/29.
 */
public abstract class LazyLoadFrag<T> extends BaseFragment<T> {

    protected boolean isVisible;

    protected boolean isPrepared;

    protected boolean hasLoadData;

    @Override
    protected void initViews() {
        super.initViews();
//        View mView = getView();
//        ViewGroup parent = (ViewGroup) mView.getParent();
//        if (parent != null) {
//            parent.removeView(mView);
//        }
    }

    @Override
    public void onSuccess(T value) {
        hasLoadData = true;
        showDefaultView(value);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisibility();
        } else {
            isVisible = false;
            onInvisibility();
        }
    }

    protected void onVisibility() {
        lazyLoad();
    }

    protected void onInvisibility() {
    }

    protected abstract void lazyLoad();
}
