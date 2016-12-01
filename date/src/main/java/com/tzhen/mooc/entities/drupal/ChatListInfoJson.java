package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.ChatInfo;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class ChatListInfoJson extends BaseInfo {
    private List<ChatInfo> data;

    public List<ChatInfo> getData() {
        return data;
    }

    public void setData(List<ChatInfo> data) {
        this.data = data;
    }
}
