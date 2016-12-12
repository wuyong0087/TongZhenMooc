package com.tzhen.commen.application;

import android.app.Application;
import android.util.Log;

import com.tzhen.commen.di.ApplicationComponent;
import com.tzhen.commen.di.ApplicationModule;
import com.tzhen.commen.di.DaggerApplicationComponent;
import com.tzhen.mooc.internal.di.DataModule;
import com.tzhen.mooc.net.drupal.ConfigEndpoints;

public class AndroidApplication extends Application {
    private static final String TAG = AndroidApplication.class.getSimpleName();
    private ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();
        this.initializeInjector();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .dataModule(new DataModule(ConfigEndpoints.BASE_URL))
                .build();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
                Log.e(TAG,ex.getMessage());
            }
        });

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}