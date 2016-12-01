package com.tongzhen.mooc.entities;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class WorksListInfo extends BaseInfo {
    private List<WorksInfo> worksInfoList;

    public List<WorksInfo> getWorksInfoList() {
        return worksInfoList;
    }

    public void setWorksInfoList(List<WorksInfo> worksInfoList) {
        this.worksInfoList = worksInfoList;
    }
}
