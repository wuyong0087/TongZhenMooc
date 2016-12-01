package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CommentInfo;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class CommentListInfoJson extends BaseInfo {
    private List<CommentInfo> data;

    public List<CommentInfo> getData() {
        return data;
    }

    public void setData(List<CommentInfo> data) {
        this.data = data;
    }
}
