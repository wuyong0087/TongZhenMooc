package com.lib.play_rec.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.lib.play_rec.BaseActivity;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.entity.ViewGroupBean;
import com.lib.play_rec.rec.RecordView;
import com.lib.play_rec.utils.BitmapUtil;
import com.lib.play_rec.utils.HexUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoPlayerBaseAty extends BaseActivity {

	private WakeLock wakeLock = null;
	protected PlayViewGroup viewGroup;
	public int currPager = 0; // 当前页
	public PlayView playView = null;
	public RecordView wacomView = null;

	/** 全局变量 */
	protected int videoWidth = 944, videoHeight = 719; // 视频录制的宽度高度
	protected static float scale = 1f; // 屏幕缩放比例
	protected final int thousand = 1000; // 基数1000
	protected int playStatus = 0; // 播放状态 (0-未开始播放 1-正在播放 2-播放完毕)
	protected boolean pauseRecord = false; // 是否暂停录制的动作
	protected int color = Color.BLACK; // 笔触颜色
	protected int mid;
	// protected boolean isAndroid;
	protected ArrayList<String> midList;// 删除画笔mid集合
	protected int stroke = 2; // 线宽
	// protected int backgroundOrder = 1; // 背景的序号，默认第一张
	protected int operate = Config.OPER_PEN; // 操作类型
	protected int editTextColor = Color.BLACK; // 文本颜色
	protected int editTextSize = 26; // 文本大小
	protected Map<Integer, String> bmpMap; // 所有对象集合
	protected Map<Integer, Bitmap> bmpMaps = new HashMap<Integer, Bitmap>(); // 所有对象集合
	protected List<ViewGroupBean> viewGroupList;
	protected ViewGroupBean groupBean;
	protected String imgPath; // 图片路径
	protected int arrowStroke = 4; // 箭头线宽
	protected int shapeStroke = 4; // 图形线宽

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PowerManager powerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"My Lock");
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.wakeLock.acquire();
	}

	@Override
	public void onPause() {
		super.onPause();
		this.wakeLock.release();
	}

	// 初始化默认值
	protected void initVal() {
		currPager = 0;
		playStatus = 0; // 播放状态 (0-未开始播放 1-正在播放 2-播放完毕)
		pauseRecord = false; // 是否暂停录制的动作
		color = Color.BLACK; // 笔触颜色
		stroke = 2; // 线宽
		// backgroundOrder = 1; // 背景的序号，默认第一张
		operate = Config.OPER_PEN; // 操作类型
		editTextColor = Color.BLACK; // 文本颜色
		editTextSize = 26; // 文本大小
		arrowStroke = 4; // 箭头线宽
	}

	/**
	 * 计算播放的缩放比例
	 */
	protected void calculateScale() {
		float playWidth = GlobalInit.getInstance().getScreenWidth();
		float playHeight = GlobalInit.getInstance().getScreenHeight();

		if (playWidth / videoWidth >= playHeight / videoHeight) {
			scale = playHeight / videoHeight;
		} else {
			scale = playWidth / videoWidth;
		}
		videoWidth = (int) (videoWidth * scale + 0.5);
		videoHeight = (int) (videoHeight * scale + 0.5);
	}

	// 坐标值的缩放
	protected double calculatePoint(double point) {
		return point * scale;
	}

	protected int calculatePointToInt(double point) {
		return (int) (point * scale + 0.5);
	}

	/** 判断是否暂停操作 */
	protected boolean isPause(String type, double startTime, double endTime) {
		if (type.equals("Pencil")
				|| type.equals("Rubber")
				|| type.equals("Arrow")
				|| type.equals("Ellipse")
				|| type.equals("Rect")
				|| type.equals("Cursor")
				// || type.equals("FlipPage")
				// || type.equals("NewPage")
				|| type.equals("AddImage") || type.equals("AddText")
				|| type.equals("MoveImage") || type.equals("MoveText")
				|| type.equals("ScaleImage")
		// || type.equals("RotateImage")
		) {
			if (startTime == endTime) { // 指令操作的开始时间和结束时间如果一样，则标识是暂停时的操作
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 比例缩放
	 */
	protected void scale(JSONObject commandObj, String type, boolean put)
			throws JSONException {
		if ("Pencil".equals(type) || "Arrow".equals(type)
				|| "Line".equals(type)) { // 画笔和橡皮擦
			commandObj.put("thickness", commandObj.getInt("thickness"));
			JSONArray dataArray = commandObj.getJSONArray("data");
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				dataObj.put("x", calculatePoint(dataObj.getDouble("x")));
				dataObj.put("y", calculatePoint(dataObj.getDouble("y")));
			}
		} else if ("Cursor".equals(type)) { // 移动光标
			JSONArray dataArray = commandObj.getJSONArray("center");
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				dataObj.put("x", calculatePoint(dataObj.getDouble("x")));
				dataObj.put("y", calculatePoint(dataObj.getDouble("y")));
			}
		} else if (Config.ORDER_ADD_TEXT.equals(type)) { // 添加文本
			JSONObject detailObj = commandObj.getJSONObject("detail");
			detailObj.put("fontSize",
					calculatePointToInt(detailObj.getInt("fontSize")));
			detailObj.put("x", calculatePointToInt(detailObj.getDouble("x")));
			detailObj.put("y", calculatePointToInt(detailObj.getDouble("y")));
			detailObj.put("width",
					calculatePointToInt(detailObj.getDouble("width")));
			detailObj.put("height",
					calculatePointToInt(detailObj.getDouble("height")));
		} else if (Config.ORDER_UNDO_ADDTEXT.equals(type)) { // 修改文本
			JSONObject detailObj = commandObj.getJSONObject("detail");
			detailObj.put("fontSize",
					calculatePointToInt(detailObj.getInt("fontSize")));
			detailObj.put("x", calculatePointToInt(detailObj.getDouble("x")));
			detailObj.put("y", calculatePointToInt(detailObj.getDouble("y")));
			detailObj.put("width",
					calculatePointToInt(detailObj.getDouble("width")));
			detailObj.put("height",
					calculatePointToInt(detailObj.getDouble("height")));
		} else if (Config.ORDER_REDO_ADDTEXT.equals(type)) { // 修改文本
			JSONObject detailObj = commandObj.getJSONObject("detail");
			detailObj.put("fontSize",
					calculatePointToInt(detailObj.getInt("fontSize")));
			detailObj.put("x", calculatePointToInt(detailObj.getDouble("x")));
			detailObj.put("y", calculatePointToInt(detailObj.getDouble("y")));
			detailObj.put("width",
					calculatePointToInt(detailObj.getDouble("width")));
			detailObj.put("height",
					calculatePointToInt(detailObj.getDouble("height")));
		} else if (Config.ORDER_EDIT_TEXT.equals(type)) { // 修改文本
			JSONObject detailObj = commandObj.getJSONObject("detail");
			detailObj.put("fontSize",
					calculatePointToInt(detailObj.getInt("fontSize")));
			detailObj.put("x", calculatePointToInt(detailObj.getDouble("x")));
			detailObj.put("y", calculatePointToInt(detailObj.getDouble("y")));
			detailObj.put("width",
					calculatePointToInt(detailObj.getDouble("width")));
			detailObj.put("height",
					calculatePointToInt(detailObj.getDouble("height")));
		} else if (Config.ORDER_UNDO_EDITTEXT.equals(type)) { // 修改文本
			JSONObject detailObj = commandObj.getJSONObject("detail");
			detailObj.put("fontSize",
					calculatePointToInt(detailObj.getInt("fontSize")));
			detailObj.put("x", calculatePointToInt(detailObj.getDouble("x")));
			detailObj.put("y", calculatePointToInt(detailObj.getDouble("y")));
			detailObj.put("width",
					calculatePointToInt(detailObj.getDouble("width")));
			detailObj.put("height",
					calculatePointToInt(detailObj.getDouble("height")));
		} else if (Config.ORDER_REDO_EDITTEXT.equals(type)) { // 修改文本
			JSONObject detailObj = commandObj.getJSONObject("detail");
			detailObj.put("fontSize",
					calculatePointToInt(detailObj.getInt("fontSize")));
			detailObj.put("x", calculatePointToInt(detailObj.getDouble("x")));
			detailObj.put("y", calculatePointToInt(detailObj.getDouble("y")));
			detailObj.put("width",
					calculatePointToInt(detailObj.getDouble("width")));
			detailObj.put("height",
					calculatePointToInt(detailObj.getDouble("height")));
		} else if ("MoveText".equals(type)) { // 移动文本
			JSONArray dataArray = commandObj.getJSONArray("detail");
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				dataObj.put("x", calculatePointToInt(dataObj.getDouble("x")));
				dataObj.put("y", calculatePointToInt(dataObj.getDouble("y")));
			}
		} else if (Config.ORDER_UNDO_MOVETEXT.equals(type)) { // 移动文本
			JSONArray dataArray = commandObj.getJSONArray("detail");
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				dataObj.put("x", calculatePointToInt(dataObj.getDouble("x")));
				dataObj.put("y", calculatePointToInt(dataObj.getDouble("y")));
			}
		} else if (Config.ORDER_REDO_MOVETEXT.equals(type)) { // 移动文本
			JSONArray dataArray = commandObj.getJSONArray("detail");
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				dataObj.put("x", calculatePointToInt(dataObj.getDouble("x")));
				dataObj.put("y", calculatePointToInt(dataObj.getDouble("y")));
			}
		} else if (Config.ORDER_ADD_IMAGE.equals(type)) { // 添加图片
			JSONObject detailObj = commandObj.getJSONObject("detail");
			detailObj.put("x", calculatePointToInt(detailObj.getDouble("x")));
			detailObj.put("y", calculatePointToInt(detailObj.getDouble("y")));
			detailObj.put("width",
					calculatePointToInt(detailObj.getDouble("width")));
			detailObj.put("height",
					calculatePointToInt(detailObj.getDouble("height")));

			// 加载图片对象---------------------------------
			String source = detailObj.getString("source");
			if (put) {
				bmpMap.put(commandObj.getInt("mid"),
						imgPath + source.substring(0, source.lastIndexOf("."))
								+ Config.LOCAL_IMAGE_SUFFIX);
			} else {
				bmpMaps.put(
						commandObj.getInt("mid"),
						BitmapUtil.getDatBmpWithPngPathForWORH(
								imgPath
										+ source.substring(0,
												source.lastIndexOf("."))
										+ Config.LOCAL_IMAGE_SUFFIX,
								detailObj.getInt("width"),
								detailObj.getInt("height")));
			}
		} else if (Config.ORDER_UNDO_ADDIMAGE.equals(type)) { // 添加图片
			JSONObject detailObj = commandObj.getJSONObject("detail");
			detailObj.put("x", calculatePointToInt(detailObj.getDouble("x")));
			detailObj.put("y", calculatePointToInt(detailObj.getDouble("y")));
			detailObj.put("width",
					calculatePointToInt(detailObj.getDouble("width")));
			detailObj.put("height",
					calculatePointToInt(detailObj.getDouble("height")));

			// 加载图片对象---------------------------------
			String source = detailObj.getString("source");
			if (put) {
				bmpMap.put(commandObj.getInt("mid"),
						imgPath + source.substring(0, source.lastIndexOf("."))
								+ Config.LOCAL_IMAGE_SUFFIX);
			} else {
				bmpMaps.put(
						commandObj.getInt("mid"),
						BitmapUtil.getDatBmpWithPngPathForWORH(
								imgPath
										+ source.substring(0,
												source.lastIndexOf("."))
										+ Config.LOCAL_IMAGE_SUFFIX,
								detailObj.getInt("width"),
								detailObj.getInt("height")));
			}
		} else if (Config.ORDER_REDO_ADDIMAGE.equals(type)) { // 添加图片
			JSONObject detailObj = commandObj.getJSONObject("detail");
			detailObj.put("x", calculatePointToInt(detailObj.getDouble("x")));
			detailObj.put("y", calculatePointToInt(detailObj.getDouble("y")));
			detailObj.put("width",
					calculatePointToInt(detailObj.getDouble("width")));
			detailObj.put("height",
					calculatePointToInt(detailObj.getDouble("height")));

			// 加载图片对象---------------------------------
			String source = detailObj.getString("source");
			if (put) {
				bmpMap.put(commandObj.getInt("mid"),
						imgPath + source.substring(0, source.lastIndexOf("."))
								+ Config.LOCAL_IMAGE_SUFFIX);
			} else {
				bmpMaps.put(
						commandObj.getInt("mid"),
						BitmapUtil.getDatBmpWithPngPathForWORH(
								imgPath
										+ source.substring(0,
												source.lastIndexOf("."))
										+ Config.LOCAL_IMAGE_SUFFIX,
								detailObj.getInt("width"),
								detailObj.getInt("height")));
			}
		} else if (Config.ORDER_MOVE_IMAGE.equals(type)) { // 移动图片
			JSONArray dataArray = commandObj.getJSONArray("detail");
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				dataObj.put("x", calculatePoint(dataObj.getDouble("x")));
				dataObj.put("y", calculatePoint(dataObj.getDouble("y")));
			}
		} else if (Config.ORDER_UNDO_MOVEIMAGE.equals(type)) { // 移动图片
			JSONArray dataArray = commandObj.getJSONArray("detail");
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				dataObj.put("x", calculatePoint(dataObj.getDouble("x")));
				dataObj.put("y", calculatePoint(dataObj.getDouble("y")));
			}
		} else if (Config.ORDER_REDO_MOVEIMAGE.equals(type)) { // 移动图片
			JSONArray dataArray = commandObj.getJSONArray("detail");
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				dataObj.put("x", calculatePoint(dataObj.getDouble("x")));
				dataObj.put("y", calculatePoint(dataObj.getDouble("y")));
			}
		}
	}

	/**
	 * 修改文本
	 * 
	 * @throws JSONException
	 */
	protected void modifyText(JSONObject commandObj) throws JSONException {
		JSONObject detailObj = commandObj.getJSONObject("detail");
		viewGroup.modifyEditText(
				(PlayEditText) groupBean.viewMap.get(commandObj.getInt("mid")),
				detailObj.getString("words"), detailObj.getInt("fontSize"),
				HexUtil.hexToColor(detailObj.getString("fontColor")),
				detailObj.getInt("width"), detailObj.getInt("height"),
				!pauseRecord);
	}

}
