package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.SubjectInfo;
import com.tongzhen.mooc.entities.SubjectListInfo;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class SubjectListInfoJson extends BaseInfo {
    private List<SubjectInfo> data;

    public List<SubjectInfo> getData() {
        return data;
    }

    public void setData(List<SubjectInfo> data) {
        this.data = data;
    }
}
