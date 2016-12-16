package com.tongzhen.common.presenters;

import com.tongzhen.mooc.entities.UserInfo;

/**
 * Created by wuyong on 2016/11/26.
 */
public interface UserSession {

    String getUid();

    String getEmail();

    UserInfo getUserInfo();

    void setUserInfo(UserInfo userInfo);
}
