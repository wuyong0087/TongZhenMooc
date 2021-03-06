package com.tzhen.mooc.entities;

import com.tongzhen.common.presenters.UserSession;
import com.tongzhen.mooc.entities.UserInfo;

/**
 * Implementation class for the UserSession interface
 */
public class UserSessionImp implements UserSession {
    private String userId;
    private String email;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUid() {
        return userId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public UserInfo getUserInfo() {
        return null;
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {

    }
}
