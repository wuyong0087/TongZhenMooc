package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 16/12/1.
 */
public class UserVerifyParams {
    private String title;
    private String name;
    private String school;
    private String country;
    private String city;
    private String address;
    private String email;
    private String img;
    private String val;

    public UserVerifyParams(Builder builder) {
        title = builder.title;
        name = builder.name;
        school = builder.school;
        country = builder.country;
        city = builder.city;
        address = builder.address;
        email = builder.email;
        img = builder.img;
        val = builder.val;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getSchool() {
        return school;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getImg() {
        return img;
    }

    public String getVal() {
        return val;
    }

    public static class Builder{

        private String title;
        private String name;
        private String school;
        private String country;
        private String city;
        private String address;
        private String email;
        private String img;
        private String val;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSchool(String school) {
            this.school = school;
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

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setImg(String img) {
            this.img = img;
            return this;
        }

        public Builder setVal(String val) {
            this.val = val;
            return this;
        }

        public UserVerifyParams build(){
            return new UserVerifyParams(this);
        }
    }
}
