package com.lib.play_rec.rec;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;

public class HistoryMemory {

	private int mid;// 线条id
	private int type; // 类型，1-画笔 2-文本 3-图片 4-箭头 5-矩形 6-椭圆
	private int operaterType; // 操作类型
	private int source = 1; // 画笔来源 1 = 手写；2 = 数码笔。默认为1
	
	// ------------文本相关------------
	private RecordEditText met = null;
	private int oldColor, newColor; // 文本颜色
	private int oldSize, newSize; // 文字大小
	private int textWidth,textHeight;//宽高
	private String oldText = null, newText = null; // 文本内容
	private float[] point=new float[]{}; 

	// -------------图片相关--------------
	private RecordImageView mimg = null;
	private List<Float> scaleList; // 开始缩放位置
	private List<Float> rotateList; // 开始旋转位置
	private Bitmap bitmap;
	private String bitmapPath;
	private String imgName;

	// ----------画笔相关---------
	private Path path; // 路径
	private Paint paint; // 画笔
	private List<float[]> dataList;

	// ----------------公共属性------------
	private int viewGroupPager;
	private long startTime;
	private long endTime;
	private List<float[]> moveList; // 开始移动位置
	private List<Long> timeList; // 时间列表
	private boolean lock = false; // 控件是否锁定
	// 与橡皮擦坐标点相交的执行的ID
	private String ids;
	// 实例化的数据的类型
	public static final int DATA_LIST = 1;
	public static final int MOVE_LIST = 2;
	public static final int IMAGE_LIST = 3;
	
	public HistoryMemory(int type) {
		this.type = type;
		timeList = new ArrayList<Long>();
		switch (type) {
		case DATA_LIST:
			dataList = new ArrayList<float[]>();
			break;
		case MOVE_LIST:
			moveList = new ArrayList<float[]>();
			break;
		case IMAGE_LIST:
			moveList = new ArrayList<float[]>();
			scaleList = new ArrayList<Float>();
			rotateList = new ArrayList<Float>();
			break;
		}
	}
	
	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}


	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getBitmapPath() {
		return bitmapPath;
	}  

	public void setBitmapPath(String bitmapPath) {
		this.bitmapPath = bitmapPath;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOperaterType() {
		return operaterType;
	}

	public void setOperaterType(int operaterType) {
		this.operaterType = operaterType;
	}

	public RecordEditText getRecordText() {
		return met;
	}

	public void setRecordText(RecordEditText met) {
		this.met = met;
	}

	public int getOldColor() {
		return oldColor;
	}

	public void setOldColor(int oldColor) {
		this.oldColor = oldColor;
	}

	public int getNewColor() {
		return newColor;
	}

	public void setNewColor(int newColor) {
		this.newColor = newColor;
	}

	public int getOldSize() {
		return oldSize;
	}

	public void setOldSize(int oldSize) {
		this.oldSize = oldSize;
	}

	public int getNewSize() {
		return newSize;
	}

	public void setNewSize(int newSize) {
		this.newSize = newSize;
	}

	public String getOldText() {
		return oldText;
	}

	public void setOldText(String oldText) {
		this.oldText = oldText;
	}

	public String getNewText() {
		return newText;
	}

	public void setNewText(String newText) {
		this.newText = newText;
	}

	public RecordImageView getMimg() {
		return mimg;
	}

	public void setMimg(RecordImageView mimg) {
		this.mimg = mimg;
	}

	public List<Float> getScaleList() {
		return scaleList;
	}

	public void setScaleList(List<Float> scaleList) {
		this.scaleList = scaleList;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public List<float[]> getDataList() {
		return dataList;
	}

	public void setDataList(List<float[]> dataList) {
		this.dataList = dataList;
	}

	public List<float[]> getMoveList() {
		return moveList;
	}

	public void setMoveList(List<float[]> moveList) {
		this.moveList = moveList;
	}

	public List<Long> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<Long> timeList) {
		this.timeList = timeList;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public List<Float> getRotateList() {
		return rotateList;
	}

	public void setRotateList(List<Float> rotateList) {
		this.rotateList = rotateList;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getViewGroupPager() {
		return viewGroupPager;
	}

	public void setViewGroupPager(int viewGroupPager) {
		this.viewGroupPager = viewGroupPager;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public float[] getPoint() {
		return point;
	}

	public void setPoint(float[] point) {
		this.point = point;
	}

	public int getTextHeight() {
		return textHeight;
	}

	public void setTextHeight(int textHeight) {
		this.textHeight = textHeight;
	}

	public int getTextWidth() {
		return textWidth;
	}

	public void setTextWidth(int textWidth) {
		this.textWidth = textWidth;
	}

}
