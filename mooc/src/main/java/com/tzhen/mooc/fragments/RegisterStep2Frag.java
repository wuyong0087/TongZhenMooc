package com.tzhen.mooc.fragments;

import android.content.Context;
import android.support.annotation.MainThread;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.params.RegisterParams;
import com.tzhen.commen.fragment.BaseFragment;
import com.tzhen.mooc.R;
import com.tzhen.mooc.activities.SignUpActivity;
import com.tzhen.mooc.events.CountryEvent;
import com.tzhen.mooc.navigator.Navigator;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/2.
 */
@EFragment(R.layout.fragment_register_step_second)
public class RegisterStep2Frag extends BaseFragment<BaseInfo> {

    @ViewById(R.id.et_nickname) EditText etUsername;

    @ViewById(R.id.tv_gender) TextView tvGender;

    @ViewById(R.id.tv_country) TextView tvCountry;

    @Inject
    Navigator navigator;

    private RegisterParams.Builder paramsBuilder;

    private EventBus eventBus;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        paramsBuilder = new RegisterParams.Builder();

    }

    @Click({R.id.btn_next, R.id.tv_gender, R.id.tv_country})
    public void signUp(View v){
        switch (v.getId()){
            case R.id.tv_next:
                SignUpActivity activity = (SignUpActivity) getActivity();
                activity.setupParams(paramsBuilder.build());
                activity.attachFragment(SignUpActivity.STEP_3);
            break;
            case R.id.tv_gender:
                showGenderDialog();
                break;
            case R.id.tv_country:
                showCountriesDialog();
                break;
        }

    }

    private void showCountriesDialog() {
        navigator.toCountryList(getContext());
    }

    private void showGenderDialog(){
        new MaterialDialog.Builder(getContext())
                .title(R.string.gender_dialog_title)
                .items(R.array.gender)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int gender, CharSequence text) {
                        paramsBuilder.setSex(gender);
                        tvGender.setText(text);
                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCountryEvent(CountryEvent event){
        if (event != null){
            tvCountry.setText(event.getInfo().getName());
            paramsBuilder.setCountry(event.getInfo().getId());
        }
    }
}
