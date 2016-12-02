package com.tzhen.mooc.entities.drupal;

import com.google.gson.annotations.SerializedName;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.RegisterInfo;

/**
 * Created by wuyong on 2016/11/26.
 */
public class RegisterJson extends BaseInfo {
    @SerializedName("data")
    private RegisterInfo data;

    public RegisterInfo getData() {
        return data;
    }
}
