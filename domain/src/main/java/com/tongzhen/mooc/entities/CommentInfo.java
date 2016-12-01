package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 16/12/1.
 */
public class CommentInfo {
    private int id;
    private String uid;
    private String nickname;
    private String head;
    private int is_verify;
    private String content;
    private long addtime;

    public CommentInfo(Builder builder) {
        id = builder.id;
        uid = builder.uid;
        nickname = builder.nickname;
        head = builder.head;
        is_verify = builder.is_verify;
        content = builder.content;
        addtime = builder.addtime;
    }

    public int getId() {
        return id;
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

    public String getContent() {
        return content;
    }

    public long getAddtime() {
        return addtime;
    }

    public static class Builder{

        private int id;
        private String uid;
        private String nickname;
        private String head;
        private int is_verify;
        private String content;
        private long addtime;

        public Builder setId(int id) {
            this.id = id;
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

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setAddtime(long addtime) {
            this.addtime = addtime;
            return this;
        }

        public CommentInfo build(){
            return new CommentInfo(this);
        }
    }
}
