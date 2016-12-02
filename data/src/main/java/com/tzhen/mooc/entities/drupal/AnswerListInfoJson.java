package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.AnswerInfo;
import com.tongzhen.mooc.entities.BaseInfo;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class AnswerListInfoJson extends BaseInfo {
    private List<AnswerInfo> data;

    public List<AnswerInfo> getData() {
        return data;
    }

    public void setData(List<AnswerInfo> data) {
        this.data = data;
    }
}
