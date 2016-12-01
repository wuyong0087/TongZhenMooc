package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.QuestionInfo;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class QuestionListInfoJson extends BaseInfo {
    private List<QuestionInfo> data;

    public List<QuestionInfo> getData() {
        return data;
    }

    public void setData(List<QuestionInfo> data) {
        this.data = data;
    }
}
