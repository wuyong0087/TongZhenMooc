package com.tongzhen.mooc.views;

import com.tongzhen.common.views.BaseView;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.ChatListInfo;

/**
 * Created by wuyong on 16/12/20.
 */
public interface ChatView extends BaseView<ChatListInfo> {
    void onSendChat(BaseInfo baseInfo);
}
