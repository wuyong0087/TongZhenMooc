package com.lib.play_rec.emue;

/**
 * 颜色标识
 * @author Administrator
 */
public enum RecBackgroudTheme {
	WHITE(0x1),
	BLACK(0x1),
	GREEN(0x1),
	KEHAN(0x1);
	
	private int tag;
	
	private RecBackgroudTheme(int tag){
		this.tag =tag;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
	
}
