package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.WorksInfo;

/**
 * Created by wuyong on 16/12/1.
 */
public class WorksInfoJson extends BaseInfo {
    private WorksInfo data;

    public WorksInfo getData() {
        return data;
    }

    public void setData(WorksInfo data) {
        this.data = data;
    }
}
