package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.SubjectInfo;
import com.tongzhen.mooc.entities.SubjectListInfo;
import com.tongzhen.mooc.entities.TitleInfo;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class TitleListInfoJson extends BaseInfo {
    private List<TitleInfo> data;

    public List<TitleInfo> getData() {
        return data;
    }

    public void setData(List<TitleInfo> data) {
        this.data = data;
    }
}
