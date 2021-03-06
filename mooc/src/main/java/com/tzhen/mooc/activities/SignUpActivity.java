package com.tzhen.mooc.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.tongzhen.mooc.entities.RegisterInfo;
import com.tongzhen.mooc.entities.params.RegisterParams;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.presenters.SignUpPresenter;
import com.tongzhen.mooc.views.SignUpView;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.commen.utils.StringUtils;
import com.tzhen.mooc.R;
import com.tzhen.mooc.fragments.RegisterStep1Frag_;
import com.tzhen.mooc.fragments.RegisterStep2Frag;
import com.tzhen.mooc.fragments.RegisterStep2Frag_;
import com.tzhen.mooc.fragments.RegisterStep3Frag;
import com.tzhen.mooc.fragments.RegisterStep3Frag_;

import org.androidannotations.annotations.EActivity;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/12.
 */
@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends BaseActivity<RegisterInfo> implements SignUpView {

    public static final int STEP_1 = 1;

    public static final int STEP_2 = 2;

    public static final int STEP_3 = 3;

    private FragmentManager fm;
    private Fragment frag = null;

    private RegisterParams.Builder paramsBuilder;

    @Inject
    SignUpPresenter presenter;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        paramsBuilder = new RegisterParams.Builder();

        fm = getSupportFragmentManager();

        attachFragment(STEP_1);
    }

    public void attachFragment(int step) {
        switch (step) {
            case STEP_1:
                frag = RegisterStep1Frag_.builder().build();
                break;
            case STEP_2:
                frag = RegisterStep2Frag_.builder().build();
                break;
            case STEP_3:
                frag = RegisterStep3Frag_.builder().build();
                break;
        }

        fm.beginTransaction().replace(R.id.fl_container, frag).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onKeyBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            onKeyBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onKeyBack() {
        if (frag instanceof RegisterStep3Frag){
            attachFragment(STEP_2);

        } else if (frag instanceof RegisterStep2Frag){
            attachFragment(STEP_1);
        }
    }

    public void setupParams(RegisterParams params){
        if (!StringUtils.isEmpty(params.getUsername())){
            paramsBuilder.setUsername(params.getUsername());
        }
        if (!StringUtils.isEmpty(params.getPassword())){
            paramsBuilder.setPassword(params.getPassword());
        }
        if (!StringUtils.isEmpty(params.getNickname())){
            paramsBuilder.setNickname(params.getNickname());
        }
        if (!StringUtils.isEmpty(params.getDescription())){
            paramsBuilder.setDescription(params.getDescription());
        }
        if (params.getSex() > -1){
            paramsBuilder.setSex(params.getSex());
        }
        if (params.getCountry() > 0){
            paramsBuilder.setCountry(params.getCountry());
        }
    }

    public void signup(){
        presenter.attachView(this, paramsBuilder.build());
    }

    @Override
    public void onSuccess(RegisterInfo value) {
        if (ResultCodes.OK == value.getResult()){
            attachFragment(STEP_3);
        } else{
            showMsg(value.getErrorMsg());
        }
    }
}
