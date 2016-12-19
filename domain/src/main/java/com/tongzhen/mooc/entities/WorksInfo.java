package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 16/12/1.
 */
public class WorksInfo extends BaseInfo {
    private int vid;
    private String uid;
    private String nickname;
    private String head;
    private int is_verify;
    private String kind;
    private String type;
    private int pages;
    private String title;
    private long duration;
    private long createtime;
    private long updatetime;
    private String tags;
    private String versions;
    private long addtime;
    private String screenshot;
    private String screenshot_m;
    private String screenshot_s;
    private String downurl;
    private String weburl;
    private int share;
    private int comments;
    private int praise;
    private int is_collection;
    private int is_praise;
    private int is_shared;
    private int is_featured;
    private String course_title;
    private int cid;
    private String[] files;
    private String reason;

    public WorksInfo(){}

    public WorksInfo(Builder builder) {
        vid = builder.vid;
        uid = builder.uid;
        nickname = builder.nickname;
        head = builder.head;
        is_verify = builder.is_verify;
        kind = builder.kind;
        type = builder.type;
        pages = builder.pages;
        title = builder.title;
        duration = builder.duration;
        createtime = builder.createtime;
        updatetime = builder.updatetime;
        tags = builder.tags;
        versions = builder.versions;
        addtime = builder.addtime;
        screenshot = builder.screenshot;
        screenshot_m = builder.screenshot_m;
        screenshot_s = builder.screenshot_s;
        downurl = builder.downurl;
        weburl = builder.weburl;
        share = builder.share;
        comments = builder.comments;
        praise = builder.praise;
        is_collection = builder.is_collection;
        is_praise = builder.is_praise;
        is_shared = builder.is_shared;
        is_featured = builder.is_featured;
        course_title = builder.course_title;
        cid = builder.cid;
        files = builder.files;
        reason = builder.reason;
    }

    public int getVid() {
        return vid;
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

    public String getKind() {
        return kind;
    }

    public String getType() {
        return type;
    }

    public int getPages() {
        return pages;
    }

    public String getTitle() {
        return title;
    }

    public long getDuration() {
        return duration;
    }

    public long getCreatetime() {
        return createtime;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public String getTags() {
        return tags;
    }

    public String getVersions() {
        return versions;
    }

    public long getAddtime() {
        return addtime;
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

    public String getDownurl() {
        return downurl;
    }

    public String getWeburl() {
        return weburl;
    }

    public int getShare() {
        return share;
    }

    public int getComments() {
        return comments;
    }

    public int getPraise() {
        return praise;
    }

    public int getIs_collection() {
        return is_collection;
    }

    public int getIs_praise() {
        return is_praise;
    }

    public int getIs_shared() {
        return is_shared;
    }

    public int getIs_featured() {
        return is_featured;
    }

    public String getCourse_title() {
        return course_title;
    }

    public int getCid() {
        return cid;
    }

    public String[] getFiles() {
        return files;
    }

    public String getReason() {
        return reason;
    }

    public static class Builder{

        private int vid;
        private String uid;
        private String nickname;
        private String head;
        private int is_verify;
        private String kind;
        private String type;
        private int pages;
        private String title;
        private long duration;
        private long createtime;
        private long updatetime;
        private String tags;
        private String versions;
        private long addtime;
        private String screenshot;
        private String screenshot_m;
        private String screenshot_s;
        private String downurl;
        private String weburl;
        private int share;
        private int comments;
        private int praise;
        private int is_collection;
        private int is_praise;
        private int is_shared;
        private int is_featured;
        private String course_title;
        private int cid;
        private String[] files;
        private String reason;

        public Builder setVid(int vid) {
            this.vid = vid;
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

        public Builder setKind(String kind) {
            this.kind = kind;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setPages(int pages) {
            this.pages = pages;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder setCreatetime(long createtime) {
            this.createtime = createtime;
            return this;
        }

        public Builder setUpdatetime(long updatetime) {
            this.updatetime = updatetime;
            return this;
        }

        public Builder setTags(String tags) {
            this.tags = tags;
            return this;
        }

        public Builder setVersions(String versions) {
            this.versions = versions;
            return this;
        }

        public Builder setAddtime(long addtime) {
            this.addtime = addtime;
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

        public Builder setDownurl(String downurl) {
            this.downurl = downurl;
            return this;
        }

        public Builder setWeburl(String weburl) {
            this.weburl = weburl;
            return this;
        }

        public Builder setShare(int share) {
            this.share = share;
            return this;
        }

        public Builder setComments(int comments) {
            this.comments = comments;
            return this;
        }

        public Builder setPraise(int praise) {
            this.praise = praise;
            return this;
        }

        public Builder setIs_collection(int is_collection) {
            this.is_collection = is_collection;
            return this;
        }

        public Builder setIs_praise(int is_praise) {
            this.is_praise = is_praise;
            return this;
        }

        public Builder setIs_shared(int is_shared) {
            this.is_shared = is_shared;
            return this;
        }

        public Builder setIs_featured(int is_featured) {
            this.is_featured = is_featured;
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

        public Builder setFiles(String[] files) {
            this.files = files;
            return this;
        }

        public Builder setReason(String reason) {
            this.reason = reason;
            return this;
        }

        public WorksInfo build(){
            return new WorksInfo(this);
        }
    }
}
