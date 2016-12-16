package com.lib.play_rec.emue;

/**
 * 录制界面相关的指令
 */
public enum RecMeterialEvent {
	// 素材相关
	PHONE_PIC("",0x1),
	PHONE_CAMERA("",0x2),
	CLOUD_PDF("",0x3),
	CLOUD_PPT("",0x4),
	CLOUD_WORD("",0x5),
	CLOUD_IMAGE("",0x6);
	
	private String descript;
	private int tag;
	
	private RecMeterialEvent(String des,int tag){
		this.descript = des;
		this.tag = tag;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
	
	@Override
	public String toString() {
		return this.descript + "__" + this.tag;
	}
	
}
