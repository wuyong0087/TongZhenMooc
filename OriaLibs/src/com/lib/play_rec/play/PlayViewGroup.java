package com.lib.play_rec.play;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.Constant;
import com.lidroid.xutils.BitmapUtils;

public class PlayViewGroup extends ViewGroup {

	private VideoPlayerBaseAty player;
	private PlayEditText wacomEditText = null;
	private PlayImageView wacomImageView = null;

	public PlayViewGroup(Context context) {
		super(context);
	}

	public PlayViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setPlayer(VideoPlayerBaseAty player) {
		this.player = player;
	}

	/** 设置背景 */
	public void setBackground(int order) {
		// player.backgroundOrder = order;
		// player.groupBean.backgroundOrder = order;
		switch (order) {
		case 1:
			this.setBackgroundColor(Color.rgb(255, 255, 255));
			break;
		case 2:
			this.setBackgroundColor(Color.rgb(13, 13, 13));
			break;
		case 3:
			this.setBackgroundColor(Color.rgb(0, 86, 31));
			break;
		case 4:
			this.setBackgroundResource(R.drawable.bgset_paper_big_bg);
			break;
		}
	}

	/** 新增一个文本编辑框 */
	public void initEditText(int x, int y, int w, int h, String text, int mid,
			boolean refresh) {
		wacomEditText = new PlayEditText(player, x, y, w, h,
				player.editTextColor, player.editTextSize, text, mid);
		if (refresh) {
			this.addView(wacomEditText);
			// 让文本控件在绘图界面的下方
			bringChildToFront(player.wacomView);
			player.wacomView.postInvalidate();
		}
		// 保存------------
		player.groupBean.viewMap.put(wacomEditText.getMid(), wacomEditText);
		player.groupBean.viewOrderList.add(wacomEditText);
		PlayHistoryMemory memory = new PlayHistoryMemory(2);
		memory.setOperaterType(Config.OPER_TEXT_ADD);
		memory.setMet(wacomEditText);
		memory.setMid(mid);
		player.groupBean.addMemory(memory);
	}

	/** 删除文本编辑框 */
	public void delEditText(PlayEditText met, boolean refresh, boolean save) {
		player.groupBean.viewMap.remove(met.getMid());
		player.groupBean.viewOrderList.remove(met);
		if (refresh) {
			this.removeView(met);
		}
		if (save) {
			PlayHistoryMemory memory = new PlayHistoryMemory(2);
			memory.setStartTime(System.currentTimeMillis());
			memory.setEndTime(System.currentTimeMillis());
			memory.setOperaterType(Config.OPER_TEXT_DELETE);
			memory.setMet(met);
			memory.setMid(met.getMid());
			player.groupBean.remove(memory);
		}
	}

	/** 添加文本编辑框 */
	public void addEditText(PlayEditText met, boolean refresh) {
		if (refresh) {
			this.addView(met);
			// met.setText(met.getTextStr());
			// met.setLayout();
			// 让文本控件在绘图界面的下方
			bringChildToFront(player.wacomView);
			player.wacomView.postInvalidate();
		}
		player.groupBean.viewMap.put(met.getMid(), met);
		player.groupBean.viewOrderList.add(met);
	}

	/** 修改文本 */
	public void modifyEditText(PlayEditText met, String text, int size,
			int color, int width, int height, boolean refresh) {
		met.setViewWidthHeight(width, height, refresh);
		met.setTextStr(text, refresh);
		met.setSize(size, refresh);
		met.setColor(color, refresh);

		PlayHistoryMemory memory = new PlayHistoryMemory(2);
		memory.setStartTime(System.currentTimeMillis());
		memory.setOperaterType(Config.OPER_TEXT_EDIT);
		memory.setMet(met);
		memory.setMid(met.getMid());
		memory.setOldText(text);
		memory.setOldSize(size);
		memory.setOldColor(color);
		memory.setOldWidth(width);
		memory.setOldHeight(height);
		memory.setEndTime(System.currentTimeMillis());

		player.groupBean.modifyText(memory);
	}

	/** 移动文本 */
	public void startMoveText(int mid) {
		wacomEditText = (PlayEditText) player.groupBean.viewMap.get(mid);
		wacomEditText.startMoveText();
	}

	public void moveText(int left, int top, boolean refresh) {
		wacomEditText.moveText(left, top, refresh);
	}

	public void endMoveText() {
		wacomEditText.endMoveText();
	}

	/** 新增一张图片 */
	public void initImageView(Bitmap bmp, int mid, int x, int y, boolean refresh) {
		if (bmp == null) {
			return;
		}
		wacomImageView = new PlayImageView(player, x, y, bmp, mid);
		if (refresh) {
			this.addView(wacomImageView);
			// 让图片控件在绘图界面的下方
			bringChildToFront(player.wacomView);
			player.wacomView.postInvalidate();
		}
		// -把图片框加入历史记录 -----
		player.groupBean.viewMap.put(wacomImageView.getMid(), wacomImageView);
		player.groupBean.viewOrderList.add(wacomImageView);
		PlayHistoryMemory memory = new PlayHistoryMemory(3);
		memory.setOperaterType(Config.OPER_IMG_ADD);
		memory.setMimg(wacomImageView);
		memory.setMid(mid);
		player.groupBean.addMemory(memory);
	}

	/** 新增一张图片 */
	public void initImageView2(PlayViewGroup viewGroup, Context context,
			String filePath, int mid, int x, int y, int width, int height,
			boolean refresh) {
		if (filePath == null) {
			return;
		}
		wacomImageView = new PlayImageView(viewGroup, player, x, y, filePath,
				mid, width, height);
		BitmapUtils bitmapUtils = new BitmapUtils(player);
		bitmapUtils.display(wacomImageView, filePath);
		
		if (refresh) {
			this.addView(wacomImageView);
			// 让图片控件在绘图界面的下方
			bringChildToFront(player.wacomView);
			player.wacomView.postInvalidate();
		}
		// -把图片框加入历史记录 -----
		player.groupBean.viewMap.put(wacomImageView.getMid(), wacomImageView);
		player.groupBean.viewOrderList.add(wacomImageView);
		PlayHistoryMemory memory = new PlayHistoryMemory(3);
		memory.setOperaterType(Config.OPER_IMG_ADD);
		memory.setMimg(wacomImageView);
		memory.setMid(mid);
		player.groupBean.addMemory(memory);
	}

	/** 删除一张图片 */
	public void delImageView(PlayImageView imgView, boolean refesh, boolean save) {
		player.groupBean.viewMap.remove(imgView.getMid());
		player.groupBean.viewOrderList.remove(imgView);
		if (refesh) {
			this.removeView(imgView);
		}
		if (save) {
			// 如果需要保存到历史记录
			PlayHistoryMemory memory = new PlayHistoryMemory(3);
			memory.setStartTime(System.currentTimeMillis());
			memory.setEndTime(System.currentTimeMillis());
			memory.setOperaterType(Config.OPER_IMG_DELETE);
			memory.setMimg(imgView);
			memory.setMid(imgView.getMid());
			player.groupBean.remove(memory);
		}
	}

	/** 添加图片 */
	public void addImageView(PlayImageView mimg, boolean refesh) {
		if (refesh) {
			this.addView(mimg);
			// 让文本控件在绘图界面的下方
			bringChildToFront(player.wacomView);
			player.wacomView.postInvalidate();
		}
		player.groupBean.viewMap.put(mimg.getMid(), mimg);
		player.groupBean.viewOrderList.add(mimg);
	}

	/** 移动图片 */
	public void startMoveImage(int mid) {
		wacomImageView = (PlayImageView) player.groupBean.viewMap.get(mid);
		if (wacomImageView != null) {
			wacomImageView.startMove();
		}
	}

	public void moveImage(float center_x, float center_y, boolean refresh) {
		wacomImageView.move(center_x, center_y, refresh);
	}

	public void endMoveImage() {
		wacomImageView.endMove();
	}

	/** 缩放图片 */
	public void startScaleImage(int mid) {
		wacomImageView = (PlayImageView) player.groupBean.viewMap.get(mid);
		wacomImageView.startScaleImage();
	}

	public void scaleImage(double scale, boolean refresh) {
		wacomImageView.scaleImage(scale, refresh);
	}

	public void endScaleImage() {
		wacomImageView.endScaleImage();
	}

	/** 旋转图片 */
	public void startRotateImage(int mid) {
		wacomImageView = (PlayImageView) player.groupBean.viewMap.get(mid);
		wacomImageView.startRotateImage();
	}

	public void rotateImage(float scale, boolean refresh) {
		wacomImageView.rotateImage(scale, refresh);
	}

	public void endRotateImage() {
		wacomImageView.endRotateImage();
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
			if (child instanceof PlayView) {
				child.layout(0, 0, right - left, bottom - top);
			} else if (child instanceof PlayEditText) {
				PlayEditText childEt = (PlayEditText) child;
				// 获取在onMeasure中计算的视图尺寸
				// int measureHeight = childEt.getMeasuredHeight();
				// int measuredWidth = childEt.getMeasuredWidth();
				childEt.layout(childEt.posx, childEt.posy, childEt.posx
						+ childEt.viewWidth, childEt.posy + childEt.viewHeight);
			} else if (child instanceof PlayImageView) {
				PlayImageView childImg = (PlayImageView) child;
				childImg.cusLayout();
			}
		}
	}

}