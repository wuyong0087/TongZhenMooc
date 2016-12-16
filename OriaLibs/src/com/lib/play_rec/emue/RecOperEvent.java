package com.lib.play_rec.emue;

/**
 * 录制界面相关的指令
 */
public enum RecOperEvent {
	
	// 操作相关
	OPER_PEN("画笔",0x1),
	OPER_HAND("手势",0x2),
	OPER_RUBBER("橡皮擦",0x3),
	OPER_UNDO("撤销",0x4),
	OPER_REDO("重置",0x5),
	OPER_PAGE_UP("上一页",0x6),
	OPER_PAGE_DOWN("下一页",0x7),
	OPER_START_REC("录制",0x8),
	OPER_PAUSE("暂停",0x9),
	OPER_EXIT("退出",0xa),
	OPER_SAVE("保存",0xb),
	OPER_CHOOSE("选择",0xc);
	
	private String descript;
	private int tag;
	
	private RecOperEvent(String des,int tag){
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
