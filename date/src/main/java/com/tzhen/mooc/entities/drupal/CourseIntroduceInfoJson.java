package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CourseIntroduceInfo;

/**
 * Created by wuyong on 16/12/1.
 */
public class CourseIntroduceInfoJson extends BaseInfo {
    private CourseIntroduceInfo data;

    public CourseIntroduceInfo getData() {
        return data;
    }

    public void setData(CourseIntroduceInfo data) {
        this.data = data;
    }
}
