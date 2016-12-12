package com.tzhen.commen.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.tongzhen.common.views.View;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.mooc.R;
import com.tzhen.commen.application.AndroidApplication;
import com.tzhen.commen.di.ApplicationComponent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import rx.Subscription;

@EActivity
public abstract class BaseActivity<T extends BaseInfo> extends AppCompatActivity implements View<T> {

    private final String TAG = BaseActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    protected Subscription mRxSub;

    @AfterInject protected void init() {
    }

    @AfterViews protected void initViews() {
    }

    public ApplicationComponent getApplicationComponent() {
        return ((AndroidApplication)getApplication()).getApplicationComponent();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initToolBar(Toolbar toolbar, String title) {
        setupToolbar(toolbar);
        setToolbarTitle(title);
    }

    public void initToolBar(Toolbar toolbar, Boolean homeEnabled) {
        setupToolbar(toolbar);
        setToolBarDisplayHomeAsUpEnabled(homeEnabled);
//        toolbar.setNavigationIcon(R.drawable.back);
    }
    public void initToolBar(Toolbar toolbar) {
        setupToolbar(toolbar);
    }

    public void setToolBarDisplayHomeAsUpEnabled(Boolean homeEnabled) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeEnabled);
    }

    private void setupToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        mToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbarTitle(CharSequence title){
        mToolbarTitle.setText(title);
    }


    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    public void showMsg(String msg){
        hideProgress();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void sendErrorCode(String errorCode){
    }

    @Override
    public void onNetworkError() {
        showMsg("网络异常");
    }

    @Override
    public void onError(String message, Throwable e) {
        showMsg(message);
    }

    @Override
    public void logError(Throwable e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRxSub != null && !mRxSub.isUnsubscribed()) {
            mRxSub.unsubscribe();
        }
    }

    @Override
    public void onSuccess(BaseInfo value) {

    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showDefaultView(BaseInfo value) {

    }
}