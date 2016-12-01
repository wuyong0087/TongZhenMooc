package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 16/12/1.
 */
public class AnswerInfo {
    private int aid;
    private String uid;
    private String nickname;
    private String head;
    private int is_verify;
    private long addtime;
    private int help;
    private int is_help;
    private int atype;
    private String content;
    private int vid;
    private String type;
    private String screenshot;
    private String screenshot_m;
    private String screenshot_s;
    private long duration;
    private int pages;
    private String downurl;
    private String weburl;

    public AnswerInfo(Builder builder) {
        aid = builder.aid;
        uid = builder.uid;
        nickname = builder.nickname;
        head = builder.head;
        is_verify = builder.is_verify;
        addtime = builder.addtime;
        help = builder.help;
        is_help = builder.is_help;
        atype = builder.atype;
        content = builder.content;
        vid = builder.vid;
        type = builder.type;
        screenshot = builder.screenshot;
        screenshot_m = builder.screenshot_m;
        screenshot_s = builder.screenshot_s;
        duration = builder.duration;
        pages = builder.pages;
        downurl = builder.downurl;
        weburl = builder.weburl;
    }

    public int getAid() {
        return aid;
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

    public long getAddtime() {
        return addtime;
    }

    public int getHelp() {
        return help;
    }

    public int getIs_help() {
        return is_help;
    }

    public int getAtype() {
        return atype;
    }

    public String getContent() {
        return content;
    }

    public int getVid() {
        return vid;
    }

    public String getType() {
        return type;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public String getScreenshot_m() {
        return screenshot_m;
    }

    public String getScreenshot_s() {
        return screenshot_s;
    }

    public long getDuration() {
        return duration;
    }

    public int getPages() {
        return pages;
    }

    public String getDownurl() {
        return downurl;
    }

    public String getWeburl() {
        return weburl;
    }

    public static class Builder{

        private int aid;
        private String uid;
        private String nickname;
        private String head;
        private int is_verify;
        private long addtime;
        private int help;
        private int is_help;
        private int atype;
        private String content;
        private int vid;
        private String type;
        private String screenshot;
        private String screenshot_m;
        private String screenshot_s;
        private long duration;
        private int pages;
        private String downurl;
        private String weburl;

        public Builder setAid(int aid) {
            this.aid = aid;
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

        public Builder setAddtime(long addtime) {
            this.addtime = addtime;
            return this;
        }

        public Builder setHelp(int help) {
            this.help = help;
            return this;
        }

        public Builder setIs_help(int is_help) {
            this.is_help = is_help;
            return this;
        }

        public Builder setAtype(int atype) {
            this.atype = atype;
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

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setScreenshot(String screenshot) {
            this.screenshot = screenshot;
            return this;
        }

        public Builder setScreenshot_m(String screenshot_m) {
            this.screenshot_m = screenshot_m;
            return this;
        }

        public Builder setScreenshot_s(String screenshot_s) {
            this.screenshot_s = screenshot_s;
            return this;
        }

        public Builder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder setPages(int pages) {
            this.pages = pages;
            return this;
        }

        public Builder setDownurl(String downurl) {
            this.downurl = downurl;
            return this;
        }

        public Builder setWeburl(String weburl) {
            this.weburl = weburl;
            return this;
        }

        public AnswerInfo build(){
            return new AnswerInfo(this);
        }
    }
}
