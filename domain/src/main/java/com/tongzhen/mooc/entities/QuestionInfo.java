package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 16/12/1.
 */
public class QuestionInfo extends BaseInfo {
    private String qid;
    private String question;
    private String img;
    private String uid;
    private String nickname;
    private String head;
    private int is_verify;
    private String course_title;
    private int cid;
    private int watch;
    private int writing;
    private int videos;
    private long addtime;

    public QuestionInfo(Builder builder) {
        qid = builder.qid;
        question = builder.question;
        img = builder.img;
        uid = builder.uid;
        nickname = builder.nickname;
        head = builder.head;
        is_verify = builder.is_verify;
        course_title = builder.course_title;
        cid = builder.cid;
        watch = builder.watch;
        writing = builder.writing;
        videos = builder.videos;
        addtime = builder.addtime;
    }

    public String getQid() {
        return qid;
    }

    public String getQuestion() {
        return question;
    }

    public String getImg() {
        return img;
    }

    public String getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHead() {
        return head;
    }

    public int getIs_verify() {
        return is_verify;
    }

    public String getCourse_title() {
        return course_title;
    }

    public int getCid() {
        return cid;
    }

    public int getWatch() {
        return watch;
    }

    public int getWriting() {
        return writing;
    }

    public int getVideos() {
        return videos;
    }

    public long getAddtime() {
        return addtime;
    }

    public static class Builder{

        private String qid;
        private String question;
        private String img;
        private String uid;
        private String nickname;
        private String head;
        private int is_verify;
        private String course_title;
        private int cid;
        private int watch;
        private int writing;
        private int videos;
        private long addtime;

        public Builder setQid(String qid) {
            this.qid = qid;
            return this;
        }

        public Builder setQuestion(String question) {
            this.question = question;
            return this;
        }

        public Builder setImg(String img) {
            this.img = img;
            return this;
        }

        public Builder setUid(String uid) {
            this.uid = uid;
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

        public Builder setIs_verify(int is_verify) {
            this.is_verify = is_verify;
            return this;
        }

        public Builder setCourse_title(String course_title) {
            this.course_title = course_title;
            return this;
        }

        public Builder setCid(int cid) {
            this.cid = cid;
            return this;
        }

        public Builder setWatch(int watch) {
            this.watch = watch;
            return this;
        }

        public Builder setWriting(int writing) {
            this.writing = writing;
            return this;
        }

        public Builder setVideos(int videos) {
            this.videos = videos;
            return this;
        }

        public Builder setAddtime(long addtime) {
            this.addtime = addtime;
            return this;
        }

        public QuestionInfo build(){
            return new QuestionInfo(this);
        }
    }
}
