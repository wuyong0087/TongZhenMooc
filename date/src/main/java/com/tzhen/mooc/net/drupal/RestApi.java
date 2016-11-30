package com.tzhen.mooc.net.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.LoginJson;
import com.tongzhen.mooc.entities.RegisterJson;
import com.tongzhen.mooc.entities.UserInfoJson;
import com.tzhen.mooc.storage.Persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;

public class RestApi {
    private Map<String, String> encryptParams;

    private final Endpoints endpoints;
    private final Persistence persistence;
    private List<String> paramParies;

    @Inject
    public RestApi(Endpoints endpoints, Persistence persistence) {
        this.endpoints = endpoints;
        this.persistence = persistence;
        encryptParams = new HashMap<>();
        paramParies = new ArrayList<>();
    }

    private boolean notEmpty(String param) {
        if (param == null || "".equals(param)) {
            return false;
        }

        return true;
    }

    public Observable<LoginJson> login(String username, String password) {

        return endpoints.login(username, password, encryptParams());
    }

    public Observable<UserInfoJson> my_info(String uid) {

        return endpoints.my_info(uid, encryptParams());
    }

    public Map<String, String> encryptParams() {
        encryptParams.put("kind", "android");
        encryptParams.put("version", "0.1");

        return encryptParams;
    }

    private Map<String, RequestBody> getEncryptParts() {
        Map<String, RequestBody> bodys = new HashMap<>();

        bodys.put("kind", RequestBody.create(null, "android"));
        bodys.put("version", RequestBody.create(null, "0.1"));

        return bodys;
    }

    public Observable<RegisterJson> register(String username, String password, String nickname, int sex, int country, String description) {

        return endpoints.register(username, password, nickname, sex, country, description, encryptParams());
    }

    public Observable<BaseInfo> check_username(String username) {

        return endpoints.check_username(username, encryptParams());
    }

    public Observable<BaseInfo> check_nickname(String nickname) {

        return endpoints.check_nickname(nickname, encryptParams());
    }
}
