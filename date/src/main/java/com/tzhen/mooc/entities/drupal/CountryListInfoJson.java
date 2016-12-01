package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CountryInfo;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class CountryListInfoJson extends BaseInfo {
    private List<CountryInfo> data;

    public List<CountryInfo> getData() {
        return data;
    }

    public void setData(List<CountryInfo> data) {
        this.data = data;
    }
}
