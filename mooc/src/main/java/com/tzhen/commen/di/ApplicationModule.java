package com.tzhen.commen.di;

import android.content.Context;

import com.tongzhen.common.presenters.UserSession;
import com.tongzhen.common.schedulers.ObserveOn;
import com.tongzhen.common.schedulers.SubscribeOn;
import com.tongzhen.mooc.repositories.SessionRepository;
import com.tzhen.commen.application.AndroidApplication;
import com.tzhen.mooc.entities.UserSessionImp;
import com.tzhen.mooc.internal.di.DataModule;
import com.tzhen.mooc.navigator.Navigator;
import com.tzhen.mooc.repositories.SessionDataRepository;
import com.tzhen.mooc.storage.Persistence;

import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuyong on 2016/12/11.
 */
@Module(includes = {DataModule.class})
public class ApplicationModule {
    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton public SessionRepository provideSessionRepository(SessionDataRepository sessionDataRepository) {
        return sessionDataRepository;
    }

    @Singleton @Provides
    Navigator provideNavigator() {
        return new Navigator();
    }

    @Singleton @Provides
    SubscribeOn provideSubscribeOn() {
        return (new SubscribeOn() {
            @Override public Scheduler getScheduler() {
                return Schedulers.newThread();
            }
        });
    }

    @Singleton @Provides
    ObserveOn provideObserveOn() {
        return (new ObserveOn() {
            @Override public Scheduler getScheduler() {
                return AndroidSchedulers.mainThread();
            }
        });
    }

    @Singleton @Provides
    MoneyFormatter provideMoneyFormatter() {
        return new MoneyFormatterBuilder().appendCurrencySymbolLocalized().appendAmount().toFormatter();
    }

    @Singleton @Provides
    DateTimeFormatter provideDateTimeFormatter() {
        return DateTimeFormat.mediumDateTime().withLocale(Locale.getDefault());
    }

    @Provides
    UserSession providesUserSession(Context context, Persistence persistence) {

        UserSessionImp userSession = new UserSessionImp();
        return userSession;
    }
}
