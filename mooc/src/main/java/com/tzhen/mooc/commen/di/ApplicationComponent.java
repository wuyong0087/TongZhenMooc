package com.tzhen.mooc.commen.di;

import com.tzhen.mooc.sign.LoginActivity;
import com.tzhen.mooc.sign.RegisterActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by wuyong on 2016/12/11.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(LoginActivity loginActivity);

    void inject(RegisterActivity registerActivity);
}
