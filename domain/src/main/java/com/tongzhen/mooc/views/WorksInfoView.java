package com.tongzhen.mooc.views;

import com.tongzhen.common.views.BaseView;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CommentListInfo;
import com.tongzhen.mooc.entities.WorksInfo;

/**
 * Created by wuyong on 16/12/19.
 */
public interface WorksInfoView extends BaseView<WorksInfo> {
    void onLoadedComments(CommentListInfo commentListInfo);

    void onCommentPost(BaseInfo baseInfo);
}
