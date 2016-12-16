package com.lib.play_rec.rec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lib.play_rec.entity.Config;
import com.lib.play_rec.listener.ImageOperateListener;
import com.lib.play_rec.utils.BitmapUtil;
import com.lib.play_rec.utils.DateUtil;

import java.util.List;

public class RecordImageView extends ImageView {

	/** 父控件传过来的值 */
	private int width, height; // 父控件的宽度和高度
	private RecordViewGroup viewGroup;
	/** touch事件相关 */
	private float start_x, start_y, current_x, current_y;// 触摸位置
	private float down_x, down_y;

	/** 图片操作变量 */
	private int mode = Config.VAIN;
	float oldRotation = 0, newRotation = 0;
	private float oldDistance, newDistance, scale;
	private boolean lockMove = false;

	/** 对象属性 */
	public float posLeft = 0, posTop = 0, posRight = 0, posBottom = 0;
	private float rotation = 0; // 实际旋转的角度
	private boolean lock = false; // 是否锁定
	private String imgName;
	private int mid;

	private ImageOperateListener imageOperate;
	private float[] move;
	private HistoryMemory memory;

	public RecordImageView(ViewPager paintPager, Context context,
			RecordViewGroup viewGroup, String filePath, String imgName, int mid) throws Exception {
		super(context);
		this.viewGroup = viewGroup;
		this.width = paintPager.getWidth();
		this.height = paintPager.getHeight();
		this.imgName = imgName;
		this.mid = mid;
		Bitmap bmp = BitmapUtil.getBitmapFromPath1(filePath);
		if (bmp == null) {
			throw new Exception();
		}
		Bitmap bitmap = BitmapUtil.getScaleBitmap2(bmp, width, height);
		imageOperate = new ImageOperateListener(context);
		LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
		this.setLayoutParams(pa);

		posLeft = (width - bitmap.getWidth()) / 2f;
		posTop = (height - bitmap.getHeight()) / 2f;
		posRight = posLeft + bitmap.getWidth();
		posBottom = posTop + bitmap.getHeight();

		this.setImageBitmap(bitmap);
		this.setScaleType(ScaleType.FIT_XY);

		if (bitmap != null) {
			bitmap.recycle();
		}
	}
	
	public RecordImageView(ViewPager paintPager, Context context,
			RecordViewGroup viewGroup, Bitmap bmp, float startX, float startY) {
		super(context);
		this.viewGroup = viewGroup;
		this.width = paintPager.getWidth();
		this.height = paintPager.getHeight();

		imageOperate = new ImageOperateListener(context);

		LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
		this.setLayoutParams(pa);

		posLeft = startX;
		posTop = startY;

		posRight = posLeft + bmp.getWidth();
		posBottom = posTop + bmp.getHeight();
		this.setImageBitmap(bmp);
		this.setScaleType(ScaleType.FIT_XY);
	}

	public RecordImageView(Context context, RecordViewGroup viewGroup,
			Bitmap bmp, String imgName, int mid) {
		super(context);
		this.viewGroup = viewGroup;
		this.width = viewGroup.getWidth();
		this.height = viewGroup.getHeight();
		this.imgName = imgName;
		this.mid = mid;

		imageOperate = new ImageOperateListener(context);
		LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
		this.setLayoutParams(pa);

		posLeft = (width - bmp.getWidth()) / 2f;
		posTop = (height - bmp.getHeight()) / 2f;
		posRight = posLeft + bmp.getWidth();
		posBottom = posTop + bmp.getHeight();
		this.setImageBitmap(bmp);
		this.setScaleType(ScaleType.FIT_XY);
	}

	public RecordImageView(float[] move, float scale, List<Float> rotateList,
			Context context, RecordViewGroup viewGroup, Bitmap bmp,
			String imgName, int mid) {
		super(context);

		this.viewGroup = viewGroup;
		this.width = viewGroup.getWidth();
		this.height = viewGroup.getHeight();
		this.imgName = imgName;
		this.mid = mid;

		imageOperate = new ImageOperateListener(context);
		LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
		this.setLayoutParams(pa);
		if (move != null) {
			posLeft = move[0] - (bmp.getWidth() / 2f);
			posTop = move[1] - (bmp.getHeight() / 2f);
			posRight = posLeft + bmp.getWidth();
			posBottom = posTop + bmp.getHeight();
		} else {
			posLeft = (width - bmp.getWidth()) / 2f;
			posTop = (height - bmp.getHeight()) / 2f;
			posRight = posLeft + bmp.getWidth();
			posBottom = posTop + bmp.getHeight();
		}
		if (scale != 1.0) {
			this.setScale(scale);
		}
		this.setImageBitmap(bmp);
		this.setScaleType(ScaleType.FIT_XY);
		float total = 0;
		for (int i = 0; i < rotateList.size(); i++) {
			float rotation = (rotateList.get(i) / (float) (2 * Math.PI)) * 360;
			 total += rotation;
		}
		this.setRotation(total);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			start_x = event.getRawX();
			start_y = event.getRawY();
			down_x = event.getRawX();
			down_y = event.getRawY();
			mode = Config.DRAG;
			// 记录位置
			startMove();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			if (lockMove) {
				break;
			}
			// 多点触摸,设置缩放模式
			if (event.getPointerCount() == 2) {
				mode = Config.ZOOM;
				oldDistance = spacing(event);
				midPoint(pointF, event);
				startScaleImage();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (lock) { // 如果锁定，则不执行移动操作
				break;
			}
			if (mode == Config.DRAG) {
				current_x = event.getRawX();
				current_y = event.getRawY();
				// 处理拖动
				if (Math.abs(current_x - start_x) < 8
						&& Math.abs(current_y - start_y) < 8) {
					break;
				}
				lockMove = true;
				posLeft = posLeft + current_x - start_x;
				posRight = posRight + current_x - start_x;
				posTop = posTop + current_y - start_y;
				posBottom = posBottom + current_y - start_y;
				// 记录位置
				move();
				start_x = current_x;
				start_y = current_y;
			} else if (mode == Config.ZOOM) {
				if (lock) { // 如果锁定，则不执行缩放操作
					break;
				}
				if (lockMove) {
					break;
				}
				// 处理缩放
				newDistance = spacing(event);
				if (Math.abs(newDistance - oldDistance) > 5) {
					scale = newDistance / oldDistance;
					oldDistance = newDistance;
					// 记录
					scaleImage(scale, true);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (lock) { // 如果锁定，则直接弹出操作工具栏
				imageOperate.showPopupWindow(viewGroup, this);
			} else if (Math.abs(event.getRawX() - down_x) < 8
					&& Math.abs(event.getRawY() - down_y) < 8) {
				if (mode == Config.DRAG) {
					imageOperate.showPopupWindow(viewGroup, this);
				}
			} else {
				// 记录
				if (mode == Config.DRAG) {
					endMove();
				}
			}
			lockMove = false;
			mode = Config.VAIN;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			if (lock) {
				break;
			}
			if (lockMove) {
				break;
			}
			if (mode == Config.ZOOM) {
				endScaleImage();
			}
			mode = Config.VAIN;

			break;
		}
		return true;
	}

	// 取旋转角度
	private float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}

	/** 计算触碰两点间距离 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float)Math.sqrt(x * x + y * y);
	}

	public void setScale(float scale) {
		float dixX = (posRight - posLeft) * (1 - scale) / 2f; // 获取缩放水平距离
		float dixY = (posBottom - posTop) * (1 - scale) / 2f; // 获取缩放垂直距离

		posLeft += dixX;
		posTop += dixY;
		posRight -= dixX;
		posBottom -= dixY;
		this.cusLayout();
	}

	/** 以图片中心点进行布局 */
	public void setLayoutByMid(float center_x, float center_y) {
		float w = posRight - posLeft;
		float h = posBottom - posTop;
		posLeft = center_x - w / 2f;
		posTop = center_y - h / 2f;
		posRight = posLeft + w;
		posBottom = posTop + h;
		this.cusLayout();
	}

	/** ---------移动操作(点坐标为中心点坐标)------------- */
	public void startMove() {
		memory = new HistoryMemory(3);
		memory.setStartTime(DateUtil.getTimeMillisLong());
		move = new float[] { getMidWidth(), getMidHeight() };
		memory.getMoveList().add(move);
		memory.getTimeList().add(DateUtil.getTimeMillisLong());
	}

	public void move() {
		this.cusLayout();
		move = new float[] { getMidWidth(), getMidHeight() };
		if (memory != null) {
			memory.getMoveList().add(move);
			memory.getTimeList().add(DateUtil.getTimeMillisLong());
		}
	}

	public void endMove() {
		memory.setEndTime(DateUtil.getTimeMillisLong());
		memory.setOperaterType(Config.OPER_IMG_MOVE);
		memory.setMimg(this);
		viewGroup.historyOperater.setMove(memory);
	}

	/** 缩放操作 */
	public void startScaleImage() {
		memory = new HistoryMemory(3);
		memory.setStartTime(DateUtil.getTimeMillisLong());
		memory.getScaleList().add(1f);
		memory.getTimeList().add(DateUtil.getTimeMillisLong());
	}

	public void scaleImage(float scale, boolean save) {
		setScale(scale);
		if (save) {
			memory.getScaleList().add(scale);
			memory.getTimeList().add(DateUtil.getTimeMillisLong());
		}
	}

	public void endScaleImage() {
		memory.setEndTime(DateUtil.getTimeMillisLong());
		memory.setMimg(this);
		memory.setOperaterType(Config.OPER_IMG_SCALE);
		viewGroup.historyOperater.scaleImage(memory);
	}

	// 获取图片中心点位置
	private float getMidWidth() {
		return (posLeft + posRight) / 2f;
	}

	private float getMidHeight() {
		return (posTop + posBottom) / 2f;
	}

	PointF pointF = new PointF();

	// 中间点
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/** 顺时针旋转90度 */
	// public void rotate90() {
	// memory = new HistoryMemory(3);
	// memory.setStartTime(DateUtil.getTimeMillisLong());
	// memory.getRotateList().add((float) -1.570796);// "angle":-1.570796
	// memory.getTimeList().add(DateUtil.getTimeMillisLong());
	// memory.setEndTime(DateUtil.getTimeMillisLong());
	// memory.setMimg(this);
	// memory.setOperaterType(Constant.RotateImage);
	// viewGroup.historyOperater.rotateImage(memory);
	// rotation += 90;
	// rotation %= 360;
	// this.setRotation(rotation);
	// }
	public void rotate90(boolean save) {
		if (save) {
			memory = new HistoryMemory(3);
			memory.setStartTime(DateUtil.getTimeMillisLong());
			memory.getRotateList().add((float) 1.570796);// 顺时针旋转90度
			memory.getTimeList().add(DateUtil.getTimeMillisLong());
			memory.setEndTime(DateUtil.getTimeMillisLong());
			memory.setMimg(this);
			memory.setOperaterType(Config.OPER_IMG_ROTATE);
			viewGroup.historyOperater.rotateImage(memory);
		}
		rotation += 90;
		rotation %= 360;
		this.setRotation(rotation);
	}

	/**
	 * 逆时针旋转90度
	 * 
	 * @param
	 */
	public void rotate_90(boolean save) {
		// memory = new HistoryMemory(3);
		// memory.setStartTime(DateUtil.getTimeMillisLong());
		// memory.getRotateList().add((float) -1.570796);// 逆时针旋转90度
		// memory.getTimeList().add(DateUtil.getTimeMillisLong());
		// memory.setEndTime(DateUtil.getTimeMillisLong());
		// memory.setMimg(this);
		// memory.setOperaterType(Constant.RotateImage);
		// viewGroup.historyOperater.rotateImage(memory, save);
		rotation -= 90;
		rotation %= 360;
		this.setRotation(rotation);
	}

	public void rotateImage(float angle) {
		float rotation = (angle / (float) (2 * Math.PI)) * 360;
		float total = rotation;
		this.setRotation(total);
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

	public String getImgName() {
		return imgName;
	}

	public void cusLayout() {
		this.layout((int) posLeft, (int) posTop, (int) posRight,
				(int) posBottom);
	}

	public void setRotate(float rotation) {
		this.setRotation(rotation);
	}
	
	public float getStart_x() {
		return start_x;
	}

	public void setStart_x(float start_x) {
		this.start_x = start_x;
	}

	public float getStart_y() {
		return start_y;
	}

	public void setStart_y(float start_y) {
		this.start_y = start_y;
	}

	public float getCurrent_x() {
		return current_x;
	}

	public void setCurrent_x(float current_x) {
		this.current_x = current_x;
	}

	public float getCurrent_y() {
		return current_y;
	}

	public void setCurrent_y(float current_y) {
		this.current_y = current_y;
	}

	public void moveImg() {
		// 处理拖动
		if (Math.abs(current_x - start_x) > 8
				&& Math.abs(current_y - start_y) > 8) {
			lockMove = true;
			posLeft = posLeft + current_x - start_x;
			posRight = posRight + current_x - start_x;
			posTop = posTop + current_y - start_y;
			posBottom = posBottom + current_y - start_y;
			// 记录位置
			move();
			start_x = current_x;
			start_y = current_y;
		}
	}

}