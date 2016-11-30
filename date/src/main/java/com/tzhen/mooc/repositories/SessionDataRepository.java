package com.tzhen.mooc.repositories;

import android.content.Context;

import com.tongzhen.mooc.entities.LoginJson;
import com.tongzhen.mooc.entities.RegisterJson;
import com.tongzhen.mooc.entities.UserInfoJson;
import com.tongzhen.mooc.repositories.SessionRepository;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.mooc.storage.Persistence;
import com.tzhen.mooc.net.drupal.RestApi;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by russellgutierrez on 17/5/2016.
 */
public class SessionDataRepository implements SessionRepository {
    private final RestApi restApi;
    private Context context;
    private Persistence persistence;

    @Inject
    public SessionDataRepository(Context context, RestApi restApi, Persistence persistence) {
        this.context = context;
        this.restApi = restApi;
        this.persistence = persistence;
    }

    @Override
    public Observable<RegisterJson> register(String username, String password, String nickname, int sex, int country, String description) {
        return restApi.register(username, password, nickname, sex, country, description);
    }

    @Override
    public Observable<LoginJson> login(String username, String password) {
        return restApi.login(username, password);
    }

    @Override
    public Observable<BaseInfo> check_username(String username) {
        return restApi.check_username(username);
    }

    @Override
    public Observable<BaseInfo> check_nickname(String nickname) {
        return restApi.check_nickname(nickname);
    }

    @Override
    public Observable<UserInfoJson> my_info(String uid) {
        return restApi.my_info(uid);
    }
}