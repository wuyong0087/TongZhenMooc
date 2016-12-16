package com.lib.play_rec.rec;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

import com.lib.play_rec.entity.Config;
import com.lib.play_rec.utils.DateUtil;
import com.lib.play_rec.utils.HexUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NoteWacomViewGroup {

	public ArrayList<HistoryMemory> historyMemories;
	public ArrayList<HistoryMemory> undoMemories;

	private Path path;
	private HistoryMemory paintMemory;
	private Paint paint;
	private float[] data;
	private float cur_x;
	private float cur_y;
	private Map<Integer, HistoryMemory> memoryMap;
	private int pager = 0;
	private int pagers = 0;
	private ArrayList<Integer> pagerBgs = new ArrayList<Integer>();
	private int bgNum = 1;// 默认背景为1 (白色)

	public NoteWacomViewGroup() {
		memoryMap = Collections
				.synchronizedMap(new HashMap<Integer, HistoryMemory>());
		historyMemories = new ArrayList<HistoryMemory>();
		undoMemories = new ArrayList<HistoryMemory>();
		pagerBgs.add(0, 1);
	}

	// 设置总共页数
	public void setPagers() {
		pagers++;
		pagerBgs.add(pagers, bgNum);
	}

	// 得到总共页数
	public int getGroupPager() {
		return pagers;
	}

	// 设置当前页号
	public void setViewGroupPager(int viewGroupPager) {
		pager += viewGroupPager;
	}

	public void setViewGroupBg(int bgNum) {
		this.bgNum = bgNum;
		pagerBgs.set(pagers, bgNum);
	}

	public ArrayList<Integer> getpagerBgList() {
		return pagerBgs;
	}

	/** 初始化画笔对象 */
	private void initPaint(int color, int stroke, int operate) {
		paint = new Paint();
		paint.setAntiAlias(true); // 抗锯齿
		paint.setDither(true); // 消除拉动，使画面圓滑
		paint.setColor(operate == Config.OPER_RUBBER ? Color.TRANSPARENT : color); // 设置颜色
		paint.setXfermode(operate == Config.OPER_RUBBER ? new PorterDuffXfermode(
				Mode.CLEAR) : null);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND); // 结合方式，平滑
		paint.setStrokeCap(Paint.Cap.ROUND); // 形状
		paint.setStrokeWidth(stroke); // 设置线宽
	}

	public void touch_start(float x, float y, int color, int stroke, int mid,
			int operate) {
		path = new Path();
		initPaint(color, stroke, operate);
		path.moveTo(x, y);
		cur_x = x;
		cur_y = y;
		paintMemory = new HistoryMemory(1);
		paintMemory.setMid(mid);
		paintMemory.setPath(path);
		paintMemory.setPaint(paint);
		paintMemory.setStartTime(DateUtil.getTimeMillisLong());
		data = new float[] { x, y, 0, 0 };
		paintMemory.getTimeList().add(paintMemory.getStartTime());
		paintMemory.getDataList().add(data);
	}

	public void touch_move(float x, float y, int operate) {
		if (path == null) {
			return;
		}
		if (operate == Config.OPER_RUBBER) {
			// 如果是擦除操作，必须实时绘制path
			path.lineTo(x, y);
		} else {
			path.quadTo(cur_x, cur_y, (x + cur_x) / 2, (y + cur_y) / 2);
		}
		cur_x = x;
		cur_y = y;
		data = new float[] { x, y, 0, 0 };
		paintMemory.getTimeList().add(System.currentTimeMillis());
		paintMemory.getDataList().add(data);
	}

	public void touch_up(int operate) {
		if (path == null) {
			return;
		}
		path.lineTo(cur_x, cur_y);
		// 鼠标弹起保存最后状态
		paintMemory.setEndTime(System.currentTimeMillis());
		if (operate == Config.OPER_RUBBER) {
			paintMemory.setOperaterType(Config.OPER_RUBBER);
		} else {
			paintMemory.setOperaterType(Config.OPER_PEN);
		}
		paintMemory.setViewGroupPager(pager);
		historyMemories.add(paintMemory);
	}

	/** 绘制箭头 */
	public void pathStart(float x, float y, int color, int stroke, int mid) {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		paint.setStrokeWidth(AtyRecord.arrowStroke);
		paint.setStyle(Paint.Style.STROKE);
		cur_x = x;
		cur_y = y;

		// 录屏操作时，保存一些信息记录
		paintMemory = new HistoryMemory(HistoryMemory.DATA_LIST);
		paintMemory.setMid(mid);
		paintMemory.setPaint(paint);
		paintMemory.setStartTime(DateUtil.getTimeMillisLong());
		data = new float[] { cur_x, cur_y };
		paintMemory.getTimeList().add(paintMemory.getStartTime());
		paintMemory.getDataList().add(data);
	}

	public void pathMove(float x, float y) {
		cur_x = x;
		cur_y = y;

		data = new float[] { cur_x, cur_y };
		paintMemory.getTimeList().add(System.currentTimeMillis());
		paintMemory.getDataList().add(data);
	}

	public void pathUp(float x, float y,int type) {
		paintMemory.setEndTime(DateUtil.getTimeMillisLong());
		data = new float[] { x, y };
		paintMemory.getTimeList().add(paintMemory.getEndTime());
		paintMemory.getDataList().add(data);
		paintMemory.setOperaterType(type);
		paint = null;
		paintMemory.setViewGroupPager(pager);
		historyMemories.add(paintMemory);
	}

	public void initEditText(Activity context, float x, float y,
			int width, int height, int editTextColor, int editTextSize,
			String text, int mid) {
		HistoryMemory editMemory = new HistoryMemory(2);
		editMemory.setOperaterType(Config.OPER_TEXT_ADD);
		data = new float[] { x, y };
		editMemory.setPoint(data);
		editMemory.setMid(mid);
		editMemory.setTextWidth(width);
		editMemory.setTextHeight(height);
		editMemory.setOldColor(editTextColor);
		editMemory.setOldSize(editTextSize);
		editMemory.setOldText(text);
		editMemory.setViewGroupPager(pager);
		historyMemories.add(editMemory);
		memoryMap.put(mid, editMemory);
	}

	/** 修改文本 */
	public void modifyText(JSONObject currObj) {
		try {
			JSONObject detailObj = currObj.getJSONObject("detail");
			HistoryMemory editMemory = (HistoryMemory) memoryMap.get(currObj
					.getInt("mid"));
			historyMemories.remove(editMemory);
			editMemory.setOldColor(HexUtil.hexToColor(detailObj
					.getString("fontColor")));
			editMemory.setOldSize(detailObj.getInt("fontSize"));
			editMemory.setOldText(detailObj.getString("words"));
			editMemory.setTextWidth(detailObj.getInt("width"));
			editMemory.setTextHeight(detailObj.getInt("height"));
			historyMemories.add(editMemory);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 删除文本 */
	public void delEditText(int mid) {
		HistoryMemory editMemory = memoryMap.get(mid);
		historyMemories.remove(editMemory);
		memoryMap.remove(mid);
	}

	/** 移动文本 */
	public void MoveText(JSONObject currObj) {
		try {
			HistoryMemory editMemory = memoryMap.get(currObj.getInt("mid"));
			historyMemories.remove(editMemory);
			JSONArray dataArray = currObj.getJSONObject("detail").getJSONArray(
					"data");
			JSONObject dataObj = dataArray
					.getJSONObject(dataArray.length() - 1);
			float[] move = new float[] { dataObj.getInt("x"),
					dataObj.getInt("y") };
			editMemory.getMoveList().add(move);
			historyMemories.add(editMemory);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 添加图片 */
	public void initImageView(Activity context, Bitmap bmp,
			String imgName, int mid) {
		HistoryMemory imageMemory = new HistoryMemory(3);
		imageMemory.setBitmap(bmp);
		imageMemory.setMid(mid);
		imageMemory.setImgName(imgName);
		imageMemory.setOperaterType(Config.OPER_IMG_ADD);
		imageMemory.setViewGroupPager(pager);
		historyMemories.add(imageMemory);
		memoryMap.put(mid, imageMemory);
	}

	/** 删除图片 */
	public void delImageView(int mid) {
		HistoryMemory imageMemory = (HistoryMemory) memoryMap.get(mid);
		historyMemories.remove(imageMemory);
	}

	/** 移动图片 */
	public void MoveImageView(JSONObject currObj) {
		try {
			JSONArray dataArray = currObj.getJSONObject("detail").getJSONArray(
					"data");
			HistoryMemory imageMemory = (HistoryMemory) memoryMap.get(currObj
					.getInt("mid"));
			historyMemories.remove(imageMemory);
			JSONObject dataObj = dataArray
					.getJSONObject(dataArray.length() - 1);
			float center_x = (float) (dataObj.getDouble("x"));
			float center_y = (float) (dataObj.getDouble("y"));
			float[] move = new float[] { center_x, center_y };
			imageMemory.getMoveList().add(move);
			historyMemories.add(imageMemory);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 缩放图片 */
	public void ScaleImageView(JSONObject currObj) {
		try {
			int mid = currObj.getInt("mid");
			HistoryMemory imageMemory = (HistoryMemory) memoryMap.get(mid);
			historyMemories.remove(imageMemory);
			JSONArray dataArray = currObj.getJSONObject("detail").getJSONArray(
					"data");
			double scale = 1.0;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				scale *= dataArray.getJSONObject(m).getDouble("scale");
			}
			imageMemory.getScaleList().add((float) scale);
			historyMemories.add(imageMemory);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 旋转图片 */
	public void RotateImageView(JSONObject currObj) {
		try {
			int mid = currObj.getInt("mid");
			HistoryMemory imageMemory = (HistoryMemory) memoryMap.get(mid);
			historyMemories.remove(imageMemory);
			JSONArray dataArray = currObj.getJSONObject("detail").getJSONArray(
					"data");
			double angle = 1.0;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				double d = dataArray.getJSONObject(m).getDouble("angle");
				angle *= d;
			}
			imageMemory.getRotateList().add((float) angle);
			historyMemories.add(imageMemory);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 回退 */
	public void undo() {
		for (int i = historyMemories.size() - 1; i >= 0; i--) {
			HistoryMemory memory = historyMemories.get(i);
			if (memory.getViewGroupPager() == pager) {
				int type = memory.getOperaterType();
				if (memory.getViewGroupPager() == pager) {
					if (type == Config.OPER_PEN || type == Config.OPER_RUBBER
							|| type == Config.OPER_ARROW
							|| type == Config.OPER_ELLIPSE
							|| type == Config.OPER_RECT
							|| type == Config.OPER_LINE) {
						undoMemories.add(memory);
						historyMemories.remove(memory);
						break;
					}
				}
			}
		}
		// HistoryMemory memory = historyMemories.get(historyMemories.size()-1);
		// int type = memory.getOperaterType();
		// switch (type) {
		// case Config.OPER_ScaleImage:
		// historyMemories.remove(memory);
		// List<Float> scaleList = memory.getScaleList();
		// Float scale = scaleList.get(scaleList.size() - 1);
		// scaleFloats.add(scale);
		// memory.getScaleList().remove(scaleList.size() - 1);
		// historyMemories.add(memory);
		// break;
		// case Config.OPER_RotateImage:
		// memory.getMimg().rotate_90(false);
		// break;
		// }
	}

	/** 前进 */
	public void redo() {
		HistoryMemory memory = undoMemories.get(undoMemories.size() - 1);
		int type = memory.getOperaterType();
		if (type == Config.OPER_PEN || type == Config.OPER_RUBBER
				|| type == Config.OPER_ARROW || type == Config.OPER_ELLIPSE
				|| type == Config.OPER_RECT || type == Config.OPER_LINE) {
			undoMemories.remove(memory);
			historyMemories.add(memory);
		}
		// switch (type) {
		// case Config.OPER_ScaleImage:
		// historyMemories.remove(memory);
		// Float scale = scaleFloats.get(scaleFloats.size() - 1);
		// memory.getScaleList().add(scale);
		// scaleFloats.remove(scaleFloats.size() - 1);
		// historyMemories.add(memory);
		// break;
		// case Config.OPER_RotateImage:
		// memory.getMimg().rotate_90(false);
		// break;
		// }
	}

}
