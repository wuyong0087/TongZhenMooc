package com.tzhen.mooc.events;

import com.tongzhen.mooc.entities.CountryInfo;

/**
 * Created by wuyong on 16/12/23.
 */
public class CountryEvent {
    private CountryInfo info;

    public CountryEvent(CountryInfo info) {
        this.info = info;
    }

    public CountryInfo getInfo() {
        return info;
    }

    public void setInfo(CountryInfo info) {
        this.info = info;
    }
}
