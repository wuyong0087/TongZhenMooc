package com.tzhen.commen.di;

import com.tzhen.mooc.activities.LoginActivity;
import com.tzhen.mooc.activities.MainActivity;
import com.tzhen.mooc.activities.RegisterActivity;
import com.tzhen.mooc.fragments.RegisterStep1Frag;
import com.tzhen.mooc.fragments.RegisterStep2Frag;
import com.tzhen.mooc.fragments.RegisterStep3Frag;
import com.tzhen.mooc.main.ContactsFragment;
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

    void inject(RegisterActivity registerActivity);

    void inject(RegisterStep1Frag registerStep1Frag);

    void inject(RegisterStep2Frag registerStep2Frag);

    void inject(RegisterStep3Frag registerStep3Frag);

    void inject(MLMFragment mlmFragment);

    void inject(QAFragment qaFragment);

    void inject(ContactsFragment contactsFragment);

    void inject(MeFragment meFragment);

    void inject(WorksListFragment worksListFragment);
}
