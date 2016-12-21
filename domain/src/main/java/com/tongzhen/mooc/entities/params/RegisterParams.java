package com.tongzhen.mooc.entities.params;

/**
 * Created by wuyong on 16/12/21.
 */
public class RegisterParams {
    private String username;
    private String password;
    private String nickname;
    private int sex = -1;
    private int country;
    private String description;

    public RegisterParams(Builder builder) {
        username = builder.username;
        password = builder.password;
        nickname = builder.nickname;
        sex = builder.sex;
        country = builder.country;
        description = builder.description;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public int getSex() {
        return sex;
    }

    public int getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder{
        private String username;
        private String password;
        private String nickname;
        private int sex = -1;
        private int country;
        private String description;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder setSex(int sex) {
            this.sex = sex;
            return this;
        }

        public Builder setCountry(int country) {
            this.country = country;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public RegisterParams build(){
            return new RegisterParams(this);
        }
    }
}
