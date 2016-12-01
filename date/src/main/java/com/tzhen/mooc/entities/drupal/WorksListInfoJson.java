package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.WorksInfo;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class WorksListInfoJson extends BaseInfo {
    private List<WorksInfo> data;

    public void setData(List<WorksInfo> data) {
        this.data = data;
    }

    public List<WorksInfo> getData() {
        return data;
    }
}
