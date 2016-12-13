package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 16/12/1.
 */
public class RegisterInfo extends BaseInfo {

    private String username;
    private String nickname;
    private String uid;
    private String head;

    private RegisterInfo(Builder builder) {
        username = builder.username;
        nickname = builder.nickname;
        uid = builder.uid;
        head = builder.head;
        result = builder.result;
        errorMsg = builder.errorMsg;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUid() {
        return uid;
    }

    public String getHead() {
        return head;
    }

    public static class Builder{

        private String username;
        private String nickname;
        private String uid;
        private String head;
        private int result;
        private String errorMsg;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder setHead(String head) {
            this.head = head;
            return this;
        }

        public Builder setResult(int result) {
            this.result = result;
            return this;
        }

        public Builder setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
            return this;
        }

        public RegisterInfo build(){
            return new RegisterInfo(this);
        }

    }
}
