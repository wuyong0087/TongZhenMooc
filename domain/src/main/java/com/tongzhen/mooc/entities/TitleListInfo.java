package com.tongzhen.mooc.entities;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class TitleListInfo extends BaseInfo {
    private List<TitleInfo> subjectInfoList;

    public List<TitleInfo> getSubjectInfoList() {
        return subjectInfoList;
    }

    public void setSubjectInfoList(List<TitleInfo> subjectInfoList) {
        this.subjectInfoList = subjectInfoList;
    }
}