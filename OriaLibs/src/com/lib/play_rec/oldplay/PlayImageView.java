package com.lib.play_rec.oldplay;

import android.graphics.Bitmap;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.Constant;
import com.lib.play_rec.utils.DateUtil;

public class PlayImageView extends ImageView {

	/** 父控件传过来的值 */
	private VideoPlayerBaseAT player;
	/** 图片操作变量 */
	float oldRotation = 0, newRotation = 0;

	/** 对象属性 */
	private double posLeft = 0, posTop = 0, posRight = 0, posBottom = 0;
	private float rotation = 0; // 实际旋转的角度
	private boolean lock = false; // 是否锁定
	private int mid;
	private double sumScale = 1.0;
	private double left, top, right, bottom;

	private float[] move;
	private HistoryMemoryPlayer memory;

	public PlayImageView(VideoPlayerBaseAT player, int x, int y,
			Bitmap bmp, int mid) {
		super(player);
		this.player = player;
		this.mid = mid;

		LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
		this.setLayoutParams(pa);

		posLeft = x;
		posTop = y;
		posRight = posLeft + bmp.getWidth();
		posBottom = posTop + bmp.getHeight();
		this.setImageBitmap(bmp);
		this.setScaleType(ScaleType.FIT_XY);
	}
	
	public PlayImageView(PlayViewGroup viewGroup,
			VideoPlayerBaseAT player, int x, int y, String filePath, int mid,
			int width, int height) {
		super(player);
		this.player = player;
		this.mid = mid;

		LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
		this.setLayoutParams(pa);

		/*BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inJustDecodeBounds = false;

		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

		Bitmap bitmap = BitmapUtil.getScaleBitmap3(bmp, width, height,
				isHsaScale);*/
		
		posLeft = x;
		posTop = y;
//		posRight = posLeft + bitmap.getWidth();
//		posBottom = posTop + bitmap.getHeight();
		posRight = posLeft + width;	// ADD BY WUYONG
		posBottom = posTop + height;
//		this.setImageBitmap(bitmap);
		this.setScaleType(ScaleType.FIT_XY);
		/*if (bitmap != null) {// 及时回收
			bitmap.recycle();
			bitmap = null;
		}*/
	}

	/** 以图片中心点进行布局 */
	public void setLayoutByMid(float center_x, float center_y, boolean refresh) {
		double w = posRight - posLeft;
		double h = posBottom - posTop;
		posLeft = center_x - w / 2;
		posTop = center_y - h / 2;
		posRight = posLeft + w;
		posBottom = posTop + h;
		if (refresh) {
			this.cusLayout();
		}
	}

	/** ---------移动操作(点坐标为中心点坐标)------------- */
	public void startMove() {
		memory = new HistoryMemoryPlayer(3);
		memory.setStartTime(DateUtil.getTimeMillisLong());
	}

	public void move(float center_x, float center_y, boolean refresh) {
		setLayoutByMid(center_x, center_y, refresh);
		move = new float[] { center_x, center_y };
		memory.getMoveList().add(move);
		memory.getTimeList().add(DateUtil.getTimeMillisLong());
	}

	public void endMove() {
		memory.setEndTime(DateUtil.getTimeMillisLong());
		memory.setOperaterType(Config.OPER_IMG_MOVE);
		memory.setMimg(this);
		player.groupBean.setMove(memory);
	}

	/** 缩放操作 */
	public void startScaleImage() {
		sumScale = 1.0;
		left = posLeft;
		top = posTop;
		right = posRight;
		bottom = posBottom;
		memory = new HistoryMemoryPlayer(3);
		memory.setStartTime(DateUtil.getTimeMillisLong());
	}

	// 正常播放的缩放
	public void scaleImage(double scale, boolean refresh) {
		sumScale *= scale;
		double dixX = (right - left) * (1.0 - sumScale) / 2.0; // 获取缩放水平距离
		double dixY = (bottom - top) * (1.0 - sumScale) / 2.0; // 获取缩放垂直距离
		posLeft = left + dixX;
		posTop = top + dixY;
		posRight = right - dixX;
		posBottom = bottom - dixY;
		if (refresh) {
			this.cusLayout();
		}

		memory.getScaleList().add((float) scale);
		memory.getTimeList().add(DateUtil.getTimeMillisLong());
	}

	public void endScaleImage() {
		sumScale = 1.0;
		memory.setEndTime(DateUtil.getTimeMillisLong());
		memory.setMimg(this);
		memory.setOperaterType(Config.OPER_IMG_SCALE);
		player.groupBean.scaleImage(memory);
	}

	// 回退前进的缩放
	public void scaleImageForUnReDo(double scale, boolean refresh) {
		double dixX = (posRight - posLeft) * (1.0 - scale) / 2.0; // 获取缩放水平距离
		double dixY = (posBottom - posTop) * (1.0 - scale) / 2.0; // 获取缩放垂直距离

		posLeft += dixX;
		posTop += dixY;
		posRight -= dixX;
		posBottom -= dixY;
		if (refresh) {
			this.cusLayout();
		}
	}

	private float total;

//	/** 顺时针旋转90度 */
//	public void rotateImage(final float angle, boolean refresh) {
//		float rotation = (angle / (float) (2 * Math.PI)) * 360;
//		total += rotation;
//		if (refresh) {
//			this.setRotation(total);
//		}
//	}
	
//	public void startRotateImage() {
//		memory = new HistoryMemoryPlayer(3);
//		memory.setStartTime(DateUtil.getTimeMillisLong());
//	}

	/** 顺时针旋转90度 */
	public void startRotateImage() {
		memory = new HistoryMemoryPlayer(3);
		memory.setStartTime(DateUtil.getTimeMillisLong());
	}
	
	public void rotateImage(final float angle, boolean refresh) {
		float rotation = (angle / (float) (2 * Math.PI)) * 360;
		total += rotation;
		this.setRotation(total);
		if (refresh) {
			memory.getRotateList().add((float) angle);
			memory.getTimeList().add(DateUtil.getTimeMillisLong());
		}
	}

	public void endRotateImage() {
		memory.setEndTime(DateUtil.getTimeMillisLong());
		memory.setMimg(this);
		memory.setOperaterType(Config.OPER_IMG_ROTATE);
		player.groupBean.rotateImage(memory);
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

	public void cusLayout() {
		this.layout((int) posLeft, (int) posTop, (int) posRight,
				(int) posBottom);
	}
}
