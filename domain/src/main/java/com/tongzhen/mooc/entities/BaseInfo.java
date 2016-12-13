package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 2016/11/26.
 */
public class BaseInfo {
    protected int result;
    protected String errorMsg;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
