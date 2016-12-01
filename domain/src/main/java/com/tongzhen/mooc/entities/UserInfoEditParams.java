package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 16/12/1.
 */
public class UserInfoEditParams {

    private String head;
    private String nickname;
    private String name;
    private String email;
    private String description;
    private String sex;
    private String country;
    private String city;
    private String school;

    public UserInfoEditParams(Builder builder) {
        head = builder.head;
        nickname = builder.nickname;
        name = builder.name;
        email = builder.email;
        description = builder.description;
        sex = builder.sex;
        country = builder.country;
        city = builder.city;
        school = builder.school;
    }

    public String getHead() {
        return head;
    }

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getSex() {
        return sex;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getSchool() {
        return school;
    }

    public static class Builder{
        private String head;
        private String nickname;
        private String name;
        private String email;
        private String description;
        private String sex;
        private String country;
        private String city;
        private String school;

        public Builder setHead(String head) {
            this.head = head;
            return this;
        }

        public Builder setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setSex(String sex) {
            this.sex = sex;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setSchool(String school) {
            this.school = school;
            return this;
        }

        public UserInfoEditParams build(){
            return new UserInfoEditParams(this);
        }
    }
}
