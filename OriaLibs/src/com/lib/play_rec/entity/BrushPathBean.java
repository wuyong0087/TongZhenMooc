package com.lib.play_rec.entity;

import com.lib.play_rec.play.PlayHistoryMemory;
import com.lib.play_rec.rec.HistoryMemory;

public class BrushPathBean {

	private float sx;// 起始X坐标
	private float sy;// 起始Y坐标
	private float ex;// 结束X坐标
	private float ey;// 结束Y坐标
	private int currPager;// 当前页
	private com.lib.play_rec.rec.HistoryMemory memory;// 记录Path路径的对象(录微课)
	private PlayHistoryMemory memoryPlayer;// 记录Path路径的对象(播放微课)

	public BrushPathBean(float sx, float sy, float ex, float ey,int currPager,
			HistoryMemory memory) {
		super();
		this.sx = sx;
		this.sy = sy;
		this.ex = ex;
		this.ey = ey;
		this.setCurrPager(currPager);
		this.memory = memory;
	}

	public BrushPathBean(float sx, float sy, float ex, float ey,
			PlayHistoryMemory memoryPlayer) {
		super();
		this.sx = sx;
		this.sy = sy;
		this.ex = ex;
		this.ey = ey;
		this.memoryPlayer = memoryPlayer;
	}

	public float getSx() {
		return sx;
	}

	public void setSx(float sx) {
		this.sx = sx;
	}

	public float getEx() {
		return ex;
	}

	public void setEx(float ex) {
		this.ex = ex;
	}

	public float getSy() {
		return sy;
	}

	public void setSy(float sy) {
		this.sy = sy;
	}

	public float getEy() {
		return ey;
	}

	public void setEy(float ey) {
		this.ey = ey;
	}

	public HistoryMemory getMemory() {
		return memory;
	}

	public void setMemory(HistoryMemory memory) {
		this.memory = memory;
	}

	public PlayHistoryMemory getMemoryPlayer() {
		return memoryPlayer;
	}

	public void setMemoryPlayer(PlayHistoryMemory memoryPlayer) {
		this.memoryPlayer = memoryPlayer;
	}

	public int getCurrPager() {
		return currPager;
	}

	public void setCurrPager(int currPager) {
		this.currPager = currPager;
	}
}
