package com.tongzhen.mooc.entities;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class ChatListInfo extends BaseInfo {
    private List<ChatInfo> chatInfoList;

    public List<ChatInfo> getChatInfoList() {
        return chatInfoList;
    }

    public void setChatInfoList(List<ChatInfo> chatInfoList) {
        this.chatInfoList = chatInfoList;
    }
}
