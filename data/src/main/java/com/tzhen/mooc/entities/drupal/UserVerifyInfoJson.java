package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.UserVerifyInfo;

/**
 * Created by wuyong on 16/12/1.
 */
public class UserVerifyInfoJson extends BaseInfo {
    private UserVerifyInfo data;

    public UserVerifyInfo getData() {
        return data;
    }

    public void setData(UserVerifyInfo data) {
        this.data = data;
    }
}
