package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 16/12/1.
 */
public class CourseEditInfo extends BaseInfo {
    private int cid;
    private String course_title;
    private String cover;
    private String description;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
