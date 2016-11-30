package com.tongzhen.mooc.repositories;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.LoginJson;
import com.tongzhen.mooc.entities.RegisterJson;
import com.tongzhen.mooc.entities.UserInfoJson;

import rx.Observable;

/**
 * Created by wuyong on 2016/11/26.
 */
public interface SessionRepository {
    Observable<RegisterJson> register(String username, String password, String nickname, int sex, int country, String description);

    Observable<LoginJson> login(String username, String password);

    Observable<BaseInfo> check_username(String username);

    Observable<BaseInfo> check_nickname(String nickname);

    Observable<UserInfoJson> my_info(String uid);
}
