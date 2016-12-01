package com.tongzhen.mooc.entities;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class CountryListInfo extends BaseInfo {
    private List<CountryInfo> countryInfoList;

    public List<CountryInfo> getCountryInfoList() {
        return countryInfoList;
    }

    public void setCountryInfoList(List<CountryInfo> countryInfoList) {
        this.countryInfoList = countryInfoList;
    }
}
