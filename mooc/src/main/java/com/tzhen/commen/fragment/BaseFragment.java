package com.tzhen.commen.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tongzhen.common.views.View;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.commen.di.ApplicationComponent;
import com.tzhen.mooc.progress.DialogProgress;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

@EFragment
public abstract class BaseFragment<T> extends Fragment implements View<T> {

    protected boolean isRegister;

    protected EventBus eventBus;

    @Inject
    DialogProgress progress;

    @AfterInject
    protected void init() {
    }

    @AfterViews
    protected void initViews() {

    }

    protected ApplicationComponent getApplicationComponent() {
        return ((BaseActivity)getActivity()).getApplicationComponent();
    }

    @Override
    public void showProgress() {
        progress.showProgressView(getActivity());
    }

    @Override
    public void hideProgress() {
        progress.hideProgressView();
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

    public void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showMsg(int message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (!isRegister){
//            eventBus = EventBus.getDefault();
//            eventBus.register(this);
//            isRegister = true;
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        if (isRegister){
//            eventBus.unregister(this);
//            isRegister = false;
//        }
    }
}

