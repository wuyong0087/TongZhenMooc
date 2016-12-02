package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CourseEditInfo;

/**
 * Created by wuyong on 16/12/1.
 */
public class CourseEditInfoJson extends BaseInfo {
    private CourseEditInfo data;

    public CourseEditInfo getData() {
        return data;
    }

    public void setData(CourseEditInfo data) {
        this.data = data;
    }
}
