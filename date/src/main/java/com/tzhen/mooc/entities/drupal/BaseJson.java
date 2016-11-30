package com.tzhen.mooc.entities.drupal;

/**
 * Created by wuyong on 16/8/30.
 */
public class BaseJson {
    private String result;
    private String errorMsg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
