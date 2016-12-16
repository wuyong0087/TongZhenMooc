package com.lib.play_rec.rec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.lib.play_rec.R;
import com.lib.play_rec.emue.RecBackgroudTheme;
import com.lib.play_rec.emue.RecBtnStatusEvent;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.play.VideoPlayerBaseAty;
import com.lib.play_rec.utils.DateUtil;
import com.lidroid.xutils.BitmapUtils;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主画布的操作
 * 
 * @author Administrator
 */
public class RecordViewGroup extends ViewGroup {

	private VideoPlayerBaseAty context;
	// 操作类型
	private int operate = Config.OPER_PEN;
	// 文本颜色
	public int editTextColor = Color.BLACK;
	public int editTextSize = 26;
	// 图片
	private RecordImageView wacomImageView = null;
	// 文本
	private RecordEditText wacomEditText = null;
	// 历史操作集合
	public HistoryOperater historyOperater;
	// 所有操作的mid集合
	public ArrayList<Integer> memoryMids = new ArrayList<Integer>();
	// 用于保存每一个新加的View的集合
	public Map<Integer, View> viewMap = new HashMap<Integer, View>();
	// 当前纸条的指令
	private int recTheme;
	// 事件分发总线
	private EventBus eventBus;
	private boolean isRegister;

	public RecordViewGroup(VideoPlayerBaseAty context) {
		super(context);
		this.context = context;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.setLayoutParams(params);
		historyOperater = new HistoryOperater(this);
		eventBus = EventBus.getDefault();

	}

	/** 设置背景 */
	public void setBackground(RecBackgroudTheme theme) {
		switch (theme) {
		case WHITE:
			this.setBackgroundColor(Color.rgb(255, 255, 255));
			break;
		case BLACK:
			this.setBackgroundColor(Color.rgb(13, 13, 13));
			break;
		case GREEN:
			this.setBackgroundColor(Color.rgb(0, 86, 31));
			break;
		case KEHAN:
			this.setBackgroundResource(R.drawable.bgset_paper_big_bg);
			break;
		}
	}

//	public void setBackground(int order, boolean save, boolean refresh) {
//		if (context.backgroundOrder == order) {
//			return;
//		}
//		context.backgroundOrder = order;
//		if (refresh) {
//			switch (context.backgroundOrder) {
//			case 1:
//				this.setBackgroundColor(Color.rgb(255, 255, 255));
//				break;
//			case 2:
//				this.setBackgroundColor(Color.rgb(13, 13, 13));
//				break;
//			case 3:
//				this.setBackgroundColor(Color.rgb(0, 86, 31));
//				break;
//			case 4:
//				this.setBackgroundResource(R.drawable.bgset_paper_big_bg);
//				break;
//			}
//		}
//		if (save) {
//			// 保存记录
//			JsonOperater.getInstance()
//					.changeBackground(context.backgroundOrder);
//		}
//	}

	/** 获取背景序号 */
	public int getBackgroundOrder() {
		return this.recTheme;
	}
	
	public void setBackgroudOrder(int order) {
		this.recTheme = order;
	}
	
	// 设置操作类型
	public void setOperate(int operate) {
		this.operate = operate;
	}

	public int getOperate() {
		return operate;
	}

	/** 翻页变量 */
	public int n = 0;
	public int m = 0;
	public int j = 0;// 用于判断前进操作是否为可点击
	public boolean preBtnIsTrue = false;// 用于判断在来回翻页的时候判断向钱操作是否可以点击

	/** 当画笔写了之后调用此方法，n变量自增长 */
	public void addPencilNum() {
		eventBus.post(RecBtnStatusEvent.UNDO_ON_REDO_OFF);
		m = n++;
	}

	/** 一键恢复之后回退数量 */
	public void setDelPencilNum(int size) {
		eventBus.post(RecBtnStatusEvent.UNDO_ON_REDO_OFF);
		m = size - 1;
		n = size;
		j = 0;
	}
	
	// 回退操作
	public void undo() {
		n--;
		j++;
		if (n < 1) {
			eventBus.post(RecBtnStatusEvent.REDO_ON_UNDO_OFF);
			preBtnIsTrue = true;
		} else {
			eventBus.post(RecBtnStatusEvent.REDO_ON);
			preBtnIsTrue = true;
		}
		historyOperater.undo(context.wacomView);
	}

	// 向前操作
	public void redo() {
		n++;
		j--;
		if (n > m) { // 改变颜色背景
			eventBus.post(RecBtnStatusEvent.UNDO_ON_REDO_OFF);
			preBtnIsTrue = false;
		} else {
			eventBus.post(RecBtnStatusEvent.UNDO_ON);
		}
		historyOperater.redo(context.wacomView);
	}

	/**
	 * 重新绘制操作
	 */
	public void resetHistory(ArrayList<HistoryMemory> resetMemerys) {
		n++;
		j--;
		if (n > m) { // 改变颜色背景
			eventBus.post(RecBtnStatusEvent.UNDO_ON_REDO_OFF);
			preBtnIsTrue = false;
		} else {
			eventBus.post(RecBtnStatusEvent.UNDO_ON);
		}
		historyOperater.repeatList = resetMemerys;
		historyOperater.redo(context.wacomView);
	}

	// 增加到回退的操作集合
	public void delPath(HistoryMemory memory) {
		historyOperater.delPath(memory);
	}

	public void delMemories() {
		historyOperater.delMemories(context.wacomView);
	}

	/** 新增一个文本编辑框 */
	public void initEditText(float x, float y, String text, int mid) {
		wacomEditText = new RecordEditText(context, this, x, y, editTextColor,
				editTextSize, text, mid);
		wacomEditText.setFocusable(false);
		wacomEditText.setFocusableInTouchMode(false);
		this.addView(wacomEditText);
		// 让文本控件在绘图界面的下方
		bringChildToFront(context.wacomView);
		context.wacomView.postInvalidate();
		wacomEditText.setFocusable(true);
		wacomEditText.setFocusableInTouchMode(true);
		wacomEditText.requestFocus();
		wacomEditText.requestFocusFromTouch();
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(wacomEditText, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 把文本控件加入历史记录
	 * @param
	 */
	public void saveEditText() {
		viewMap.put(wacomEditText.getMid(), wacomEditText);
		HistoryMemory memory = new HistoryMemory(2);
		memory.setOperaterType(Config.OPER_TEXT_ADD);
		memory.setRecordText(wacomEditText);
		historyOperater.create(memory);
	}

	/** 删除文本编辑框 */
	public void delEditText(RecordEditText met, boolean save) {
		viewMap.remove(met.getMid());
		this.removeView(met);

		if (save) {
			HistoryMemory memory = new HistoryMemory(2);
			memory.setStartTime(System.currentTimeMillis());
			memory.setEndTime(System.currentTimeMillis());
			memory.setOperaterType(Config.OPER_TEXT_DELETE);
			memory.setRecordText(met);
			historyOperater.remove(memory);
		}
	}

	/** 添加文本编辑框 */
	public void addEditText(RecordEditText met) {
		this.addView(met);
		// 让文本控件在绘图界面的下方
		bringChildToFront(context.wacomView);
		context.wacomView.postInvalidate();
		viewMap.put(met.getMid(), met);
	}

	/** 修改文本 */
	public void modifyEditText(RecordEditText met, String oldText,
			String newText, int oldSize, int newSize, int oldColor,
			int newColor, boolean save) {
		met.setTextStr(newText);
		met.setSize(newSize);
		met.setColor(newColor);

		HistoryMemory memory = new HistoryMemory(2);
		memory.setStartTime(System.currentTimeMillis());
		memory.setOperaterType(Config.OPER_TEXT_EDIT);
		memory.setRecordText(met);
		memory.setOldText(oldText);
		memory.setNewText(newText);
		memory.setOldSize(oldSize);
		memory.setNewSize(newSize);
		memory.setOldColor(oldColor);
		memory.setNewColor(newColor);
		memory.setEndTime(System.currentTimeMillis());

		met.setOldWidth(met.getWidth());
		met.setOldHeight(met.getHeight());
		if (save) {
			historyOperater.modifyText(memory);
		}
	}

	/** 新增一张图片 */
	public void initImageView(Bitmap bmp, String imgName, int mid) {
		if (bmp == null) {
			return;
		}
		imgName = imgName.substring(0, imgName.lastIndexOf("."))
				+ Config.LOCAL_REAL_IMAGE_SUFFIX;
		wacomImageView = new RecordImageView(context, this, bmp, imgName, mid);
		JsonOperater.getInstance().getFileList().add(imgName);
		this.addView(wacomImageView);

		// 让图片控件在绘图界面的下方
		bringChildToFront(context.wacomView);
		context.wacomView.postInvalidate();
	}

	/** 新增一张图片 */
	public boolean initImageView2(ViewPager paintPager, String filePath,
			String imgName, int mid) {
		if (filePath == null) {
			return false;
		}
		imgName = imgName.substring(0, imgName.lastIndexOf("."))
				+ Config.LOCAL_REAL_IMAGE_SUFFIX;
		try {
			wacomImageView = new RecordImageView(paintPager, context, this,
					filePath, imgName, mid);
		} catch (Exception e) {
			return false;
		}
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(wacomImageView, filePath);
		JsonOperater.getInstance().getFileList().add(imgName);
		this.addView(wacomImageView);
		// 让图片控件在绘图界面的下方
		bringChildToFront(context.wacomView);
		context.wacomView.postInvalidate();
		return true;
	}

	/** 把图片框加入历史记录 */
	public void saveImageView() {
		viewMap.put(wacomImageView.getMid(), wacomImageView);
		HistoryMemory memory = new HistoryMemory(3);
		memory.setOperaterType(Config.OPER_IMG_ADD);
		memory.setMimg(wacomImageView);
		historyOperater.create(memory);
	}

	/** 删除一张图片 */
	public void delImageView(RecordImageView imgView, boolean save) {
		viewMap.remove(imgView.getMid());
		this.removeView(imgView);
		if (save) {
			// 如果需要保存到历史记录
			HistoryMemory memory = new HistoryMemory(3);
			memory.setStartTime(System.currentTimeMillis());
			memory.setEndTime(System.currentTimeMillis());
			memory.setOperaterType(Config.OPER_IMG_DELETE);
			memory.setMimg(imgView);
			historyOperater.remove(memory);
		}
	}

	/** 添加图片 */
	public void addImageView(RecordImageView mimg) {
		this.addView(mimg);
		// 让文本控件在绘图界面的下方
		bringChildToFront(context.wacomView);
		context.wacomView.postInvalidate();
		viewMap.put(mimg.getMid(), mimg);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		measureChildren(widthSize, heightSize);
		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 只有android:windowSoftInputMode="adjustResize"且非全屏模式，键盘弹出才执行
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// 遍历所有子视图
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			if (child instanceof RecordView) {
				child.layout(0, 0, right - left, bottom - top);
			} else if (child instanceof RecordEditText) {
				RecordEditText childEt = (RecordEditText) child;

				// 获取在onMeasure中计算的视图尺寸
				int measureHeight = childEt.getMeasuredHeight();
				int measuredWidth = childEt.getMeasuredWidth();
				childEt.layout((int) (childEt.posx), (int) (childEt.posy),
						(int) (measuredWidth + childEt.posx),
						(int) (childEt.posy + measureHeight));
				if (childEt.getRight() > this.getRight()) {
					childEt.setMaxWidth((int) (this.getWidth() - childEt
							.getLeft()));
				}
			} else if (child instanceof RecordImageView) {
				RecordImageView childImg = (RecordImageView) child;
				childImg.cusLayout();
			}
		}
	}

	/** 添加图片 */
	public void addImageView(HistoryMemory memory) {
		int mid = memory.getMid();
		if (memoryMids.contains(mid)) {
			return;
		}
		memoryMids.add(mid);
		String imgName = memory.getImgName();
		Bitmap bitmap = memory.getBitmap();
		if (bitmap == null) {
			return;
		}
		float[] move = null;
		if (memory.getMoveList().size() > 0) {
			move = memory.getMoveList().get(memory.getMoveList().size() - 1);
		}
		float scale = (float) 1.0;
		List<Float> scaleList = memory.getScaleList();
		for (int i = 0; i < scaleList.size(); i++) {
			scale *= scaleList.get(i);
		}
		List<Float> rotateList = memory.getRotateList();
		imgName = imgName.substring(0, imgName.lastIndexOf("."))
				+ Config.LOCAL_REAL_IMAGE_SUFFIX;
		wacomImageView = new RecordImageView(move, scale, rotateList, context,
				this, bitmap, imgName, mid);
		JsonOperater.getInstance().getFileList().add(imgName);
		this.addView(wacomImageView);
		// 让图片控件在绘图界面的下方
		bringChildToFront(context.wacomView);
		context.wacomView.postInvalidate();

		memory.setStartTime(DateUtil.getTimeMillisLong());
		memory.setEndTime(DateUtil.getTimeMillisLong());
		memory.setMimg(wacomImageView);
		JsonOperater.getInstance().imgAdd(wacomImageView);
		int size = rotateList.size();
		if (size > 0) {// 如果有旋转操作则插入到json数据
			for (int i = 0; i < size; i++) {
				memory.getRotateList().clear();
				memory.getRotateList().add((float) 1.570796);// 顺时针旋转90度
				JsonOperater.getInstance().imgRotate(memory);
			}
		}
	}

	/** 添加文本 */
	public void addEditText(HistoryMemory memory) {
		int mid = memory.getMid();
		if (memoryMids.contains(mid)) {
			return;
		}
		memoryMids.add(mid);
		float[] point = memory.getPoint();
		if (memory.getMoveList().size() > 0) {
			point = memory.getMoveList().get(memory.getMoveList().size() - 1);
		}
		float pointX = point[0];
		float pointY = point[1];
		// float pointX = point[0] - (memory.getTextWidth() / 2);
		// float pointY = point[1] - (memory.getTextHeight() / 2);
		wacomEditText = new RecordEditText(context, this, pointX, pointY,
				memory.getOldColor(), memory.getOldSize(), memory.getOldText(),
				mid);
		wacomEditText.setFocusable(false);
		wacomEditText.setFocusableInTouchMode(false);
		wacomEditText.clearFocus();
		memory.setRecordText(wacomEditText);
		this.addView(wacomEditText);
		// 让文本控件在绘图界面的下方
		bringChildToFront(context.wacomView);
		context.wacomView.postInvalidate();

		// TextPaint mTextPaint = wacomEditText.getPaint();
		// int byteSize = memory.getOldText().getBytes().length;
		// float textWidth = mTextPaint.measureText(memory.getOldText());
		// float textHeight = textWidth / (byteSize / 2);
		JsonOperater.getInstance().textAdd(pointX, pointY, memory);
	}

	/**
	 * 当activity调用onResume时调用该方法
	 */
	@Override
	protected void onAttachedToWindow() {
		if (!isRegister) {
			eventBus.register(this);
			isRegister = true;
		}
		super.onAttachedToWindow();
	}

	/**
	 * 当activity调用onDestroy时调用该方法
	 */
	@Override
	protected void onDetachedFromWindow() {

		if (isRegister) {
			eventBus.unregister(this);
			isRegister = false;
		}

		super.onDetachedFromWindow();
	}
	
	public void onEventAsync(RecordViewGroup group){
		
	}
	
}