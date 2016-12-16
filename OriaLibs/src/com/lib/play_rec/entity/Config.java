package com.lib.play_rec.entity;


/**
 * 配置类
 * @author Administrator
 */
public class Config {
	//　项目存储的根路径
//	public static String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "skrecord" + File.separator;
	public static String filePath;
	// 图片资源名称
	public static String imgFileName;
	// 文件命名相关
	// 图片
	public static final String LOCAL_REAL_IMAGE_SUFFIX = ".jpg";
	public static final String LOCAL_IMAGE_SUFFIX = ".jpg";
	// 音频
	public static final String LOCAL_REAL_AUDIO_SUFFIX = ".mp3";
	public static final String LOCAL_AUDIO_SUFFIX = ".mp3";
	// 文本
	public static final String LOCAL_TEXT_SUFFIX = ".txt";
	public static final String LOCAL_REAL_TEXT_SUFFIX = ".txt";
	
	// 橡皮擦的宽度
	public static final int RUBBER_STROKE = 20;
	
	// 是否是蓝牙笔操作
	public static boolean isDigital = false;
	/** 操作类型相关 **/
	// 画笔类
	public static final int OPER_NONE = 0x0;
	public static final int OPER_PEN = 0x1;
	public static final int OPER_UNPEN = 0x2;
	public static final int OPER_REPEN = 0x3;
	public static final int OPER_RUBBER = 0x4;
	public static final int OPER_UNRUBBER = 0x5;
	public static final int OPER_RERUBBER = 0x6;
	public static final int OPER_DIGITAL = 0x7;
	public static final int OPER_HAND = 0x8;
	// 图片
	public static final int OPER_IMG_SCALE = 0x9;
	public static final int OPER_IMG_UNSCALE = 0xa;
	public static final int OPER_IMG_RESCALE = 0xb;
	public static final int OPER_IMG_MOVE = 0xc;
	public static final int OPER_IMG_UNMOVE = 0xd;
	public static final int OPER_IMG_REMOVE = 0xe;
	public static final int OPER_IMG_ROTATE = 0xf;
	public static final int OPER_IMG_UNROTATE = 0x10;
	public static final int OPER_IMG_REROTATE = 0x11;
	public static final int OPER_IMG_DELETE = 0x12;
	public static final int OPER_IMG_UNDELETE = 0x13;
	public static final int OPER_IMG_REDELETE = 0x14;
	public static final int OPER_IMG_ADD = 0x15;
	public static final int OPER_IMG_UNADD = 0x16;
	public static final int OPER_IMG_READD = 0x17;
	public static final int OPER_IMG_LOCK = 0x18;
	// 文本
	public static final int OPER_TEXT_ADD = 0x19;
	public static final int OPER_TEXT_UNADD = 0x1a;
	public static final int OPER_TEXT_READD = 0x1b;
	public static final int OPER_TEXT_DELETE = 0x1c;
	public static final int OPER_TEXT_UNDELETE = 0x1d;
	public static final int OPER_TEXT_REDELETE = 0x1e;
	public static final int OPER_TEXT_EDIT = 0x1f;
	public static final int OPER_TEXT_UNEDIT = 0x20;
	public static final int OPER_TEXT_REEDIT = 0x21;
	public static final int OPER_TEXT_MOVE = 0x22;
	public static final int OPER_TEXT_UNMOVE = 0x23;
	public static final int OPER_TEXT_REMOVE = 0x24;
	public static final int OPER_TEXT_LOCK = 0x25;
	
	public static final int OPER_SELECT = 0x26;
	
	// 操作相关
	public static final int PEN_COLOR_CHANGE = 0x27;
	public static final int PEN_STROKE_CHANGE = 0x28;
	
	// 录制界面的方向，及录制的类型
	public static final int RECORDE_VIRTICAL = 0x29;
	public static final int RECORDE_HORIZONTAL = 0x2a;
	
	// 翻页
	public static final int OPER_NEWPAGE = 0x2b;
	public static final int OPER_FLIPPAGE = 0x2c;
	public static final int OPER_COPYPAGE = 0x2d;
	//改变背景
	public static final int OPER_CHANGE_BG = 0x2e;
	
	// 撤销和重做
	public static final int OPER_UNDO = 0x2f;
	public static final int OPER_REDO = 0x30;
	
	// 光标
	public static final int OPER_CURSOR = 0x31;
	
	// 画笔扩展类
	public static final int OPER_ARROW = 0x32;
	public static final int OPER_ELLIPSE = 0x33;
	public static final int OPER_RECT = 0x34;
	public static final int OPER_LINE = 0x35;
	public static final int OPER_CLEAN_RECT = 0x36;
	
	/** 指令的名称 */
	public static final String ORDER_NAME = "order";
	public static final String ORDER_PENCIL = "Pencil";
	public static final String ORDER_UNDO_PENCIL = "UndoPencil";
	public static final String ORDER_REDO_PENCIL = "RedoPencil";
	public static final String ORDER_RUBBER = "Rubber";
	public static final String ORDER_UNDO_RUBBER = "UndoRubber";
	public static final String ORDER_REDO_RUBBER = "RedoRubber";
	public static final String ORDER_ADD_TEXT = "AddText";
	public static final String ORDER_UNDO_ADDTEXT = "UndoAddText";
	public static final String ORDER_REDO_ADDTEXT = "RedoAddText";
	public static final String ORDER_MOVE_TEXT = "MoveText";
	public static final String ORDER_UNDO_MOVETEXT = "UndoMoveText";
	public static final String ORDER_REDO_MOVETEXT = "RedoMoveText";
	public static final String ORDER_EDIT_TEXT = "EditText";
	public static final String ORDER_UNDO_EDITTEXT = "UndoEditText";
	public static final String ORDER_REDO_EDITTEXT = "RedoEditText";
	public static final String ORDER_DELETE_TEXT = "DeleteText";
	public static final String ORDER_UNDO_DELETE_TEXT = "UndoDeleteText";
	public static final String ORDER_REDO_DELETE_TEXT = "RedoDeleteText";
	public static final String ORDER_CURSOR = "Cursor";
	
	/** 翻页相关 */
	public static final String ORDER_NEWPAGE = "NewPage";
	public static final String ORDER_FLIPPAGE = "FlipPage";
	public static final String ORDER_COPYPAGE = "CopyPage";
	
	public static final String ORDER_ADD_IMAGE = "AddImage";
	public static final String ORDER_UNDO_ADDIMAGE = "UndoAddImage";
	public static final String ORDER_REDO_ADDIMAGE = "RedoAddImage";
	public static final String ORDER_MOVE_IMAGE = "MoveImage";
	public static final String ORDER_UNDO_MOVEIMAGE = "UndoMoveImage";
	public static final String ORDER_REDO_MOVEIMAGE = "RedoMoveImage";
	public static final String ORDER_SCALE_IMAGE = "ScaleImage";
	public static final String ORDER_UNDO_SCALEIMAGE = "UndoScaleImage";
	public static final String ORDER_REDO_SCALEIMAGE = "RedoScaleImage";
	public static final String ORDER_ROTATION_IMAGE = "RotationImage";
	public static final String ORDER_UNDO_ROTATION_IMAGE = "UndoRotationImage";
	public static final String ORDER_REDO_ROTATION_IMAGE = "RedoRotationImage";
	public static final String ORDER_DELETE_IMAGE = "DeleteImage";
	public static final String ORDER_UNDO_DELETE_IMAGE = "UndoDeleteImage";
	public static final String ORDER_REDO_DELETE_IMAGE = "RedoDeleteImage";
	
	public static final String ORDER_BACKGROUND = "Background";
	public static final String ORDER_CLEARPEN = "ClearPen";
	public static final String ORDER_CLEARPAGE = "ClearPage";
	public static final String ORDER_CLEARRECT = "ClearRect";
	public static final String ORDER_ARROW = "Arrow";
	public static final String ORDER_ELLIPSE = "Ellipse";
	public static final String ORDER_LINE = "Line";
	public static final String ORDER_RECT = "Rect";

	public static final String ORDER_START = "Start";
	public static final String ORDER_END = "End";
	
	/** 图片操作变量 */
	public static final int VAIN = 0;
	public static final int DRAG = 1; // 拖动
	public static final int TOWPOINT = 2; // 两点操作
	public static final int ZOOM = 3; // 缩放
	public static final int ROTATE = 5; // 旋转
}
