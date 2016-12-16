package com.lib.play_rec.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectRWUtil {

	private List<String> videoIds = new ArrayList<String>();// 存放已经上传了的视频id

	private static ObjectRWUtil instance = null;

	private Map<String, List<String>> storeMap = null;

	public static ObjectRWUtil getInstance() {
		if (instance == null) {
			instance = new ObjectRWUtil();
		}
		return instance;
	}


	// 存放已经上传的视频的id
	public void putVideoId(String videoId) {
		videoIds.add(videoId);
	}

	// 上传集合是否有该视频id，如果不存在则返回true
	public boolean IDOnUploadList(String videoId) {
		if (videoIds.contains(videoId)) {
			return false;
		} else {
			return true;
		}
	}

	// arraylist 冒泡排序
	public ArrayList<Integer> getSortList(ArrayList<Integer> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 1; j < list.size() - i; j++) {
				Integer a;
				if ((list.get(j - 1)).compareTo(list.get(j)) > 0) { // 比较两个整数的大小
					a = list.get(j - 1);
					list.set((j - 1), list.get(j));
					list.set(j, a);
				}
			}
		}
		return list;
	}
}
