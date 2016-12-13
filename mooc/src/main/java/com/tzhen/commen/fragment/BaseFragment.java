package com.tzhen.commen.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tongzhen.common.views.View;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.commen.di.ApplicationComponent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import rx.Subscription;

@EFragment
public abstract class BaseFragment<T> extends Fragment implements View<T> {

    protected Intent errorIntent;
    protected Subscription mRxSub;

    protected boolean isVisable;

    protected boolean isPrepared;

    protected boolean hasLoadData;

    protected boolean isRegister;

    protected boolean isRefreshing;

    protected int memberId;
    protected float[] location;

    @AfterInject
    protected void init() {
    }

    @AfterViews
    protected void initViews() {
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((BaseActivity)getActivity()).getApplicationComponent();
    }

    protected void showMsg(int message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }


    @Override
    public void onError(String message, Throwable e){
        showMsg(e.getMessage());
    }

    @Override
    public void onNetworkError(){
    }

    @Override
    public void logError(Throwable e){

    }

    protected void sendErrorCode(String errorCode){
    }


    @Override
    public void onSuccess(T value) {
        showDefaultView(value);
    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void showEmptyView() {

    }


    @Override
    public void showDefaultView(T value) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisable = true;
            onVisibility();
        } else {
            isVisable = false;
            onInvisibility();
        }
    }

    protected void onVisibility() {
        lazyLoad();
    }

    protected void onInvisibility() {
    }

    protected void lazyLoad(){

    }

    public void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}

