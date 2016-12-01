package com.tongzhen.mooc.entities;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class AnswerListInfo extends BaseInfo {
    private List<AnswerInfo> answerInfoList;

    public List<AnswerInfo> getAnswerInfoList() {
        return answerInfoList;
    }

    public void setAnswerInfoList(List<AnswerInfo> answerInfoList) {
        this.answerInfoList = answerInfoList;
    }
}
