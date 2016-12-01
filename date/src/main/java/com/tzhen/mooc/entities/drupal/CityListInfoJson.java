package com.tzhen.mooc.entities.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CityInfo;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class CityListInfoJson extends BaseInfo {
    private List<CityInfo> data;

    public List<CityInfo> getData() {
        return data;
    }

    public void setData(List<CityInfo> data) {
        this.data = data;
    }
}
