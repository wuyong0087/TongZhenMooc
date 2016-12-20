package com.tongzhen.mooc.views;

import com.tongzhen.common.views.BaseView;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.UserInfo;

/**
 * Created by wuyong on 16/12/19.
 */
public interface UserInfoView extends BaseView<UserInfo> {
    void onFollow(BaseInfo baseInfo);
}
