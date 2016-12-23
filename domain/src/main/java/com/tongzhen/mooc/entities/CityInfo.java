package com.tongzhen.mooc.entities;

import java.io.Serializable;

/**
 * Created by wuyong on 16/12/1.
 */
public class CityInfo implements Serializable {
    private String id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
