package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 16/12/1.
 */
public class CourseEditParams {
    private String uid;
    private String course_title;
    private String cover;
    private String description;

    public CourseEditParams(Builder builder) {
        uid = builder.uid;
        course_title = builder.course_title;
        cover = builder.cover;
        description = builder.description;
    }

    public String getUid() {
        return uid;
    }

    public String getCourse_title() {
        return course_title;
    }

    public String getCover() {
        return cover;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder{

        private String uid;
        private String course_title;
        private String cover;
        private String description;

        public Builder setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder setCourse_title(String course_title) {
            this.course_title = course_title;
            return this;
        }

        public Builder setCover(String cover) {
            this.cover = cover;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public CourseEditParams build(){
            return new CourseEditParams(this);
        }
    }
}
