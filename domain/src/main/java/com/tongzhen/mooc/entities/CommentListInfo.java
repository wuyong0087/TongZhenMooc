package com.tongzhen.mooc.entities;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class CommentListInfo extends BaseInfo {
    private List<CommentInfo> commentInfoList;

    public List<CommentInfo> getCommentInfoList() {
        return commentInfoList;
    }

    public void setCommentInfoList(List<CommentInfo> commentInfoList) {
        this.commentInfoList = commentInfoList;
    }
}
