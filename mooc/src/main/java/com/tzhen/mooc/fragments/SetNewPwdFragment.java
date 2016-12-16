package com.tzhen.mooc.fragments;

import android.widget.EditText;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.ForgotPwdPresenter;
import com.tongzhen.mooc.views.ForgotPwdView;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;
import com.tzhen.mooc.activities.ForgotPwdActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/16.
 */
@EFragment(R.layout.fargment_set_new_pwd)
public class SetNewPwdFragment extends BaseFragment<BaseInfo> implements ForgotPwdView {

    private static final String ARG_EMAIL = "ARG_EMAIL";
    @ViewById(R.id.et_password)
    EditText etPassword;

    @ViewById(R.id.et_confirm_password)
    EditText etConfirmPassword;

    @FragmentArg(ARG_EMAIL) String username;

    @Inject
    ForgotPwdPresenter presenter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

    }

    @Click(R.id.tv_submit)
    public void submit(){
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!password.equals(confirmPassword)){
            showMsg(getString(R.string.password_not_match));
            return;
        }

        presenter.attachView(this, username, password);
    }

    @Override
    public void onSuccess(BaseInfo value) {
        if (ResultCodes.OK == value.getResult()){
            showMsg(getString(R.string.reset_pwd_success));

            getActivity().finish();
        } else{
            showMsg(value.getErrorMsg());
        }
    }
}
