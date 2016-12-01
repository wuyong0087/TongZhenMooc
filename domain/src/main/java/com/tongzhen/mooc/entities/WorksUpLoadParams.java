package com.tongzhen.mooc.entities;

/**
 * Created by wuyong on 16/12/1.
 */
public class WorksUpLoadParams {
    private String type;
    private String files;
    private String zipfile;
    private String title;
    private long duration;
    private long createtime;
    private String versions;
    private String tags;
    private String pages;
    private String old_vid;

    private WorksUpLoadParams(Builder builder) {
        type = builder.type;
        files  =builder.files;
        zipfile = builder.zipfile;
        title = builder.title;
        duration = builder.duration;
        createtime = builder.createtime;
        versions = builder.versions;
        tags = builder.tags;
        pages = builder.pages;
        old_vid = builder.old_vid;
    }

    public String getType() {
        return type;
    }

    public String getFiles() {
        return files;
    }

    public String getZipfile() {
        return zipfile;
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

    public String getVersions() {
        return versions;
    }

    public String getTags() {
        return tags;
    }

    public String getPages() {
        return pages;
    }

    public String getOld_vid() {
        return old_vid;
    }

    public static class Builder{

        private String type;
        private String files;
        private String zipfile;
        private String title;
        private long duration;
        private long createtime;
        private String versions;
        private String tags;
        private String pages;
        private String old_vid;

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setFiles(String files) {
            this.files = files;
            return this;
        }

        public Builder setZipfile(String zipfile) {
            this.zipfile = zipfile;
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

        public Builder setVersions(String versions) {
            this.versions = versions;
            return this;
        }

        public Builder setTags(String tags) {
            this.tags = tags;
            return this;
        }

        public Builder setPages(String pages) {
            this.pages = pages;
            return this;
        }

        public Builder setOld_vid(String old_vid) {
            this.old_vid = old_vid;
            return this;
        }

        public WorksUpLoadParams build(){
            return new WorksUpLoadParams(this);
        }
    }
}
