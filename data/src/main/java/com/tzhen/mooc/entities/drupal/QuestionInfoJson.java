package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.QuestionInfo;

/**
 * Created by wuyong on 16/12/1.
 */
public class QuestionInfoJson extends BaseInfo {
    private QuestionInfo data;

    public QuestionInfo getData() {
        return data;
    }

    public void setData(QuestionInfo data) {
        this.data = data;
    }
}
