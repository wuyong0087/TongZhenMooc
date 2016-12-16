package com.lib.play_rec.emue;

/**
 * 图片操作
 * @author Administrator
 */
public enum ImageOper {
	OPER_IMG_SCALE("ScaleImage",0x9),
	OPER_IMG_UNSCALE("UndoScaleImage",0xa),
	OPER_IMG_RESCALE("RedoScaleImage",0xb),
	OPER_IMG_MOVE("MoveImage",0xc),
	OPER_IMG_UNMOVE("UndoMoveImage",0xd),
	OPER_IMG_REMOVE("RedoMoveImage",0xe),
	OPER_IMG_ROTATE("RotationImage",0xf),
	OPER_IMG_UNROTATE("UndoRotationImage",0x10),
	OPER_IMG_REROTATE("RedoRotationImage",0x11),
	OPER_IMG_DELETE("DeleteImage",0x12),
	OPER_IMG_UNDELETE("UndoDeleteImage",0x13),
	OPER_IMG_REDELETE("RedoDeleteImage",0x14),
	OPER_IMG_ADD("AddImage",0x15),
	OPER_IMG_UNADD("UndoAddImage",0x16),
	OPER_IMG_READD("RedoAddImage",0x17),
	OPER_IMG_LOCK("LockImage",0x18);
	private int tag;
	private String des;
	private ImageOper(String des,int tag){
		this.tag = tag;
		this.des = des;
	}
}
