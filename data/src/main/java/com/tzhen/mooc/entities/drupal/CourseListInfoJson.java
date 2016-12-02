package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CourseInfo;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class CourseListInfoJson extends BaseInfo {
    private List<CourseInfo> data;

    public List<CourseInfo> getData() {
        return data;
    }

    public void setData(List<CourseInfo> data) {
        this.data = data;
    }
}
