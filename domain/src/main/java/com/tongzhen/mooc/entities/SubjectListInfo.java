package com.tongzhen.mooc.entities;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class SubjectListInfo extends BaseInfo {
    private List<SubjectInfo> subjectInfoList;

    public List<SubjectInfo> getSubjectInfoList() {
        return subjectInfoList;
    }

    public void setSubjectInfoList(List<SubjectInfo> subjectInfoList) {
        this.subjectInfoList = subjectInfoList;
    }
}