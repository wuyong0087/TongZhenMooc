package com.lib.play_rec.emue;

import java.io.Serializable;

/**
 * 录制保存事件
 */
public class RecordEvent implements Serializable {
	private int REC_STATUS;
	public static final int REC_START = 0x01;
	public static final int REC_PAUSE = 0x02;
	public static final int REC_SAVE = 0x03;
	public static final int REC_RUSH = 0x04;
	public static final int REC_RESTART = 0x05;

	private int WorksId;
	public RecordEvent() { }
	public RecordEvent(int REC_STATUS) {
		this.REC_STATUS = REC_STATUS;
	}

	public RecordEvent(int worksId, int REC_STATUS) {
		WorksId = worksId;
		this.REC_STATUS = REC_STATUS;
	}

	public int getWorksId() {
		return WorksId;
	}

	public void setWorksId(int worksId) {
		WorksId = worksId;
	}

	public int getREC_STATUS() {
		return REC_STATUS;
	}

	public void setREC_STATUS(int REC_STATUS) {
		this.REC_STATUS = REC_STATUS;
	}
}
