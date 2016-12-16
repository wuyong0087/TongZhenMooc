package com.lib.play_rec.rec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 重置界面
 * 
 * @author Administrator
 */
public class ResetScreenCAP {

	private AtyRecord activity;
	private RecordViewGroup group;
	private JSONArray commandArray;

	public void resetViewGroup() {
		for (int i = 0, j = commandArray.length(); i < j; i++) {
			try {
				JSONObject currObj = commandArray.getJSONObject(i);
				String type = currObj.getString("order");
				double startTime = currObj.getDouble("startTime");
				double endTime = currObj.getDouble("endTime");
				if (startTime == 0 && endTime == 0) {
					play(type, currObj);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void play(String type, JSONObject currObj) {
		try {
			if ("NewPage".equals(type)) { // 新建空白页
				activity.newBlankPage();
			} else if ("FlipPage".equals(type)) { // 移动一页
				activity.nextPage();
			} else if ("Cbackground".equals(type)) { // 改变背景
				int cBackground = currObj.getJSONObject("detail").getInt(
						"cBackground");
//				group.setBackground(cBackground, false, true);
			} else if ("Pencil".equals(type) || "Rubber".equals(type)) {
			} else if ("Rectangle".equals(type)) { // 矩形操作
			} else if ("ClearRec".equals(type)) { // 删除操作
			} else if ("Ellipse".equals(type)) { // 椭圆操作
			} else if ("Arrow".equals(type)) {// 箭头操作
			} else if ("Undo".equals(type)) { // 回退
			} else if ("Redo".equals(type)) { // 前进
			} else if ("AddText".equals(type)) { // 添加文本
			} else if ("ModifyText".equals(type)) { // 修改文本
			} else if ("DeleteText".equals(type)) { // 删除文本
			} else if ("MoveText".equals(type)) { // 移动文本
			} else if ("AddImage".equals(type)) { // 添加图片
			} else if ("DeleteImage".equals(type)) { // 删除图片
			} else if ("MoveImage".equals(type)) { // 移动图片
			} else if ("ScaleImage".equals(type)) { // 缩放图片
			} else if ("RotateImage".equals(type)) {// 旋转图片
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
