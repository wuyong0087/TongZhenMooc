package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.UserInfo;

/**
 * Created by wuyong on 2016/11/26.
 */
public class LoginJson extends BaseInfo {
    private UserInfo data;

    public UserInfo getData() {
        return data;
    }
}
