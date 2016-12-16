package com.lib.play_rec.oldplay;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.Constant;
import com.lib.play_rec.utils.DateUtil;

public class PlayView extends View {

	private VideoPlayerBaseAT player;
	private Path path;
	private Paint bitmapPaint; // 画布的画笔
	private Paint paint; // 真实画笔
	private Paint arrowpaint; // 箭头的画笔
	private Paint ovalPaint;// 椭圆的画笔
	private Paint rectPaint; // 矩形的画笔
	private Paint linePaint; // 直线的画笔
	private Paint cursorPaint; // 光标的画笔
	private Bitmap cursorIcon; // 光标图标

	private Bitmap cacheBitmap;
	private Canvas cacheCanvas;

	private float cur_x, cur_y; // 临时点坐标
	private static final float TOUCH_TOLERANCE = 1;

	private int viewWidth = 0, viewHeight = 0; // 绘画区的宽和高

	private List<HistoryMemoryPlayer> hisList; // 保留的操作集合

	/** 记录Path路径的对象 */
	private HistoryMemoryPlayer penMemory;
	private HistoryMemoryPlayer arrowMemory;
	private HistoryMemoryPlayer ovalMemory;
	private HistoryMemoryPlayer rectMemory;
	private HistoryMemoryPlayer lineMemory;
	private HistoryMemoryPlayer cursorMemory;

	private float[] point;
	private float[] data;
	private float xstart, ystart;
	private float xstart1, ystart1;

	public PlayView(Context context) {
		super(context);
		initCursorIcon();
	}

	public PlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCursorIcon();
	}

	public void setPlayer(VideoPlayerBaseAT player) {
		this.player = player;
		initCursorIcon();
	}

	/**
	 * 初始化光标的图片
	 */
	private void initCursorIcon() {
		if (cursorIcon == null) {
			cursorIcon = BitmapFactory.decodeResource(getResources(),
					R.drawable.cursor);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		createCacheBitmap();// 创建画笔对象
		// 绘制上一次的，否则不连贯
		canvas.drawBitmap(cacheBitmap, 0, 0, bitmapPaint);
		try {
			if (path != null && (player.operate == Config.OPER_PEN)) {
				canvas.drawPath(path, paint); // 实时显示
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * if (path != null) { canvas.drawPath(path, paint); // 实时显示 }
		 */
	}

	/** 创建画板对象 */
	private void createCacheBitmap() {
		if (cacheBitmap == null) {
			viewWidth = this.getWidth();
			viewHeight = this.getHeight();
			cacheBitmap = Bitmap.createBitmap(viewWidth, viewHeight,
					android.graphics.Bitmap.Config.ARGB_8888);
			cacheBitmap.eraseColor(Color.TRANSPARENT);
			cacheCanvas = new Canvas(cacheBitmap);
			bitmapPaint = new Paint(Paint.DITHER_FLAG);
		}
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// return false;
	// }

	/** 初始化画笔对象 */
	private void initPaint(int color, int stroke) {
		paint = new Paint();
		paint.setAntiAlias(true); // 抗锯齿
		paint.setDither(true); // 消除拉动，使画面圓滑
		paint.setColor(player.operate == Config.OPER_RUBBER ? Color.TRANSPARENT
				: color); // 设置颜色
		paint.setXfermode(player.operate == Config.OPER_RUBBER ? new PorterDuffXfermode(
				Mode.CLEAR) : null);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND); // 结合方式，平滑
		paint.setStrokeCap(Paint.Cap.ROUND); // 形状
		paint.setStrokeWidth(stroke); // 设置线宽
	}

	/**
	 * @param x
	 * @param y
	 * @param color
	 * @param stroke
	 * @param refresh
	 *            是否需要立马刷新界面
	 */
	public synchronized void touch_start(float x, float y, int color,
			int stroke, boolean refresh, int mid) {
		path = new Path();
		initPaint(color, stroke);
		path.moveTo(x, y);
		cur_x = x;
		cur_y = y;
		if (refresh) {
			invalidate(); // 刷新界面
		}
		penMemory = new HistoryMemoryPlayer(1);
		penMemory.setMid(mid);
		penMemory.setPath(path);
		penMemory.setPaint(paint);

		// 保存一些信息记录
		penMemory.setStartTime(DateUtil.getTimeMillisLong());
		data = new float[] { x, y, 0, 0 };
		penMemory.getTimeList().add(penMemory.getStartTime());
		penMemory.getDataList().add(data);
	}

	/**
	 * @param x
	 * @param y
	 * @param refresh
	 *            是否需要立马刷新界面 (true 播放状态)
	 */
	public synchronized void touch_move(float x, float y, boolean refresh) {
		if (path == null) {
			return;
		}
		float dx = Math.abs(cur_x - x);
		float dy = Math.abs(cur_y - y);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			// 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也是可以的)
			if (player.operate == Config.OPER_RUBBER) {
				// 如果是擦除操作，必须实时绘制path
				path.lineTo(x, y);
				if (refresh) {
					cacheCanvas.drawPath(path, paint);
				}
			} else {
				path.quadTo(cur_x, cur_y, (x + cur_x) / 2, (y + cur_y) / 2);
			}
			cur_x = x;
			cur_y = y;
			if (refresh) {
				invalidate(); // 刷新界面
			}
		}
		data = new float[] { x, y, 0, 0 };
		penMemory.getTimeList().add(System.currentTimeMillis());
		penMemory.getDataList().add(data);
	}

	/**
	 * @param refresh
	 *            是否需要立马刷新界面
	 */
	public synchronized void touch_up(boolean refresh) {
		if (path == null) {
			return;
		}
		path.lineTo(cur_x, cur_y);
		if (refresh) {
			cacheCanvas.drawPath(path, paint);
		}
		path = null; // 重新置空
		paint = null;
		if (refresh) {
			invalidate(); // 刷新界面
		}
		penMemory.setEndTime(System.currentTimeMillis());
		savePath();
	}

	/** 绘制直线 */
	public synchronized void line_start(float x, float y, int color,
			int stroke, boolean refresh, int mid) {
		if (refresh) {
			refresh4New();
		}
		xstart = x;
		ystart = y;
		linePaint = new Paint();
		linePaint.setDither(true);
		linePaint.setAntiAlias(true);
		linePaint.setColor(color);
		linePaint.setStrokeWidth(stroke);
		linePaint.setStyle(Paint.Style.STROKE);

		lineMemory = new HistoryMemoryPlayer(7);
		lineMemory.setMid(mid);
		lineMemory.setLinePaint(linePaint);
		lineMemory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { x, y };
		lineMemory.getTimeList().add(lineMemory.getStartTime());
		lineMemory.getLineDataList().add(point);
	}

	public synchronized void line_move(float x, float y, boolean refresh) {
		createCacheBitmap();// 创建画笔对象
		if (refresh) {
			refresh4New();
		}
		cur_x = x;
		cur_y = y;
		drawLine(xstart, ystart, cur_x, cur_y, linePaint);
		invalidate();

		point = new float[] { cur_x, cur_y };
		lineMemory.getTimeList().add(System.currentTimeMillis());
		lineMemory.getLineDataList().add(point);
	}

	public synchronized void line_up(float x, float y, boolean refresh) {
		lineMemory.setEndTime(DateUtil.getTimeMillisLong());
		if (linePaint != null) {
			saveLine();
		}
		linePaint = null;
	}

	/** 暂停时调用此方法 绘制直线 */
	public void drawLine(float sx, float sy, float ex, float ey, int color,
			int stroke, int mid) {
		createCacheBitmap();
		linePaint = new Paint();
		linePaint.setDither(true);
		linePaint.setAntiAlias(true);
		linePaint.setColor(color);
		linePaint.setStrokeWidth(stroke);
		linePaint.setStyle(Paint.Style.STROKE);

		drawLine(sx, sy, ex, ey, linePaint);
		// invalidate();
		lineMemory = new HistoryMemoryPlayer(7);
		lineMemory.setMid(mid);
		lineMemory.setLinePaint(linePaint);
		lineMemory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { sx, sy };
		lineMemory.getTimeList().add(lineMemory.getStartTime());
		lineMemory.getLineDataList().add(point);
		point = new float[] { ex, ey };
		lineMemory.getTimeList().add(System.currentTimeMillis());
		lineMemory.getLineDataList().add(point);
		lineMemory.setEndTime(DateUtil.getTimeMillisLong());
		saveLine();
		linePaint = null;
	}

	/** 绘制直线 -回退 */
	public void drawLine(float sx, float sy, float ex, float ey, Paint paint) {
		cacheCanvas.drawLine(sx, sy, ex, ey, paint);
	}

	/** 绘制光标 */
	public synchronized void cursor_start(float x, float y, boolean refresh,
			int mid) {
		if (refresh) {
			refresh4New();
		}
		xstart = x;
		ystart = y;
		if (cursorPaint != null) {
			cursorPaint = null;
		}
		cursorPaint = new Paint();
		cursorPaint.setDither(true);
		cursorPaint.setAntiAlias(true);
		cursorPaint.setColor(Color.BLACK);
		cursorPaint.setStyle(Paint.Style.STROKE);

		cursorMemory = new HistoryMemoryPlayer(8);
		cursorMemory.setCursorPaint(cursorPaint);
		cursorMemory.setMid(mid);
		cursorMemory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { x, y };
		cursorMemory.getTimeList().add(cursorMemory.getStartTime());
		cursorMemory.getCursorDataList().add(point);
	}

	public synchronized void cursor_move(float x, float y, boolean refresh) {
		createCacheBitmap();// 创建画笔对象
		if (refresh) {
			refresh4New();
		}
		cur_x = x;
		cur_y = y;
		drawCursor(xstart, ystart, cur_x, cur_y, cursorPaint);
		invalidate();

		point = new float[] { cur_x, cur_y };
		cursorMemory.getTimeList().add(System.currentTimeMillis());
		cursorMemory.getCursorDataList().add(point);
	}

	public synchronized void cursor_up(float x, float y, boolean refresh) {
		cur_x = x;
		cur_y = y;
		if (refresh) {
			refresh4New();
		}
		// refreshCanvas(); // 刷新界面
		cursorMemory.setEndTime(DateUtil.getTimeMillisLong());
	}

	/** 绘制直线 -回退 */
	public void drawCursor(float sx, float sy, float ex, float ey, Paint paint) {
		cacheCanvas.drawBitmap(cursorIcon, ex, ey, paint);
	}

	/** 绘制箭头 */
	public synchronized void arrow_start(float x, float y, int color,
			int stroke, boolean refresh, int mid) {
		if (refresh) {
			refresh4New();
		}
		xstart = x;
		ystart = y;
		arrowpaint = new Paint();
		arrowpaint.setDither(true);
		arrowpaint.setAntiAlias(true);
		arrowpaint.setColor(color);
		arrowpaint.setStrokeWidth(stroke);
		arrowpaint.setStyle(Paint.Style.STROKE);

		arrowMemory = new HistoryMemoryPlayer(4);
		arrowMemory.setMid(mid);
		arrowMemory.setArrowpaint(arrowpaint);
		arrowMemory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { x, y };
		arrowMemory.getTimeList().add(arrowMemory.getStartTime());
		arrowMemory.getArrowDataList().add(point);
	}

	public synchronized void arrow_move(float x, float y, boolean refresh) {
		createCacheBitmap();// 创建画笔对象
		if (refresh) {
			refresh4New();
		}
		cur_x = x;
		cur_y = y;
		drawAL(xstart, ystart, cur_x, cur_y, arrowpaint);
		invalidate();

		point = new float[] { cur_x, cur_y };
		arrowMemory.getTimeList().add(System.currentTimeMillis());
		arrowMemory.getArrowDataList().add(point);
	}

	public synchronized void arrow_up(float x, float y, boolean refresh) {
		arrowMemory.setEndTime(DateUtil.getTimeMillisLong());
		if (arrowpaint != null) {
			saveArrow();
		}
		arrowpaint = null;
	}

	/** 当暂停的时候调用 箭头 */
	public void drawArrow(float sx, float sy, float ex, float ey, int color,
			int stroke, int mid) {
		createCacheBitmap();
		arrowpaint = new Paint();
		arrowpaint.setDither(true);
		arrowpaint.setAntiAlias(true);
		arrowpaint.setColor(color);
		arrowpaint.setStrokeWidth(stroke);
		arrowpaint.setStyle(Paint.Style.STROKE);

		drawAL(sx, sy, ex, ey, arrowpaint);
		// invalidate();
		arrowMemory = new HistoryMemoryPlayer(4);
		arrowMemory.setMid(mid);
		arrowMemory.setArrowpaint(arrowpaint);
		arrowMemory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { sx, sy };
		arrowMemory.getTimeList().add(arrowMemory.getStartTime());
		arrowMemory.getArrowDataList().add(point);
		point = new float[] { ex, ey };
		arrowMemory.getTimeList().add(System.currentTimeMillis());
		arrowMemory.getArrowDataList().add(point);
		arrowMemory.setEndTime(DateUtil.getTimeMillisLong());
		saveArrow();
		arrowpaint = null;
	}

	/** 绘制箭头 */
	public void drawAL(float sx, float sy, float ex, float ey, Paint paint) {
		cacheCanvas.drawLine(sx, sy, ex, ey, paint);
		drawTriangle(sx, sy, ex, ey, cacheCanvas, paint);
	}

	/** 绘制箭头的三角形 */
	public synchronized void drawTriangle(float sx, float sy, float ex,
			float ey, Canvas canvas, Paint paint) {
		double H = 8; // 箭头高度
		double L = 3.5; // 底边的一半
		int x3 = 0;
		int y3 = 0;
		int x4 = 0;
		int y4 = 0;
		Float SX = new Float(sx);
		int sx1 = SX.intValue();
		Float SY = new Float(sy);
		int sy1 = SY.intValue();
		Float EX = new Float(ex);
		int ex1 = EX.intValue();
		Float EY = new Float(ey);
		int ey1 = EY.intValue();
		double awrad = Math.atan(L / H); // 箭头角度
		double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
		double[] arrXY_1 = rotateVec(ex1 - sx1, ey1 - sy1, awrad, true,
				arraow_len);
		double[] arrXY_2 = rotateVec(ex1 - sx1, ey1 - sy1, -awrad, true,
				arraow_len);
		double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
		double y_3 = ey - arrXY_1[1];
		double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
		double y_4 = ey - arrXY_2[1];
		Double X3 = new Double(x_3);
		x3 = X3.intValue();
		Double Y3 = new Double(y_3);
		y3 = Y3.intValue();
		Double X4 = new Double(x_4);
		x4 = X4.intValue();
		Double Y4 = new Double(y_4);
		y4 = Y4.intValue();
		Path triangle = new Path();
		triangle.moveTo(ex, ey);
		triangle.lineTo(x3, y3);
		triangle.lineTo(x4, y4);
		triangle.close();
		canvas.drawPath(triangle, paint);
	}

	/** 计算箭头的角度 */
	public double[] rotateVec(int px, int py, double ang, boolean isChLen,
			double newLen) {
		double mathstr[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		if (isChLen) {
			double d = Math.sqrt(vx * vx + vy * vy);
			vx = vx / d * newLen;
			vy = vy / d * newLen;
			mathstr[0] = vx;
			mathstr[1] = vy;
		}
		return mathstr;
	}

	/** 绘制椭圆 */
	public void drawOvar(float x1, float y1, float x2, float y2, int color,
			int stroke, int mid, boolean isAndroid) {
		ovalPaint = new Paint();
		ovalPaint.setDither(true);
		ovalPaint.setAntiAlias(true);
		ovalPaint.setColor(color);
		ovalPaint.setStrokeWidth(stroke);
		ovalPaint.setStyle(Paint.Style.STROKE);
		// 操作时，保存一些信息记录
		ovalMemory = new HistoryMemoryPlayer(5);
		ovalMemory.setMid(mid);
		ovalMemory.setOvalPaint(ovalPaint);
		ovalMemory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { x1, y1 };
		ovalMemory.getOvalDataList().add(point);
		point = new float[] { x2, y2 };
		ovalMemory.getTimeList().add(ovalMemory.getStartTime());
		ovalMemory.getOvalDataList().add(point);
		ovalMemory.setEndTime(DateUtil.getTimeMillisLong());
		drawOval(x1, y1, x2, y2, ovalPaint);
		saveOval();
		// refresh4New();// 刷新
	}

	/** 绘制椭圆 */
	public synchronized void oval_start(float x, float y, int color,
			int stroke, boolean refresh, int mid) {
		if (refresh) {
			refresh4New();
		}
		xstart1 = x;
		ystart1 = y;
		ovalPaint = new Paint();
		ovalPaint.setDither(true);
		ovalPaint.setAntiAlias(true);
		ovalPaint.setColor(color);
		ovalPaint.setStrokeWidth(stroke);
		ovalPaint.setStyle(Paint.Style.STROKE);
		// 操作时，保存一些信息记录
		ovalMemory = new HistoryMemoryPlayer(5);
		ovalMemory.setMid(mid);
		ovalMemory.setOvalPaint(ovalPaint);
		ovalMemory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { xstart1, ystart1 };
		ovalMemory.getTimeList().add(ovalMemory.getStartTime());
		ovalMemory.getOvalDataList().add(point);
	}

	public synchronized void oval_move(float x, float y, boolean refresh) {
		createCacheBitmap();// 创建画笔对象
		if (refresh) {
			refresh4New();
		}
		cur_x = x;
		cur_y = y;
		drawOval(xstart1, ystart1, cur_x, cur_y, ovalPaint);
		invalidate();
		point = new float[] { cur_x, cur_y };
		ovalMemory.getTimeList().add(DateUtil.getTimeMillisLong());
		ovalMemory.getOvalDataList().add(point);
	}

	public synchronized void oval_up(float x, float y, boolean refresh) {
		ovalMemory.setEndTime(DateUtil.getTimeMillisLong());
		if (ovalPaint != null) {
			saveOval();
		}
		ovalPaint = null;
	}

	/** 暂停时调用此方法 绘制椭圆 */
	public void drawOval(float sx, float sy, float ex, float ey, int color,
			int stroke, int mid) {
		createCacheBitmap();
		ovalPaint = new Paint();
		ovalPaint.setDither(true);
		ovalPaint.setAntiAlias(true);
		ovalPaint.setColor(color);
		ovalPaint.setStrokeWidth(stroke);
		ovalPaint.setStyle(Paint.Style.STROKE);

		drawOval(sx, sy, ex, ey, ovalPaint);
		// invalidate();
		// 操作时，保存一些信息记录
		ovalMemory = new HistoryMemoryPlayer(5);
		ovalMemory.setMid(mid);
		ovalMemory.setOvalPaint(ovalPaint);
		ovalMemory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { sx, sy };
		ovalMemory.getTimeList().add(ovalMemory.getStartTime());
		ovalMemory.getOvalDataList().add(point);
		point = new float[] { ex, ey };
		ovalMemory.getTimeList().add(DateUtil.getTimeMillisLong());
		ovalMemory.getOvalDataList().add(point);
		ovalMemory.setEndTime(DateUtil.getTimeMillisLong());
		saveOval();
		ovalPaint = null;
	}

	/** 绘制椭圆 */
	public synchronized void drawOval(float sx, float sy, float ex, float ey,
			Paint paint) {
		sx = sx * VideoPlayerBaseAT.scale;
		sy = sy * VideoPlayerBaseAT.scale;
		ex = ex * VideoPlayerBaseAT.scale;
		ey = ey * VideoPlayerBaseAT.scale;
		RectF re = new RectF(sx, sy, ex, ey);
		cacheCanvas.drawOval(re, paint);
	}

	/** 绘制矩形 */
	public synchronized void rect_start(float x, float y, int color,
			int stroke, boolean refresh, int mid) {
		if (refresh) {
			refresh4New();
		}
		xstart1 = x;
		ystart1 = y;
		rectPaint = new Paint();
		rectPaint.setDither(true);
		rectPaint.setAntiAlias(true);
		rectPaint.setColor(color);
		rectPaint.setStrokeWidth(stroke);
		rectPaint.setStyle(Paint.Style.STROKE);

		// 操作时，保存一些信息记录
		rectMemory = new HistoryMemoryPlayer(6);
		rectMemory.setMid(mid);
		rectMemory.setRectPaint(rectPaint);
		rectMemory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { xstart1, ystart1 };
		rectMemory.getTimeList().add(rectMemory.getStartTime());
		rectMemory.getRectDataList().add(point);
	}

	public synchronized void rect_move(float x, float y, boolean refresh) {
		createCacheBitmap();// 创建画笔对象
		if (refresh) {
			refresh4New();
		}
		cur_x = x;
		cur_y = y;
		drawRect(xstart1, ystart1, cur_x, cur_y, rectPaint);
		invalidate();
		point = new float[] { cur_x, cur_y };
		rectMemory.getTimeList().add(DateUtil.getTimeMillisLong());
		rectMemory.getRectDataList().add(point);
	}

	public synchronized void rect_up(float x, float y, boolean refresh) {
		rectMemory.setEndTime(DateUtil.getTimeMillisLong());
		if (rectPaint != null) {
			saveRect();
		}
		rectPaint = null;
	}

	/** 暂停时调用此方法 绘制矩形 */
	public void drawRect(float sx, float sy, float ex, float ey, int color,
			int stroke, int mid) {
		createCacheBitmap();
		rectPaint = new Paint();
		rectPaint.setDither(true);
		rectPaint.setAntiAlias(true);
		rectPaint.setColor(color);
		rectPaint.setStrokeWidth(stroke);
		rectPaint.setStyle(Paint.Style.STROKE);

		drawRect(sx, sy, ex, ey, rectPaint);
		// invalidate();
		// 操作时，保存一些信息记录
		rectMemory = new HistoryMemoryPlayer(6);
		rectMemory.setMid(mid);
		rectMemory.setRectPaint(rectPaint);
		rectMemory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { sx, sy };
		rectMemory.getTimeList().add(rectMemory.getStartTime());
		rectMemory.getRectDataList().add(point);
		point = new float[] { ex, ey };
		rectMemory.getTimeList().add(DateUtil.getTimeMillisLong());
		rectMemory.getRectDataList().add(point);
		rectMemory.setEndTime(DateUtil.getTimeMillisLong());
		saveRect();
		rectPaint = null;
	}

	/** 绘制椭圆 */
	public synchronized void drawRect(float sx, float sy, float ex, float ey,
			Paint paint) {
		sx = sx * VideoPlayerBaseAT.scale;
		sy = sy * VideoPlayerBaseAT.scale;
		ex = ex * VideoPlayerBaseAT.scale;
		ey = ey * VideoPlayerBaseAT.scale;
		RectF re = new RectF(sx, sy, ex, ey);
		cacheCanvas.drawRect(re, paint);
	}

	/** 绘制矩形 */
	public synchronized void drawRectangle(float sx, float sy, float ex,
			float ey) {
		invalidate(); // 刷新界面
	}

	/** 将一条完整的路径保存下来(相当于入栈操作) */
	private void savePath() {
		if (player.operate == Config.OPER_PEN) {
			penMemory.setOperaterType(Config.OPER_PEN);
			player.groupBean.create(penMemory);
		} else {
			penMemory.setOperaterType(Config.OPER_RUBBER);
			player.groupBean.create(penMemory);
		}
	}

	/** 保存箭头数据 */
	private void saveArrow() {
		arrowMemory.setOperaterType(Config.OPER_ARROW);
		player.groupBean.create(arrowMemory);
	}

	/** 保存椭圆数据 */
	public void saveOval() {
		ovalMemory.setOperaterType(Config.OPER_ELLIPSE);
		player.groupBean.create(ovalMemory);
	}

	/**
	 * 保存矩形的数据
	 */
	public void saveRect() {
		rectMemory.setOperaterType(Config.OPER_RECT);
		player.groupBean.create(rectMemory);
	}

	/**
	 * 保存直线的数据
	 */
	public void saveLine() {
		lineMemory.setOperaterType(Config.OPER_LINE);
		player.groupBean.create(lineMemory);
	}

	/**
	 * 保存光标的数据
	 */
	public void saveCursor() {
		cursorMemory.setOperaterType(Config.OPER_CURSOR);
		player.groupBean.create(cursorMemory);
	}

	/** 清空画布 */
	public synchronized void clearCanvas() {
		createCacheBitmap();
		cacheBitmap = Bitmap.createBitmap(viewWidth, viewHeight,
				android.graphics.Bitmap.Config.ARGB_4444);
		cacheCanvas.setBitmap(cacheBitmap); // 重新设置画布，相当于清空画布
	}

	/** 绘制一条线 */
	public synchronized void drawPath(HistoryMemoryPlayer memory) {
		cacheCanvas.drawPath(memory.getPath(), memory.getPaint());
	}

	/** 绘制一个箭头 */
	public synchronized void drawAL(HistoryMemoryPlayer memory) {
		float[] point1 = memory.getArrowDataList().get(0);
		float[] point2 = memory.getArrowDataList().get(
				memory.getArrowDataList().size() - 1);
		drawAL(point1[0], point1[1], point2[0], point2[1],
				memory.getArrowpaint());
	}

	/** 绘制直线 */
	public synchronized void drawLine(HistoryMemoryPlayer memory) {
		float[] point1 = memory.getLineDataList().get(0);
		float[] point2 = memory.getLineDataList().get(
				memory.getLineDataList().size() - 1);
		drawLine(point1[0], point1[1], point2[0], point2[1],
				memory.getLinePaint());
	}

	/** 绘制光标 */
	public synchronized void drawCursor(HistoryMemoryPlayer memory) {
		float[] point1 = memory.getCursorDataList().get(0);
		float[] point2 = memory.getCursorDataList().get(
				memory.getCursorDataList().size() - 1);
		drawCursor(point1[0], point1[1], point2[0], point2[1],
				memory.getCursorPaint());
	}

	/** 绘制一个椭圆 */
	public synchronized void drawOval(HistoryMemoryPlayer memory) {
		float[] point1 = memory.getOvalDataList().get(0);
		float[] point2 = memory.getOvalDataList().get(
				memory.getOvalDataList().size() - 1);
		drawOval(point1[0], point1[1], point2[0], point2[1],
				memory.getOvalPaint());
	}

	/** 绘制一个矩形 */
	public synchronized void drawRect(HistoryMemoryPlayer memory) {
		float[] point1 = memory.getRectDataList().get(0);
		float[] point2 = memory.getRectDataList().get(
				memory.getRectDataList().size() - 1);
		drawRect(point1[0], point1[1], point2[0], point2[1],
				memory.getRectPaint());
	}

	/** 刷新画布 */
	public synchronized void refreshCanvas() {
		invalidate(); // 刷新
	}

	/** 绘制箭头的过程当中刷新画布 */
	public synchronized void refresh4New() {
		hisList = player.groupBean.getHistoryList();
		// 清空画布
		clearCanvas();
		// 绘制历史数据（线条、橡皮擦、箭头）
		for (int i = 0; i < hisList.size(); i++) {
			int type = hisList.get(i).getOperaterType();
			if (type == Config.OPER_PEN || type == Config.OPER_RUBBER) {
				drawPath(hisList.get(i));
			} else if (type == Config.OPER_ARROW) {
				drawAL(hisList.get(i));
			} else if (type == Config.OPER_ELLIPSE) {
				drawOval(hisList.get(i));
			} else if (type == Config.OPER_RECT) {
				drawRect(hisList.get(i));
			} else if (type == Config.OPER_LINE) {
				drawLine(hisList.get(i));
				// } else if (type == Config.OPER_CURSOR) {
				// drawCursor(hisList.get(i));
			}
			invalidate();
			// refreshCanvas();
		}
	}

	/** 删除被矩形包含的路径 */
	public void isContainMemory(ArrayList<String> list) {
		List<HistoryMemoryPlayer> historyList = player.groupBean
				.getHistoryList();
		for (int j = 0; j < historyList.size(); j++) {
			HistoryMemoryPlayer memoryPlayer = historyList.get(j);
			for (int i = 0; i < list.size(); i++) {
				int parseInt = Integer.parseInt(list.get(i));
				if (memoryPlayer.getMid() == parseInt) {
					player.groupBean.addRepeal(memoryPlayer);
					historyList.remove(memoryPlayer);
					j--;
					player.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							refresh4New();
						}
					});
				}
			}
		}
	}

	/** ios设备录的话就要转变下笔画的宽度 */
	private int isIOS(int stroke) {
		if (stroke == 1) {
			return 2;
		} else if (stroke == 2) {
			return 4;
		} else if (stroke == 4) {
			return 6;
		} else {
			return 8;
		}
	}

	/** 销毁Bitmap */
	public void desctroy() {
		if (cacheBitmap != null && !cacheBitmap.isRecycled()) {
			cacheBitmap.recycle();
			cacheBitmap = null;
			System.gc();
		}
	}

}
