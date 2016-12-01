package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 16/12/1.
 */
public class ChatParams {
    private String uid;
    private String oid;
    private int type;
    private long addtime;
    private String content;
    private int vid;

    public ChatParams(Builder builder) {
        uid = builder.uid;
        oid = builder.oid;
        type = builder.type;
        addtime = builder.addtime;
        content = builder.content;
        vid = builder.vid;
    }

    public String getUid() {
        return uid;
    }

    public String getOid() {
        return oid;
    }

    public int getType() {
        return type;
    }

    public long getAddtime() {
        return addtime;
    }

    public String getContent() {
        return content;
    }

    public int getVid() {
        return vid;
    }

    public static class Builder{

        private String uid;
        private String oid;
        private int type;
        private long addtime;
        private String content;
        private int vid;

        public Builder setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder setOid(String oid) {
            this.oid = oid;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setAddtime(long addtime) {
            this.addtime = addtime;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setVid(int vid) {
            this.vid = vid;
            return this;
        }

        public ChatParams build(){
            return new ChatParams(this);
        }
    }
}
