package com.tongzhen.mooc.views;

import com.tongzhen.common.views.BaseView;
import com.tongzhen.mooc.entities.CourseIntroduceInfo;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.entities.WorksListInfo;

/**
 * Created by wuyong on 2016/12/21.
 */
public interface CourseInfoView extends BaseView<CourseIntroduceInfo> {
    void onGetWorks(WorksListInfo worksListInfo);

    void onGetQuestions(QuestionListInfo questionListInfo);
}
