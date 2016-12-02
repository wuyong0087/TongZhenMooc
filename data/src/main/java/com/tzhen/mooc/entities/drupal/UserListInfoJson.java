package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.UserInfo;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class UserListInfoJson extends BaseInfo {
    private List<UserInfo> data;

    public List<UserInfo> getData() {
        return data;
    }

    public void setData(List<UserInfo> data) {
        this.data = data;
    }
}
