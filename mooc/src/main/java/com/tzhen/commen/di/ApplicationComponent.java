package com.tzhen.commen.di;

import com.tzhen.mooc.activities.ChatActivity;
import com.tzhen.mooc.activities.CourseCenterActivity;
import com.tzhen.mooc.activities.CourseInfoActivity;
import com.tzhen.mooc.activities.ForgotPwdActivity;
import com.tzhen.mooc.activities.LaunchActivity;
import com.tzhen.mooc.activities.LoginActivity;
import com.tzhen.mooc.activities.MainActivity;
import com.tzhen.mooc.activities.MyProFileActivity;
import com.tzhen.mooc.activities.OpenNewCourseActivity;
import com.tzhen.mooc.activities.SignUpActivity;
import com.tzhen.mooc.activities.UserInfoActivity;
import com.tzhen.mooc.activities.VerificationActivity;
import com.tzhen.mooc.activities.WorksInfoActivity;
import com.tzhen.mooc.countrylist.CountryListActivity;
import com.tzhen.mooc.fragments.QAListFragment;
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
import com.tzhen.mooc.main.mlm.FeaturedWorksListFrag;
import com.tzhen.mooc.main.mlm.MyCollectionWorksListFrag;
import com.tzhen.mooc.main.mlm.SharedWorksListFrag;
import com.tzhen.mooc.main.q_a.ListedQAListFrag;

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

    void inject(CourseCenterActivity courseCenterActivity);

    void inject(CourseInfoActivity courseInfoActivity);

    void inject(CountryListActivity countryListActivity);

    void inject(MyProFileActivity myProFileActivity);

    void inject(OpenNewCourseActivity openNewCourseActivity);

    void inject(VerificationActivity verificationActivity);

    void inject(ChatActivity chatActivity);

    void inject(RegisterStep1Frag registerStep1Frag);

    void inject(RegisterStep2Frag registerStep2Frag);

    void inject(RegisterStep3Frag registerStep3Frag);

    void inject(MLMFragment mlmFragment);

    void inject(QAFragment qaFragment);

    void inject(ContactsFragment contactsFragment);

    void inject(MeFragment meFragment);

    void inject(FeaturedWorksListFrag featuredWorksListFrag);

    void inject(SharedWorksListFrag sharedWorksListFrag);

    void inject(MyCollectionWorksListFrag myCollectionWorksListFrag);

    void inject(QAListFragment qaListFragment);

    void inject(SendCodeFragment sendCodeFragment);

    void inject(SetNewPwdFragment setNewPwdFragment);

    void inject(ListedQAListFrag listedQAListFrag);
}
