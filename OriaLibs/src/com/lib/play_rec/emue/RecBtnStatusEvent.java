package com.lib.play_rec.emue;

/**
 * 按钮状态
 * @author Administrator
 *
 */
public enum RecBtnStatusEvent {
	REDO_ON_UNDO_OFF(0x1),
	REDO_ON(0x2),
	UNDO_ON_REDO_OFF(0x3),
	UNDO_ON(0x4);
	
	int tag;
	private RecBtnStatusEvent(int tag){
		this.tag  =tag;
	} 
}
