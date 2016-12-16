package com.lib.play_rec.rec;

import android.app.Activity;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.listener.EditOperateListener;

public class RecordEditText extends EditText {
	/** 父控件传过来的值 */
	private RecordViewGroup group;
	private int width; // 父控件的宽度
//	private AtyRecord context;

	/** touch事件相关 */
	private float start_x, start_y, current_x, current_y;// 触摸位置
	private float down_x, down_y;

	/** 边界控制 */
	public final int maxTextSize = 109, minTextSize = 17; // 文字最大最小值
	public final int sizeChangeMount = 3; // 文本大小每次变化的量

	/** 对象属性 */
	private int mid; // 自身id

	private int color; // 文本颜色
	private int size = 30; // 文字大小
	public float posx, posy; // 控件的位置
	private boolean lock = false; // 是否锁定
	private int etWidth, etHeight; // 控件宽度和高度
	private String textStr = ""; // 文本内容

	private int keyboardHeight = 0; // 键盘高度

	private EditOperateListener editOperater = null;;
	private float[] move = null;
	private HistoryMemory memory = null;
	private int changeWidth;
	private boolean changeSize = false;

	// undoEditText时的数据
	private int oldWidth, oldHeight;
	private String oldText;
	private int oldSize;
	private int oldColor;

	public RecordEditText(final Activity context, RecordViewGroup viewGroup,
			float x, float y, int textColor, int textSize, String text, int mid) {
		super(context);
//		this.context = context;
		this.group = viewGroup;
		this.width = viewGroup.getWidth();
		this.posx = x;
		this.posy = y;
		this.color = textColor;
		this.size = textSize;
		this.textStr = text;
		this.mid = mid;

		editOperater = new EditOperateListener(context);

		this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		this.setTextColor(color);
		this.setText(text);
		this.setMaxWidth((int) (width - x));

		LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
		this.setLayoutParams(pa);
		this.setHorizontallyScrolling(false);
		this.setBackgroundResource(R.drawable.edit_bg); // 文本框背景
		this.setPadding(4, 7, 9, 7);

		// 设置横屏时键盘不全屏
		this.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		/** 用于监听键盘的显示与隐藏 */
		this.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (!RecordEditText.this.hasFocus()) {
							return;
						}
						Rect r = new Rect();
						RecordEditText.this.getWindowVisibleDisplayFrame(r);
						int screenHeight = RecordEditText.this.getRootView()
								.getHeight();
						int heightDifference = screenHeight
								- (r.bottom - r.top);
						if (heightDifference > 0) {
							keyboardHeight = heightDifference;
						} else {
							if (keyboardHeight > 0) {
								keyboardHeight = 0;
								/** 让控件失去焦点 */
								RecordEditText.this.setFocusable(false);
								RecordEditText.this
										.setFocusableInTouchMode(false);
								RecordEditText.this.clearFocus();
								((RecordViewGroup) (RecordEditText.this
										.getParent()))
										.setOperate(Config.OPER_SELECT);
								// context.clearOperaterBtnSelBg();
								// context.handBtn.setBackgroundResource(R.drawable.imgbtn_sel_bg);
								// 判断文本是否为空
								if (RecordEditText.this.getText() == null
										|| "".equals(RecordEditText.this
												.getText().toString())) {
									if ("".equals(textStr)) {
										// 如果是第一次创建，则不保存文本
									} else {
										// 如果不是第一次创建，则记录一个删除操作
										group.delEditText(RecordEditText.this,
												true);
									}
								} else {
									if ("".equals(textStr)) {
										// 如果是第一次创建，则保存文本
										textStr = RecordEditText.this.getText()
												.toString();
										group.saveEditText();
									} else {
										// 如果不是第一次创建，则记录一个修改操作
										if (textStr.equals(RecordEditText.this
												.getText().toString())) {
											// 如果两次文本相同，则不修改
										} else {
											group.modifyEditText(
													RecordEditText.this,
													textStr,
													RecordEditText.this
															.getText()
															.toString(), size,
													size, color, color, true);
											textStr = RecordEditText.this
													.getText().toString();
										}
									}
								}
							}
						}
					}
				});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			start_x = event.getRawX();
			start_y = event.getRawY();
			down_x = event.getRawX();
			down_y = event.getRawY();
			// 记录位置
			startMoveText();
			break;
		case MotionEvent.ACTION_MOVE:
			if (lock) { // 如果锁定，则不执行移动操作
				break;
			}
			current_x = event.getRawX();
			current_y = event.getRawY();
			// 处理拖动
			if (Math.abs(current_x - start_x) < 8
					&& Math.abs(current_y - start_y) < 8) {
				break;
			}
			posx = posx + current_x - start_x;
			posy = posy + current_y - start_y;
			// 记录位置
			moveText();
			start_x = current_x;
			start_y = current_y;
			break;
		case MotionEvent.ACTION_UP:
			if (lock) { // 如果锁定，则直接弹出操作工具栏
				etWidth = this.getWidth();
				etHeight = this.getHeight();
				editOperater.showPopupWindow(group, this);
			} else if (Math.abs(event.getRawX() - down_x) < 8
					&& Math.abs(event.getRawY() - down_y) < 8) {
				etWidth = this.getWidth();
				etHeight = this.getHeight();
				editOperater.showPopupWindow(group, this);
			} else {
				endMoveText();
			}
		}
		return true;
	}

	/** 移动文本 */
	public void startMoveText() {
		memory = new HistoryMemory(2);
		memory.setStartTime(System.currentTimeMillis());
		move = new float[] { getMidWidth(), getMidHeight() };
		memory.getMoveList().add(move);
		memory.getTimeList().add(System.currentTimeMillis());
	}

	public void moveText() {
		if (this.getWidth() >= changeWidth || changeSize) {
			changeWidth = this.getWidth();
			changeSize = false;
		}
		this.layout((int) posx, (int) posy, (int) (posx + changeWidth),
				(int) (posy + this.getHeight()));
		move = new float[] { getMidWidth(), getMidHeight() };
		memory.getMoveList().add(move);
		memory.getTimeList().add(System.currentTimeMillis());
	}

	public void endMoveText() {
		memory.setEndTime(System.currentTimeMillis());
		memory.setOperaterType(Config.OPER_TEXT_MOVE);
		memory.setRecordText(this);
		group.historyOperater.setMove(memory);
	}

	/** 以文本中心点进行布局 */
	public void setLayoutByMid(float left, float top) {
		posx = left - this.getWidth() / 2f;
		posy = top - this.getHeight() / 2f;
		this.layout((int) posx, (int) posy, (int) (posx + changeWidth),
				(int) (posy + this.getHeight()));
	}

	public void setLayout() {
		this.layout((int) posx, (int) posy, (int) (posx + changeWidth),
				(int) (posy + this.getHeight()));
	}

	// 获取文本中心点位置
	private float getMidWidth() {
		return posx + this.getWidth() / 2f;
	}

	private float getMidHeight() {
		return posy + this.getHeight() / 2f;
	}

	/** 缩小文字大小,返回是否可继续缩小 */
	public boolean reduceTextSize() {
		if (size <= minTextSize) {
			size = minTextSize;
			return false;
		} else {
			setSize(size - sizeChangeMount);
			if (size <= minTextSize) {
				return false;
			} else {
				return true;
			}
		}
	}

	/** 放大文字大小 ，放回是否可继续放大 */
	public boolean enlargeTextSize() {
		if (size >= maxTextSize) {
			size = maxTextSize;
			return false;
		} else {
			setSize(size + sizeChangeMount);
			if (size >= maxTextSize) {
				return false;
			} else {
				return true;
			}
		}
	}

	/** 设置文本颜色 */
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
		this.setTextColor(color);
		group.editTextColor = color; // 保存字体颜色到父控件
	}

	/** 设置文本大小 */
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		changeSize = true;
		this.size = size;
		this.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		group.editTextSize = size; // 保存字体大小到父控件
	}

	/** 设置文本 */
	public String getTextStr() {
		return textStr;
	}

	public void setTextStr(String textStr) {
		this.textStr = textStr;
		this.oldText = this.getTextStr();
		this.setText(textStr);
	}

	/** 设置控件锁定 */
	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public int getMid() {
		return mid;
	}

	public int getEtWidth() {
		return etWidth;
	}

	public int getEtHeight() {
		return etHeight;
	}
	
	public int getOldColor() {
		return oldColor;
	}

	public void setOldColor(int oldColor) {
		this.oldColor = oldColor;
	}

	public int getOldSize() {
		return oldSize;
	}

	public void setOldSize(int oldSize) {
		this.oldSize = oldSize;
	}

	public int getOldWidth() {
		return oldWidth;
	}

	public void setOldWidth(int oldWidth) {
		this.oldWidth = oldWidth;
	}

	public int getOldHeight() {
		return oldHeight;
	}

	public void setOldHeight(int oldHeight) {
		this.oldHeight = oldHeight;
	}

	public String getOldText() {
		return oldText;
	}

	public void setOldText(String oldText) {
		this.oldText = oldText;
	}

	public interface OnFinishComposingListener {
		public void finishComposing();
	}
}
