package com.tzhen.mooc.fragments;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.tongzhen.mooc.entities.SendCodeInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.entities.types.SendCodeType;
import com.tongzhen.mooc.presenters.SendCodePresenter;
import com.tongzhen.mooc.views.SendCodeView;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;
import com.tzhen.mooc.activities.ForgotPwdActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/16.
 */
@EFragment(R.layout.fragment_send_code)
public class SendCodeFragment extends BaseFragment<SendCodeInfo> implements SendCodeView {

    @ViewById(R.id.et_email)
    EditText etEmail;

    @ViewById(R.id.et_verify_code)
    EditText etVerifyCode;

    @Inject
    SendCodePresenter presenter;

    private String code;

    private String username;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

    }

    @Click({R.id.tv_get_code, R.id.tv_next})
    public void onViewsClick(View v){
        switch (v.getId()){
            case R.id.tv_get_code:
                getVerifyCode();
                break;

            case R.id.tv_next:
                next();
                break;
        }
    }

    private void next() {
        String verifyCode = etVerifyCode.getText().toString().trim();
        if (code.equals(verifyCode)){
            ((ForgotPwdActivity)getActivity()).attachFragment(ForgotPwdActivity.SEND_CODE, username);
        } else{
            showMsg(getString(R.string.verify_code_error));
        }
    }

    private void getVerifyCode() {
        username = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            showMsg("Email error");
        }
        presenter.attachView(this, username, SendCodeType.FAG);
    }

    @Override
    public void onSuccess(SendCodeInfo value) {
        if (ResultCodes.OK == value.getResult()){
            code = value.getCode();
        } else{
            showMsg(value.getErrorMsg());
        }
    }
}
