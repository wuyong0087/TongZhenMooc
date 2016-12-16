package com.lib.play_rec.play;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Paint;
import android.graphics.Path;

public class PlayHistoryMemory {

	private int mid = -1;
	private int type; // 类型，1-画笔 2-文本 3-图片 4-箭头
	private int operaterType; // 操作类型
	// ------------文本相关------------
	private PlayEditText met = null;
	private int oldColor, newColor; // 文本颜色
	private int oldSize, newSize; // 文字大小
	private int oldWidth, newWidth; // 文本宽度
	private int oldHeight, newHeight; // 文本高度
	private String oldText = null, newText = null; // 文本内容

	// -------------图片相关--------------
	private PlayImageView mimg = null;
	private List<Float> scaleList; // 开始缩放位置
	private List<Float> rotateList;//开始旋转位置

	// ----------画笔相关---------
	public Path path; // 路径
	public Paint paint; // 画笔
	public long startTime;
	public long endTime;
	public List<float[]> dataList;
	private int penSorce = 1; // 画笔的来源:1-手写，默认为该值，2-数码笔。
	
	public int getPenSorce() {
		return penSorce;
	}

	public void setPenSorce(int penSorce) {
		this.penSorce = penSorce;
	}

	// 与Rubber相交的画线的id
	private String ids;
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	// ----------箭头相关---------
	public Paint arrowpaint;
	public List<float[]> arrowDataList;

	// --------矩形相关----------
	private Paint rectPaint;
	private List<float[]> rectDataList;

	// --------椭圆相关----------
	private Paint ovalPaint;
	private List<float[]> ovalDataList;
	
	// --------直线相关----------
	private Paint linePaint;
	private List<float[]> lineDataList;

	// --------直线相关----------
	private Paint cursorPaint;
	private List<float[]> cursorDataList;
	
	// ----------------公共属性------------
	private List<float[]> moveList; // 开始移动位置
	private List<Long> timeList; // 时间列表
	private boolean lock = false; // 控件是否锁定

	public PlayHistoryMemory(int type) {
		this.type = type;
		timeList = new ArrayList<Long>();
		switch (type) {
		case 1:
			dataList = new ArrayList<float[]>();
			break;
		case 2:
			moveList = new ArrayList<float[]>();
			break;
		case 3:
			moveList = new ArrayList<float[]>();
			scaleList = new ArrayList<Float>();
			setRotateList(new ArrayList<Float>());
			break;
		case 4:
			arrowDataList = new ArrayList<float[]>();
			break;
		case 5:
			ovalDataList = new ArrayList<float[]>();
			break;
		case 6:
			rectDataList = new ArrayList<float[]>();
			break;
		case 7:
			lineDataList = new ArrayList<float[]>();
			break;
		case 8:
			cursorDataList = new ArrayList<float[]>();
			break;
		}
	}

	public PlayHistoryMemory(){}
	
	public Paint getCursorPaint() {
		return cursorPaint;
	}

	public void setCursorPaint(Paint cursorPaint) {
		this.cursorPaint = cursorPaint;
	}

	public List<float[]> getCursorDataList() {
		return cursorDataList;
	}

	public void setCursorDataList(List<float[]> cursorDataList) {
		this.cursorDataList = cursorDataList;
	}

	public Paint getArrowpaint() {
		return arrowpaint;
	}
	
	public Paint getLinePaint() {
		return linePaint;
	}

	public void setLinePaint(Paint linePaint) {
		this.linePaint = linePaint;
	}

	public List<float[]> getLineDataList() {
		return lineDataList;
	}

	public void setLineDataList(List<float[]> lineDataList) {
		this.lineDataList = lineDataList;
	}

	public void setArrowpaint(Paint arrowpaint) {
		this.arrowpaint = arrowpaint;
	}

	public List<float[]> getArrowDataList() {
		return arrowDataList;
	}

	public void setArrowDataList(List<float[]> arrowDataList) {
		this.arrowDataList = arrowDataList;
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

	public PlayEditText getMet() {
		return met;
	}

	public void setMet(PlayEditText met) {
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

	public int getOldWidth() {
		return oldWidth;
	}

	public void setOldWidth(int oldWidth) {
		this.oldWidth = oldWidth;
	}

	public int getNewWidth() {
		return newWidth;
	}

	public void setNewWidth(int newWidth) {
		this.newWidth = newWidth;
	}

	public int getOldHeight() {
		return oldHeight;
	}

	public void setOldHeight(int oldHeight) {
		this.oldHeight = oldHeight;
	}

	public int getNewHeight() {
		return newHeight;
	}

	public void setNewHeight(int newHeight) {
		this.newHeight = newHeight;
	}

	public PlayImageView getMimg() {
		return mimg;
	}

	public void setMimg(PlayImageView mimg) {
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

	public Paint getRectPaint() {
		return rectPaint;
	}

	public void setRectPaint(Paint rectPaint) {
		this.rectPaint = rectPaint;
	}

	public List<float[]> getRectDataList() {
		return rectDataList;
	}

	public void setRectDataList(List<float[]> rectDataList) {
		this.rectDataList = rectDataList;
	}

	public List<float[]> getOvalDataList() {
		return ovalDataList;
	}

	public void setOvalDataList(List<float[]> ovalDataList) {
		this.ovalDataList = ovalDataList;
	}

	public Paint getOvalPaint() {
		return ovalPaint;
	}

	public void setOvalPaint(Paint ovalPaint) {
		this.ovalPaint = ovalPaint;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public List<Float> getRotateList() {
		return rotateList;
	}

	public void setRotateList(List<Float> rotateList) {
		this.rotateList = rotateList;
	}

}
