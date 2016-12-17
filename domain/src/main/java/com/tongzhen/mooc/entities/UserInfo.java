package com.tongzhen.mooc.entities;

import java.util.List;

/**
 * Created by wuyong on 16/12/1.
 */
public class UserInfo extends BaseInfo {
    private String uid;
    private String username;
    private String nickname;
    private String head;
    private String name;
    private String email;
    private String description;
    private int sex;
    private int score;
    private int country;
    private String city;
    private String school;
    private String address;
    private int is_verify;
    private int follows;
    private int fans;
    private int chat_number;
    private int is_friends;
    private int is_fans;
    private int is_follows;
    private int is_others;
    private List<WorksInfo> videos;

    private UserInfo(Builder builder) {
        uid = builder.uid;
        username = builder.username;
        nickname = builder.nickname;
        head = builder.head;
        name = builder.name;
        email = builder.email;
        description = builder.description;
        sex = builder.sex;
        score = builder.score;
        country = builder.country;
        city = builder.city;
        school = builder.school;
        address = builder.address;
        is_verify = builder.is_verify;
        follows = builder.follows;
        fans = builder.fans;
        chat_number = builder.chat_number;
        is_fans = builder.is_fans;
        is_friends = builder.is_friends;
        is_follows = builder.is_follows;
        is_others = builder.is_others;
        videos = builder.videos;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHead() {
        return head;
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

    public int getSex() {
        return sex;
    }

    public int getScore() {
        return score;
    }

    public int getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getSchool() {
        return school;
    }

    public String getAddress() {
        return address;
    }

    public int getIs_verify() {
        return is_verify;
    }

    public int getFollows() {
        return follows;
    }

    public int getFans() {
        return fans;
    }

    public int getChat_number() {
        return chat_number;
    }

    public int getIs_friends() {
        return is_friends;
    }

    public int getIs_fans() {
        return is_fans;
    }

    public int getIs_follows() {
        return is_follows;
    }

    public int getIs_others() {
        return is_others;
    }

    public List<WorksInfo> getVideos() {
        return videos;
    }


    public static class Builder{

        private String uid;
        private String username;
        private String nickname;
        private String head;
        private String name;
        private String email;
        private String description;
        private int sex;
        private int score;
        private int country;
        private String city;
        private String school;
        private String address;
        private int is_verify;
        private String result;
        private String errorMsg;
        private int follows;
        private int fans;
        private int chat_number;
        private int is_friends;
        private int is_fans;
        private int is_follows;
        private int is_others;
        private List<WorksInfo> videos;

        public Builder setIs_friends(int is_friends) {
            this.is_friends = is_friends;
            return this;
        }

        public Builder setIs_fans(int is_fans) {
            this.is_fans = is_fans;
            return this;
        }

        public Builder setIs_follows(int is_follows) {
            this.is_follows = is_follows;
            return this;
        }

        public Builder setIs_others(int is_others) {
            this.is_others = is_others;
            return this;
        }

        public Builder setVideos(List<WorksInfo> videos) {
            this.videos = videos;
            return this;
        }

        public Builder setFollows(int follows) {
            this.follows = follows;
            return this;
        }

        public Builder setFans(int fans) {
            this.fans = fans;
            return this;
        }

        public Builder setResult(String result) {
            this.result = result;
            return this;
        }

        public Builder setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
            return this;
        }

        public Builder setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder setHead(String head) {
            this.head = head;
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

        public Builder setSex(int sex) {
            this.sex = sex;
            return this;
        }

        public Builder setScore(int score) {
            this.score = score;
            return this;
        }

        public Builder setCountry(int country) {
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

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setIs_verify(int is_verify) {
            this.is_verify = is_verify;
            return this;
        }

        public Builder setChat_number(int chat_number) {
            this.chat_number = chat_number;
            return this;
        }

        public UserInfo build(){
            return new UserInfo(this);
        }

    }
}
