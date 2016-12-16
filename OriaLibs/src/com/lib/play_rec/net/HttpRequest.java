package com.lib.play_rec.net;

import java.io.File;

import com.lib.play_rec.entity.Constant;

public class HttpRequest {

	private static HttpRequest instance = null;
	private HttpUrlConnectionUtil httpUtil = null;

	public static HttpRequest getInstance() {
		if (instance == null) {
			instance = new HttpRequest();
		}
		return instance;
	}

	public HttpRequest() {
		httpUtil = new HttpUrlConnectionUtil();
	}

	/** 获取素材库 */
	public String getMaterial(String subjectId, String gradeId, String k1,
			String k2, String k3, int page, int max) {
		StringBuffer sb = new StringBuffer();
		sb.append("&did=");
		sb.append(subjectId);
		sb.append("&phid=");
		sb.append(gradeId);
		sb.append("&k1=");
		sb.append(k1);
		sb.append("&k2=");
		sb.append(k2);
		sb.append("&k3=");
		sb.append(k3);
		if (page != -1) {
			sb.append("&page=");
			sb.append(page);
		}
		if (max != -1) {
			sb.append("&max=");
			sb.append(max);
		}

		String url = Constant.HOST + "&act=material";
		return httpUtil.post(url, sb.toString());
	}

	/** PPT素材 */
	public String getPPTMaterial(String uid, String key) {
		StringBuffer sb = new StringBuffer();
		sb.append("&uid=");
		sb.append(uid);
		sb.append("&type=");
		sb.append(key);

		String url = Constant.HOST + "&act=material";
		return httpUtil.post(url, sb.toString());
	}

	/** PPT素材图片 */
	public String getPPTImageMaterial(int id) {
		String url = Constant.HOST + "&act=material_pptimg";
		return httpUtil.post(url, "&id=" + id);
	}

	/** 图片下载,直接保存到指定文件 */
	public boolean downFile(String url, File file) {
		try {
			httpUtil.downFile(url, file);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
