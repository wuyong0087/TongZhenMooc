package com.lib.play_rec.entity;

import java.io.Serializable;

public class RecommendBean implements Serializable{
	
	private static final long serialVersionUID = 1230209988635095735L;
	
	private int id;  // id
	private String tid;  // id
	private String tags;  // 标签
	private String smallImgUrl;  // 缩略图下载地址
	private String commImgUrl;   // 素材库下载地址
	
	private int bw;  // 大图宽
	private int bh;  // 大图高
	private int sw;  // 小图宽
	private int sh;  // 小图高
	
	private boolean isCheck = false;//给PPT图片设置一个是否被选中的标识，防止复用
	
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public int getBw() {
		return bw;
	}
	public void setBw(int bw) {
		this.bw = bw;
	}
	public int getBh() {
		return bh;
	}
	public void setBh(int bh) {
		this.bh = bh;
	}
	public int getSw() {
		return sw;
	}
	public void setSw(int sw) {
		this.sw = sw;
	}
	public int getSh() {
		return sh;
	}
	public void setSh(int sh) {
		this.sh = sh;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getSmallImgUrl() {
		return smallImgUrl;
	}
	public void setSmallImgUrl(String smallImgUrl) {
		this.smallImgUrl = smallImgUrl;
	}
	public String getCommImgUrl() {
		return commImgUrl;
	}
	public void setCommImgUrl(String commImgUrl) {
		this.commImgUrl = commImgUrl;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	
}
