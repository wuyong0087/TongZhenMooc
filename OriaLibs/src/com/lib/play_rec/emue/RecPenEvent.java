package com.lib.play_rec.emue;

/**
 * 录制界面相关的指令
 */
public enum RecPenEvent {
	// 画笔粗细
	STROKE_MIN(2), // 画笔粗细最小值：2
	STROKE_MID(4), // 画笔粗细中间值：4
	STROKE_MAX(8); // 画笔粗细最大值：6
	
	private int tag;
	
	private RecPenEvent(int tag){
		this.tag = tag;
	}


	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
	
	@Override
	public String toString() {
		return "__" + this.tag;
	}
	
}
