package com.lib.play_rec.entity;

import java.io.Serializable;

public class WorksBean implements Serializable {
	
	public static boolean isDownBaseOver=false;
	
	private static final long serialVersionUID = -1435474973654121572L;
	
	private String id;  // 视频id
	private String netId;  // 上传后对应服务器的id  仅用于个人视频
	private String uid;  // 用户id
	private String videoName="";  // 视频名称
	private String videoDesc="";  // 视频描述
	private String videoTags;  // 视频关键字
	private String permission;  // 视频公开权限
	private String videoPath;  // 视频路径
	private String videoVoice="";  // 声音文件名
	private String videoText="";  // 视频文本
	private String sumTime;  // 录制时间
	private String files;  // 文件名称列表，用 ","号隔开
	private String videoType;//视频类型
	
	private String gradeId;  // 年段ID
	private String subjectId;  // 学科ID
	private String knowFirstId; // 一级知识点id
	private String knowSecondId;  // 二级知识点id
	private String knowThirdId;  // 三级知识点id
	
	private String noteImage;// 保存笔记图片
	private String screenshotUrl;  // 视频缩略图下载地址(大图)
	private String screenshotUrlMedium;  // 视频缩略图下载地址(中图)
	private String screenshotUrlSmall;   // 视频缩略图下载地址(小图)
	
	private String weburl; // 网页端播放地址
	private String zipUrl;  // zip下载地址
	private int isDownLoad;  // 是否已下载   0-未下载   1-已下载
	private int isUpLoad; // 是否已上传 0-未上传 1-已上传
	
	private String userName;  // 用户名
	private String headUrl;  // 用户头像下载地址
	private String headName;  // 头像在本地的名称
	
	private String createTime;  // 创建时间
	private String updateTime;  // 更新时间
	private int order;  // 排序
	private int pages; //　讲义的页数
	// 作品的宽高
	private double worksH;
	private double worksW;
	
	public double getWorksH() {
		return worksH;
	}
	public void setWorksH(double worksH) {
		this.worksH = worksH;
	}
	public double getWorksW() {
		return worksW;
	}
	public void setWorksW(double worksW) {
		this.worksW = worksW;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	public String getVideoDesc() {
		return videoDesc;
	}
	public void setVideoDesc(String videoDesc) {
		this.videoDesc = videoDesc;
	}
	public String getVideoTags() {
		return videoTags;
	}
	public void setVideoTags(String videoTags) {
		this.videoTags = videoTags;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getVideoPath() {
		return videoPath;
	}
	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	public String getVideoVoice() {
		return videoVoice;
	}
	public void setVideoVoice(String videoVoice) {
		this.videoVoice = videoVoice;
	}
	public String getVideoText() {
		return videoText;
	}
	public void setVideoText(String videoText) {
		this.videoText = videoText;
	}
	public String getSumTime() {
		return sumTime;
	}
	public void setSumTime(String sumTime) {
		this.sumTime = sumTime;
	}
	public String getFiles() {
		return files;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getKnowFirstId() {
		return knowFirstId;
	}
	public void setKnowFirstId(String knowFirstId) {
		this.knowFirstId = knowFirstId;
	}
	public String getKnowSecondId() {
		return knowSecondId;
	}
	public void setKnowSecondId(String knowSecondId) {
		this.knowSecondId = knowSecondId;
	}
	public String getKnowThirdId() {
		return knowThirdId;
	}
	public void setKnowThirdId(String knowThirdId) {
		this.knowThirdId = knowThirdId;
	}
	public String getScreenshotUrl() {
		return screenshotUrl;
	}
	public void setScreenshotUrl(String screenshotUrl) {
		this.screenshotUrl = screenshotUrl;
	}
	public String getScreenshotUrlMedium() {
		return screenshotUrlMedium;
	}
	public void setScreenshotUrlMedium(String screenshotUrlMedium) {
		this.screenshotUrlMedium = screenshotUrlMedium;
	}
	public String getScreenshotUrlSmall() {
		return screenshotUrlSmall;
	}
	public void setScreenshotUrlSmall(String screenshotUrlSmall) {
		this.screenshotUrlSmall = screenshotUrlSmall;
	}
	
	public String getZipUrl() {
		return zipUrl;
	}
	public void setZipUrl(String zipUrl) {
		this.zipUrl = zipUrl;
	}
	public int getIsDownLoad() {
		return isDownLoad;
	}
	public void setIsDownLoad(int isDownLoad) {
		this.isDownLoad = isDownLoad;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	public String getHeadName() {
		return headName;
	}
	public void setHeadName(String headName) {
		this.headName = headName;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getIsUpLoad() {
		return isUpLoad;
	}
	public void setIsUpLoad(int isUpLoad) {
		this.isUpLoad = isUpLoad;
	}
	public String getWeburl() {
		return weburl;
	}
	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}
	public String getNoteImage() {
		return noteImage;
	}
	public void setNoteImage(String noteImage) {
		this.noteImage = noteImage;
	}
	public String getVideoType() {
		return videoType;
	}
	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}
	
}
