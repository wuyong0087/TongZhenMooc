package com.tzhen.commen.di;

import com.tzhen.mooc.activities.ForgotPwdActivity;
import com.tzhen.mooc.activities.LaunchActivity;
import com.tzhen.mooc.activities.LoginActivity;
import com.tzhen.mooc.activities.MainActivity;
import com.tzhen.mooc.activities.SignUpActivity;
import com.tzhen.mooc.activities.UserInfoActivity;
import com.tzhen.mooc.activities.WorksInfoActivity;
import com.tzhen.mooc.fragments.RegisterStep1Frag;
import com.tzhen.mooc.fragments.RegisterStep2Frag;
import com.tzhen.mooc.fragments.RegisterStep3Frag;
import com.tzhen.mooc.fragments.SendCodeFragment;
import com.tzhen.mooc.fragments.SetNewPwdFragment;
import com.tzhen.mooc.main.ContactsFragment;
import com.tzhen.mooc.main.CreateSuccessActivity;
import com.tzhen.mooc.main.MLMFragment;
import com.tzhen.mooc.main.MeFragment;
import com.tzhen.mooc.main.QAFragment;
import com.tzhen.mooc.main.WorksListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by wuyong on 2016/12/11.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(LaunchActivity launchActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(ForgotPwdActivity forgotPwdActivity);

    void inject(CreateSuccessActivity createSuccessActivity);

    void inject(WorksInfoActivity worksInfoActivity);

    void inject(UserInfoActivity userInfoActivity);

    void inject(RegisterStep1Frag registerStep1Frag);

    void inject(RegisterStep2Frag registerStep2Frag);

    void inject(RegisterStep3Frag registerStep3Frag);

    void inject(MLMFragment mlmFragment);

    void inject(QAFragment qaFragment);

    void inject(ContactsFragment contactsFragment);

    void inject(MeFragment meFragment);

    void inject(WorksListFragment worksListFragment);

    void inject(SendCodeFragment sendCodeFragment);

    void inject(SetNewPwdFragment setNewPwdFragment);
}
