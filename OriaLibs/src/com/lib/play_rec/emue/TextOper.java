package com.lib.play_rec.emue;

public enum TextOper {
	
	OPER_TEXT_ADD("文本添加",0x19),
	OPER_TEXT_UNADD("撤销文本添加",0x1a),
	OPER_TEXT_READD("重置文本添加",0x1b),
	OPER_TEXT_DELETE("文本删除",0x1c),
	OPER_TEXT_UNDELETE("撤销文本删除",0x1d),
	OPER_TEXT_REDELETE("重置文本删除",0x1e),
	OPER_TEXT_EDIT("文本编辑",0x1f),
	OPER_TEXT_UNEDIT("撤销文本编辑",0x20),
	OPER_TEXT_REEDIT("重置文本编辑",0x21),
	OPER_TEXT_MOVE("文本移动",0x22),
	OPER_TEXT_UNMOVE("撤销文本移动",0x23),
	OPER_TEXT_REMOVE("重置文本移动",0x24),
	OPER_TEXT_LOCK("问锁定",0x25);
	
	private int tag;
	private String des;
	
	private TextOper(String des,int tag){
		this.setTag(tag);
		this.setDes(des);
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
}
