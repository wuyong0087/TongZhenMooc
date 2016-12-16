package com.lib.play_rec.rec;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.lib.play_rec.emue.RecPenEvent;
import com.lib.play_rec.entity.BrushPathBean;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.FlaotPoint;
import com.lib.play_rec.play.VideoPlayerBaseAty;
import com.lib.play_rec.utils.DateUtil;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class RecordView extends View {
	private VideoPlayerBaseAty context;
	private Path path;
	private Paint bitmapPaint; // 画布的画笔
	private Paint paint; // 真实画笔
	// 画笔的粗细及颜色
	public static int paintStroke = 4, paintColor = Color.BLACK;
	private Bitmap cacheBitmap;
	public Canvas cacheCanvas;
	public List<HistoryMemory> histList; // 保留的操作集合
	private List<HistoryMemory> resetHistoryList;

	private float cur_x, cur_y; // 临时点坐标
	private static final float TOUCH_TOLERANCE = 1;

	private int viewWidth = 0, viewHeight = 0; // 绘画区的宽和高

	private RecordViewGroup group;
	private float data[];
	private float point[];

	/** 记录Path路径的对象 */
	private HistoryMemory memory;

	/** 保存操作的坐标 */
	private float startX, startY, endX, endY;// 箭头的坐标

	private ArrayList<BrushPathBean> beans = new ArrayList<BrushPathBean>();
	private ArrayList<BrushPathBean> beans2;
	private BrushPathBean pathBean;// 保存画笔路径

	public boolean isWriting = false;// 判断是否是最先写的动作
	public boolean recovery = false;//

	/** 保存画笔路径坐标的集合 */
	private ArrayList<Float> floatx;
	private ArrayList<Float> floaty;

	/** 保存矩形括起来的画笔坐标的集合 */
	private ArrayList<Float> floatX = new ArrayList<Float>();
	private ArrayList<Float> floatY = new ArrayList<Float>();

	/** 保存画笔的mid和颜色 */
	public HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
	private OnDrawListener drawListener;
	// 橡皮擦进过的画线的ID集合
	private StringBuilder sbIds;

	private EventBus eventBus;
	private boolean isRegister;

	public OnDrawListener getDrawListener() {
		return drawListener;
	}

	public void setDrawListener(OnDrawListener drawListener) {
		this.drawListener = drawListener;
	}

	public RecordView(VideoPlayerBaseAty context) {
		super(context);
		this.context = context;
		eventBus = EventBus.getDefault();

		invalidate();
	}

	public void setGroup(RecordViewGroup group) {
		this.group = group;
	}

	private void getParentView() {
		ViewParent parent = getParent();
		if (parent instanceof RecordViewGroup) {

		}
	}

	public void onEventAsync(RecPenEvent event) {
		if (event != null) {
			paintStroke = event.getTag();
		}
	}
	public void onEventAsync(Integer color) {
		if (color != null) {
			paintColor = color;
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		createCacheBitmap();

		// 绘制上一次的，否则不连贯
		canvas.drawBitmap(cacheBitmap, 0, 0, bitmapPaint);
		// TODO 获取当前父控件的操作类型
		if (path != null && group.getOperate() == Config.OPER_PEN) {
			canvas.drawPath(path, paint); // 实时显示
		}
	}

	private void createCacheBitmap() {
		if (cacheBitmap == null) {
			viewWidth = this.getWidth();
			viewHeight = this.getHeight();
			cacheBitmap = Bitmap.createBitmap(viewWidth, viewHeight,
					android.graphics.Bitmap.Config.ARGB_8888);
			cacheCanvas = new Canvas(cacheBitmap);
			bitmapPaint = new Paint(Paint.DITHER_FLAG);
		}
		postInvalidate();
	}

	/** 通知activity隐藏删除字体 */
	private void sendmsgToSc() {
		if (beans2 != null) {
			beans2.clear();// 没有删除动作则清空保存被矩形括起来的笔记集合
		}
		refresh4New(map);
		// context.handler.sendEmptyMessage(context.DELETEBRUSH1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 禁止父控件的触摸事件
		getParent().getParent().requestDisallowInterceptTouchEvent(true);
		float x = event.getX();
		float y = event.getY();
		int operType = group.getOperate();
		switch (operType) {
		case Config.OPER_PEN: // 如果是画图和擦除操作
			if (Config.isDigital) { // 如果当前是数码笔操作，则不执行绘制的操作
				break;
			}
		case Config.OPER_RUBBER:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// sendmsgToSc();
				if (operType == Config.OPER_RUBBER) {
					touch_start(x, y, paintColor, Config.RUBBER_STROKE,
							JsonOperater.getInstance().getViewId());
				} else {
					touch_start(x, y, paintColor, paintStroke,
							JsonOperater.getInstance().getViewId());
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (Config.isDigital && operType == Config.OPER_PEN) {
					break;
				}
				touch_move(x, y);
				break;
			case MotionEvent.ACTION_UP:
				if (Config.isDigital && operType == Config.OPER_PEN) {
					break;
				}
				touch_up();
				break;
			}
			break;
		case Config.OPER_TEXT_ADD: // 如果是插入文本操作
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				group.initEditText(x, y, "", JsonOperater.getInstance()
						.getViewId());
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				group.setOperate(Config.OPER_NONE);
				break;
			}
			break;
		case Config.OPER_SELECT:
			return false;
		case Config.OPER_LINE:
		case Config.OPER_ARROW:
		case Config.OPER_ELLIPSE:
		case Config.OPER_RECT:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				pathStart(x, y, paintColor, paintStroke, JsonOperater
						.getInstance().getViewId());
				break;
			case MotionEvent.ACTION_MOVE:
				pathMove(x, y);
				break;
			case MotionEvent.ACTION_UP:
				pathUp(x, y);
				break;
			}
			break;
		case Config.OPER_CLEAN_RECT:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				clean_rectangle_start(x, y);
				break;
			case MotionEvent.ACTION_MOVE:
				clean_rectangle_move(x, y);
				break;
			case MotionEvent.ACTION_UP:
				clean_rectangle_up(x, y);
				break;
			}
			break;
		}

		return true;
	}

	private boolean getPathBean(float x, float y) {// x 357 y 269
		for (int i = 0; i < beans.size(); i++) {
			HistoryMemory pathMemory = beans.get(i).getMemory();
			switch (pathMemory.getOperaterType()) {
			case Config.OPER_LINE:
				int size = pathMemory.getDataList().size();
				float[] point1 = pathMemory.getDataList().get(0);// 68 450
				float[] point2 = pathMemory.getDataList().get(// 491 54
						pathMemory.getDataList().size() - 1);
				if ((point1[0] < x && x < point2[0])
						|| (point1[0] > x && x > point2[0])
						&& (point1[1] < y && y < point2[1])
						|| (point1[1] > y && y > point2[1])) {
					refresh4New(map);
				}
				return true;
			}
		}
		return false;
	}

	/** 刷新画布 */
	public void refreshCanvas() {
		invalidate(); // 刷新
	}

	public Path getPath() {
		return path;
	}

	/** 销毁Bitmap */
	public void destroy(Bitmap bp) {
		if (bp != null && !bp.isRecycled()) {
			bp.recycle();
			bp = null;
		}
	}

	public void destroy() {
		if (cacheBitmap != null && !cacheBitmap.isRecycled()) {
			cacheBitmap.recycle();
			cacheBitmap = null;
		}
		// TODO 清空集合
		group.historyOperater.destroy();
	}

	/** 初始化画笔对象 */
	private void initPaint(int color, int stroke) {
		// TODO 获取容器控件的当前操作类型
		boolean flag = group.getOperate() == Config.OPER_RUBBER ? true : false;
		paint = new Paint();
		paint.setAntiAlias(true); // 抗锯齿
		paint.setDither(true); // 消除拉动，使画面圓滑
		paint.setColor(color); // 设置颜色
		paint.setXfermode(flag ? new PorterDuffXfermode(Mode.CLEAR) : null);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND); // 结合方式，平滑
		paint.setStrokeCap(Paint.Cap.ROUND); // 形状
		paint.setStrokeWidth(stroke); // 设置线宽
	}

	public void touch_start(float x, float y, int color, int stroke, int mid) {
		map.put(mid, color);// 保存mid和画笔颜色
		floatx = new ArrayList<Float>();
		floaty = new ArrayList<Float>();
		path = new Path();
		initPaint(color, stroke);
		path.moveTo(x, y);
		cur_x = x;
		cur_y = y;
		refreshCanvas(); // 刷新界面
		memory = new HistoryMemory(HistoryMemory.DATA_LIST);
		memory.setMid(mid);
		memory.setPath(path);
		memory.setPaint(paint);

		// 录屏操作时，保存一些信息记录
		memory.setStartTime(DateUtil.getTimeMillisLong());
		data = new float[] { x, y, 0, 0 };
		// TODO 获取当前容器控件的操作类型
		if (group.getOperate() == Config.OPER_PEN) {
			memory.getTimeList().add(memory.getStartTime());
			memory.getDataList().add(data);
		}
	}

	public void touch_move(float x, float y) {
		floatx.add(x);
		floaty.add(y);

		if (path == null) {
			return;
		}
		float dx = Math.abs(cur_x - x);
		float dy = Math.abs(cur_y - y);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			// 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也是可以的)
			// TODO 获取当前容器控件的操作类型
			if (group.getOperate() == Config.OPER_RUBBER) {
				// 如果是擦除操作，必须实时绘制path
				path.lineTo(x, y);
				cacheCanvas.drawPath(path, paint);
			} else {
				path.quadTo(cur_x, cur_y, (x + cur_x) / 2, (y + cur_y) / 2);
			}
			cur_x = x;
			cur_y = y;
			refreshCanvas(); // 刷新界面
		}
		data = new float[] { x, y, 0, 0 };
		// TODO 获取当前容器控件的操作类型
		if (group.getOperate() == Config.OPER_PEN) {
			memory.getTimeList().add(DateUtil.getTimeMillisLong());
			memory.getDataList().add(data);
		}
	}

	public void touch_up(int... source) {
		if (path == null) {
			return;
		}
		path.lineTo(cur_x, cur_y);
		// 鼠标弹起保存最后状态
		cacheCanvas.drawPath(path, paint);
		memory.setEndTime(System.currentTimeMillis());
		addPencilPathBean(floatx, floaty, memory);
		floatx.clear();
		floaty.clear();
		savePath(source);
		invalidate(); // 刷新
		path = null; // 重新置空
		paint = null;
	}

	/**
	 * Path的绘制
	 * 
	 * @param x
	 *            开始坐标点的X值
	 * @param y
	 *            开始坐标点的Y值
	 * @param color
	 *            当前画笔颜色
	 * @param stroke
	 *            当前画笔宽度
	 * @param mid
	 *            当前指令的mid
	 */
	private void pathStart(float x, float y, int color, int stroke, int mid) {
		map.put(mid, color);// 保存mid和画笔颜色
		startX = x;
		startY = y;
		initPaint(color, stroke);
		cur_x = x;
		cur_y = y;

		// 录屏操作时，保存一些信息记录
		memory = new HistoryMemory(HistoryMemory.DATA_LIST);
		memory.setMid(mid);
		memory.setPaint(paint);
		memory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { startX, startY };
		memory.getTimeList().add(memory.getStartTime());
		memory.getDataList().add(point);
	}

	private void pathMove(float x, float y) {
		if (startX == x && startY == y) {
			return;
		}
		refresh4New();
		cur_x = x;
		cur_y = y;
		// TODO 获取当前容器控件的操作类型
		if (group.getOperate() == Config.OPER_ARROW) {
			drawAL(startX, startY, cur_x, cur_y, paint);
		} else if (group.getOperate() == Config.OPER_ARROW) {
			drawOval(startX, startY, endX, endY, paint);
		} else if (group.getOperate() == Config.OPER_ARROW) {
			drawRectangle(startX, startY, cur_x, cur_y, paint);
		}
		refreshCanvas();

		point = new float[] { cur_x, cur_y };
		memory.getTimeList().add(System.currentTimeMillis());
		memory.getDataList().add(point);
	}

	private void pathUp(float x, float y) {
		endX = x;
		endY = y;
		if (startX == endX && startY == endY) {
			return;
		}
		refresh4New();
		// TODO 获取当前容器控件的操作类型
		if (group.getOperate() == Config.OPER_ARROW) {
			drawAL(startX, startY, cur_x, cur_y, paint);
		} else if (group.getOperate() == Config.OPER_ELLIPSE) {
			drawOval(startX, startY, endX, endY, paint);
		} else if (group.getOperate() == Config.OPER_RECT) {
			drawRectangle(startX, startY, cur_x, cur_y, paint);
		}
		refreshCanvas(); // 刷新界面
		memory.setEndTime(DateUtil.getTimeMillisLong());
		point = new float[] { endX, endY };
		memory.getTimeList().add(memory.getEndTime());
		memory.getDataList().add(point);
		addPathBean(startX, startY, endX, endY, memory);
		paint = null;
		// TODO 获取当前容器控件的操作类型
		memory.setOperaterType(group.getOperate());
		// TODO 保存集合
		group.historyOperater.create(memory);
	}

	/** 绘制箭头 */
	public void drawAL(float sx, float sy, float ex, float ey, Paint paint) {
		cacheCanvas.drawLine(sx, sy, ex, ey, paint);
		drawTriangle(sx, sy, ex, ey, cacheCanvas, paint);
	}

	/** 绘制箭头的三角形 */
	public void drawTriangle(float sx, float sy, float ex, float ey,
			Canvas canvas, Paint paint) {
		double H = 8; // 箭头高度
		double L = 3.5; // 底边的一半
		// int x3 = 0;
		// int y3 = 0;
		// int x4 = 0;
		// int y4 = 0;
		// Float SX = new Float(sx);
		// int sx1 = SX.intValue();
		// Float SY = new Float(sy);
		// int sy1 = SY.intValue();
		// Float EX = new Float(ex);
		// int ex1 = EX.intValue();
		// Float EY = new Float(ey);
		// int ey1 = EY.intValue();
		// double awrad = Math.atan(L / H); // 箭头角度
		// double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
		// double[] arrXY_1 = rotateVec(ex1 - sx1, ey1 - sy1, awrad, true,
		// arraow_len);
		// double[] arrXY_2 = rotateVec(ex1 - sx1, ey1 - sy1, -awrad, true,
		// arraow_len);
		// double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
		// double y_3 = ey - arrXY_1[1];
		// double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
		// double y_4 = ey - arrXY_2[1];
		// Double X3 = new Double(x_3);
		// x3 = X3.intValue();
		// Double Y3 = new Double(y_3);
		// y3 = Y3.intValue();
		// Double X4 = new Double(x_4);
		// x4 = X4.intValue();
		// Double Y4 = new Double(y_4);
		// y4 = Y4.intValue();
		// 箭头宽度
		double arrow_width = Math.atan(L / H);
		// 箭头长度
		double arrow_length = Math.sqrt(L * L + H * H);
		double[] arrowXYStart = rotateVec((int) (ex - sx), (int) (ey - sy),
				arrow_width, true, arrow_length);
		double[] arrowXYEnd = rotateVec((int) (ex - sx), (int) (ey - sy),
				-arrow_width, true, arrow_length);

		int startX = (int) (ex - arrowXYStart[0]);
		int startY = (int) (ey - arrowXYStart[1]);
		int endX = (int) (ex - arrowXYEnd[0]);
		int endY = (int) (ey - arrowXYEnd[1]);
		Path triangle = new Path();
		triangle.moveTo(ex, ey);
		triangle.lineTo(startX, startY);
		triangle.lineTo(endX, endY);
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

	/** 绘制直线 */
	private void drawLine(float sx, float sy, float ex, float ey, Paint paint) {
		cacheCanvas.drawLine(sx, sy, ex, ey, paint);
	}

	/** 绘制椭圆 */
	public void drawOval(float sx, float sy, float ex, float ey, Paint paint) {
		RectF re = new RectF(sx, sy, ex, ey);
		cacheCanvas.drawOval(re, paint);
	}

	/** 多选删除虚线矩形操作 */
	public void clean_rectangle_start(float x, float y) {
		startX = x;
		startY = y;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(AtyRecord.shapeStroke);
		PathEffect effect = new DashPathEffect(new float[] { 6, 6, 6, 6 }, 1);
		paint.setAntiAlias(true);
		paint.setPathEffect(effect);
		cur_x = x;
		cur_y = y;

		// 录屏操作时，保存一些信息记录
		memory = new HistoryMemory(HistoryMemory.DATA_LIST);
		memory.setStartTime(DateUtil.getTimeMillisLong());
		point = new float[] { startX, startY };
		memory.getTimeList().add(memory.getStartTime());
		memory.getDataList().add(point);
	}

	public void clean_rectangle_move(float x, float y) {
		// refresh4New(map);
		refresh4New();
		cur_x = x;
		cur_y = y;
		drawRectangle(startX, startY, cur_x, cur_y, paint);
		refreshCanvas();
	}

	public void clean_rectangle_up(float x, float y) {
		endX = x;
		endY = y;
		// refresh4New(map);
		refresh4New();
		refreshCanvas(); // 刷新界面
		memory.setEndTime(System.currentTimeMillis());
		point = new float[] { x, y };
		memory.getTimeList().add(memory.getEndTime());
		memory.getDataList().add(point);
		memory.setPaint(paint);
		isContainBrushPaths();
		// saveRectangle();
		paint = null;
	}

	/** 绘制矩形 */
	public void drawRectangle(float sx, float sy, float ex, float ey,
			Paint paint) {
		RectF re = new RectF(sx, sy, ex, ey);
		cacheCanvas.drawRect(re, paint);
	}

	/** 将一条完整的路径保存下来(相当于入栈操作) */
	private void savePath(int... source) {
		// TODO 获取当前容器控件的操作类型
		if (group.getOperate() == Config.OPER_PEN) {
			memory.setOperaterType(Config.OPER_PEN);
			if (source != null) {
				if (source.length > 0)
					memory.setSource(source[0]);
			}
			// TODO 获取当前容器控件的操作类型
			group.historyOperater.create(memory);
		} else {
			// 记录当前橡皮擦相交的画线的id
			sbIds = new StringBuilder();
			// TODO 获取操作的集合
			List<HistoryMemory> historyList = group.historyOperater
					.getHistoryList();
			FlaotPoint[] points2 = getPoints(path);
			int orderType = 0;
			for (HistoryMemory memory : historyList) {
				orderType = memory.getOperaterType();
				if (orderType != Config.OPER_RUBBER
						&& orderType == Config.OPER_PEN) {
					int mid = memory.getMid();
					// 获取未包含的相交的画线的ID并保存
					if (!sbIds.toString().contains("" + mid)) {
						FlaotPoint[] points = getPoints(memory.getPath());
						// compirePoint(aa, bb, cc, dd)
						here: for (FlaotPoint point : points) {
							for (FlaotPoint point2 : points2) {
								if (point.equals(point2)) {
									sbIds.append(mid).append(",");
									break here;
								}
							}
						}
					}
				}
			}
			// 记录rubber所包括的画笔id
			memory.setIds(sbIds.toString());
			memory.setOperaterType(Config.OPER_RUBBER);
			// TODO 保存操作的集合
			group.historyOperater.create(memory);
			reDrawView(sbIds.toString());
		}
	}

	/**
	 * 根据rubber记录的ids来筛选，不需要绘制的画笔，进行重绘
	 * 
	 * @param ids
	 */
	public void reDrawView(String ids) {
		int order = 0;
		clearCanvas();
		// 获取操作的集合
		List<HistoryMemory> list = group.historyOperater.getHistoryList();
		int j = list.size();
		// 1，先获取到所有rubber指令包含的id
		for (HistoryMemory memory : list) {
			if (memory.getOperaterType() == Config.OPER_RUBBER) {
				String[] split = memory.getIds().split(",");
				for (String sp : split) {
					if (!ids.contains(sp)) {
						ids = ids + "," + sp;
					}
				}
			}
		}

		// 2，筛选不需要渲染的对应ID的画笔指令
		for (int i = 0; i < j; i++) {
			HistoryMemory memory = list.get(i);
			order = memory.getOperaterType();
			if (!ids.contains(memory.getMid() + "")) {
				if (order == Config.OPER_PEN) {// 画笔
					drawPath(memory);
				}
			}
		}
	}

	/**
	 * 根据rubber记录的ids来筛选，不需要绘制的画笔，进行重绘
	 * 
	 * @param ids
	 */
	public void undoRubber(List<HistoryMemory> list, String ids) {
		for (HistoryMemory memory : list) {
			if (ids.contains(memory.getMid() + "")) {
				if (memory.getOperaterType() == Config.OPER_PEN) {// 画笔
					drawPath(memory);
				}
			}
		}
		refreshCanvas();
	}

	/**
	 * 重现橡皮擦
	 * 
	 * @param list
	 *            总体的操作的集合
	 * @param ids
	 *            要擦除的画线的id的集合
	 */
	public void redoRubber(List<HistoryMemory> list, String ids) {
		initPaint(Color.BLACK, 4);
		paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		for (HistoryMemory memory : list) {
			if (ids.contains(memory.getMid() + "")) {
				paint.setStrokeWidth(memory.getPaint().getStrokeWidth() + 1); // 设置线宽
				cacheCanvas.drawPath(memory.getPath(), paint);
				if (isWriting || recovery) {
					ArrayList<Float> listx = new ArrayList<Float>();
					ArrayList<Float> listy = new ArrayList<Float>();
					for (int i = 0; i < memory.getDataList().size(); i++) {
						float fx = memory.getDataList().get(i)[0];
						float fy = memory.getDataList().get(i)[1];
						listx.add(fx);
						listy.add(fy);
					}
					addPencilPathBean(listx, listy, memory);
					isWriting = false;
				}
			}
		}
	}

	/** 保存画笔Path路径 */
	private void addPencilPathBean(ArrayList<Float> floatX,
			ArrayList<Float> floatY, HistoryMemory memory) {
		if (floatX.size() > 0) {
			Collections.sort(floatX);// 从小到大排序
			Collections.sort(floatY);
			float x1 = floatX.get(0);
			float y1 = floatY.get(0);
			float x2 = floatX.get(floatX.size() - 1);
			float y2 = floatY.get(floatY.size() - 1);
			pathBean = new BrushPathBean(x1, y1, x2, y2, context.currPager,
					memory);
			beans.add(pathBean);
		}
	}

	/** 保存箭头Path路径 */
	private void addPathBean(float x1, float y1, float x2, float y2,
			HistoryMemory memory) {
		pathBean = new BrushPathBean(x1, y1, x2, y2, context.currPager, memory);
		beans.add(pathBean);
	}

	/** 删除被矩形包含的箭头路径 */
	private void isContainBrushPaths() {
		beans2 = new ArrayList<BrushPathBean>();
		float Rsx = startX;
		float Rsy = startY;
		float Rex = endX;
		float Rey = endY;
		for (int i = 0; i < beans.size(); i++) {
			containBrushPaths(Rsx, Rsy, Rex, Rey, beans.get(i));
		}
		if (floatX.size() > 0) {// 是否有需要删除的画笔
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < beans2.size(); i++) {
				int mid = beans2.get(i).getMemory().getMid();
				list.add(mid);
			}
			refresh4New(list);
			drawRectangle(floatX, floatY, paint);
			list = null;
		}
	}

	/** 判断是否有要删除的画笔并且删除 */
	private void containBrushPaths(float Rsx, float Rsy, float Rex, float Rey,
			BrushPathBean brushPathBean) {
		float Bsx = brushPathBean.getSx();
		float Bsy = brushPathBean.getSy();
		float Bex = brushPathBean.getEx();
		float Bey = brushPathBean.getEy();
		int currPager = brushPathBean.getCurrPager();
		boolean isContain1 = Bsx > Rsx && Bsx < Rex && Bsy > Rsy && Bsy < Rey
				&& Bex > Rsx && Bex < Rex && Bey > Rsy && Bey < Rey;
		boolean isContain2 = Bsx > Rex && Bsx < Rsx && Bsy > Rey && Bsy < Rsy
				&& Bex > Rex && Bex < Rsx && Bey > Rey && Bey < Rsy;
		boolean isContain3 = Bsx > Rex && Bsx < Rsx && Bsy > Rsy && Bsy < Rey
				&& Bex > Rex && Bex < Rsx && Bey > Rsy && Bey < Rey;
		boolean isContain4 = Bsx > Rsx && Bsx < Rex && Bsy > Rey && Bsy < Rsy
				&& Bex > Rsx && Bex < Rex && Bey > Rey && Bey < Rsy;
		if ((isContain1 || isContain2 || isContain3 || isContain4)
				&& currPager == context.currPager) {
			floatX.add(Bsx);
			floatY.add(Bsy);
			floatX.add(Bex);
			floatY.add(Bey);
			// Message msg = context.handler.obtainMessage(context.DELETEBRUSH);
			// msg.obj = brushPathBean;
			// context.handler.sendMessage(msg);
			beans2.add(brushPathBean);
		}
	}

	/** 画出最后的大矩形 */
	private void drawRectangle(ArrayList<Float> floatX2,
			ArrayList<Float> floatY2, Paint rectAnglePaint2) {
		Collections.sort(floatX2);
		Collections.sort(floatY2);
		drawRectangle(floatX2.get(0), floatY2.get(0),
				floatX2.get(floatX2.size() - 1),
				floatY2.get(floatY2.size() - 1), rectAnglePaint2);
		floatX2.clear();
		floatY2.clear();
	}

	/** 删除画笔 */
	public void deletePathBean() {
		for (int i = 0; i < beans2.size(); i++) {
			beans.remove(beans2.get(i));
		}
		beans2.clear();
		refreshCanvas();
	}

	/** 清空画布 */
	public void clearCanvas() {
		cacheBitmap = Bitmap.createBitmap(viewWidth, viewHeight,
				android.graphics.Bitmap.Config.ARGB_4444);
		cacheCanvas.setBitmap(cacheBitmap); // 重新设置画布，相当于清空画布
	}

	/** 绘制虚线索套的过程当中刷新画布 */
	public void refresh4New() {
		// 获取操作的集合
		histList = group.historyOperater.getHistoryList();
		resetHistoryList = group.historyOperater.getResetHistoryList();
		// 清空画布
		clearCanvas();
		// 绘制历史数据（线条、橡皮擦、箭头、椭圆）
		for (int i = 0, j = resetHistoryList.size(); i < j; i++) {
			int operaterType = resetHistoryList.get(i).getOperaterType();
			if (operaterType == Config.OPER_PEN
					|| operaterType == Config.OPER_RUBBER) {
				drawPath(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_ARROW) {
				drawAL(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_ELLIPSE) {
				drawOval(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_RECT) {
				drawRect(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_LINE) {
				drawLine(resetHistoryList.get(i));
			}
			refreshCanvas();
		}
		for (int i = 0, j = histList.size(); i < j; i++) {
			int operaterType = histList.get(i).getOperaterType();
			if (operaterType == Config.OPER_PEN
					|| operaterType == Config.OPER_RUBBER) {
				drawPath(histList.get(i));
			} else if (operaterType == Config.OPER_ARROW) {
				drawAL(histList.get(i));
			} else if (operaterType == Config.OPER_ELLIPSE) {
				drawOval(histList.get(i));
			} else if (operaterType == Config.OPER_RECT) {
				drawRect(histList.get(i));
			} else if (operaterType == Config.OPER_LINE) {
				drawLine(histList.get(i));
			}
			refreshCanvas();
		}
	}

	/** 绘制箭头的过程当中刷新画布 (矩形删除时调用此方法) */
	public void refresh4New(ArrayList<Integer> mids) {
		// TODO 获取操作的集合
		histList = group.historyOperater.getHistoryList();
		resetHistoryList = group.historyOperater.getResetHistoryList();
		// 清空画布
		clearCanvas();
		// 绘制历史数据（线条、橡皮擦、箭头、椭圆）
		for (int i = 0, j = resetHistoryList.size(); i < j; i++) {
			int operaterType = resetHistoryList.get(i).getOperaterType();
			if (operaterType == Config.OPER_PEN
					|| operaterType == Config.OPER_RUBBER) {
				drawPath(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_ARROW) {
				drawAL(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_ELLIPSE) {
				drawOval(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_RECT) {
				drawRect(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_LINE) {
				drawLine(resetHistoryList.get(i));
			}
			refreshCanvas();
		}
		for (int i = 0, j = histList.size(); i < j; i++) {// 绘制历史数据（线条、橡皮擦、箭头、椭圆）
			int operaterType = histList.get(i).getOperaterType();
			if (operaterType == Config.OPER_PEN
					|| operaterType == Config.OPER_RUBBER) {
				drawPathDelete(histList.get(i), mids);
			} else if (operaterType == Config.OPER_ARROW) {
				drawALDelete(histList.get(i), mids);
			} else if (operaterType == Config.OPER_ELLIPSE) {
				drawOvalDelete(histList.get(i), mids);
			} else if (operaterType == Config.OPER_RECT) {
				drawRectDelete(histList.get(i), mids);
			} else if (operaterType == Config.OPER_LINE) {
				drawLineDelete(histList.get(i), mids);
			}
			refreshCanvas();
		}
	}

	/** 绘制箭头的过程当中刷新画布 (矩形删除时调用此方法) */
	public void refresh4New(HashMap<Integer, Integer> map) {
		// 获取操作的集合
		histList = group.historyOperater.getHistoryList();
		resetHistoryList = group.historyOperater.getResetHistoryList();
		// 清空画布
		clearCanvas();
		// 绘制历史数据（线条、橡皮擦、箭头、椭圆）
		for (int i = 0, j = resetHistoryList.size(); i < j; i++) {
			int operaterType = resetHistoryList.get(i).getOperaterType();
			if (operaterType == com.lib.play_rec.entity.Config.OPER_PEN
					|| operaterType == Config.OPER_RUBBER) {
				drawPath(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_ARROW) {
				drawAL(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_ELLIPSE) {
				drawOval(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_RECT) {
				drawRect(resetHistoryList.get(i));
			} else if (operaterType == Config.OPER_LINE) {
				drawLine(resetHistoryList.get(i));
			}
			refreshCanvas();
		}
		for (int i = 0, j = histList.size(); i < j; i++) {// 绘制历史数据（线条、橡皮擦、箭头、椭圆）
			int operaterType = histList.get(i).getOperaterType();
			if (operaterType == Config.OPER_PEN
					|| operaterType == Config.OPER_RUBBER) {
				drawPath(histList.get(i), map);
			} else if (operaterType == Config.OPER_ARROW) {
				drawAL(histList.get(i), map);
			} else if (operaterType == Config.OPER_ELLIPSE) {
				drawOval(histList.get(i), map);
			} else if (operaterType == Config.OPER_RECT) {
				drawRect(histList.get(i), map);
			} else if (operaterType == Config.OPER_LINE) {
				drawLine(histList.get(i), map);
			}
			refreshCanvas();
		}
	}

	/** 绘制一条线 */
	public void drawPath(HistoryMemory memory) {
		cacheCanvas.drawPath(memory.getPath(), memory.getPaint());
		if (isWriting || recovery) {
			ArrayList<Float> listx = new ArrayList<Float>();
			ArrayList<Float> listy = new ArrayList<Float>();
			for (int i = 0; i < memory.getDataList().size(); i++) {
				float fx = memory.getDataList().get(i)[0];
				float fy = memory.getDataList().get(i)[1];
				listx.add(fx);
				listy.add(fy);
			}
			addPencilPathBean(listx, listy, memory);
			isWriting = false;
		}
	}

	/** 绘制一个箭头 */
	public void drawAL(HistoryMemory memory) {
		float[] point1 = memory.getDataList().get(0);
		float[] point2 = memory.getDataList().get(
				memory.getDataList().size() - 1);
		drawAL(point1[0], point1[1], point2[0], point2[1], memory.getPaint());
		if (isWriting || recovery) {
			addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
			isWriting = false;
		}
	}

	/** 绘制直线 */
	public void drawLine(HistoryMemory memory) {
		float[] point1 = memory.getDataList().get(0);
		float[] point2 = memory.getDataList().get(
				memory.getDataList().size() - 1);
		drawLine(point1[0], point1[1], point2[0], point2[1], memory.getPaint());
		if (isWriting || recovery) {
			addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
			isWriting = false;
		}
	}

	/** 绘制一个椭圆 */
	public void drawOval(HistoryMemory memory) {
		float[] point1 = memory.getDataList().get(0);
		float[] point2 = memory.getDataList().get(
				memory.getDataList().size() - 1);
		drawOval(point1[0], point1[1], point2[0], point2[1], memory.getPaint());
		if (isWriting || recovery) {
			addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
			isWriting = false;
		}
	}

	/** 绘制一个矩形 */
	public void drawRect(HistoryMemory memory) {
		float[] point1 = memory.getDataList().get(0);
		float[] point2 = memory.getDataList().get(
				memory.getDataList().size() - 1);
		drawRectangle(point1[0], point1[1], point2[0], point2[1],
				memory.getPaint());
		if (isWriting || recovery) {
			addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
			isWriting = false;
		}
	}

	/** 绘制一条线 (矩形删除调用其方法ArrayList) */
	public void drawPathDelete(HistoryMemory memory, ArrayList<Integer> mids) {
		try {
			Paint paint2 = memory.getPaint();
			for (int i = 0; i < mids.size(); i++) {
				if (memory.getMid() == mids.get(i)) {
					paint2.setColor(Color.RED);
				}
			}
			cacheCanvas.drawPath(memory.getPath(), paint2);
			if (isWriting || recovery) {
				ArrayList<Float> listx = new ArrayList<Float>();
				ArrayList<Float> listy = new ArrayList<Float>();
				for (int i = 0; i < memory.getDataList().size(); i++) {
					float fx = memory.getDataList().get(i)[0];
					float fy = memory.getDataList().get(i)[1];
					listx.add(fx);
					listy.add(fy);
				}
				addPencilPathBean(listx, listy, memory);
				isWriting = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 绘制直线(矩形删除调用其方法ArrayList) */
	public void drawLineDelete(HistoryMemory memory, ArrayList<Integer> mids) {
		try {
			Paint paint2 = memory.getPaint();
			for (int i = 0; i < mids.size(); i++) {
				if (memory.getMid() == mids.get(i)) {
					paint2.setColor(Color.RED);
				}
			}
			float[] point1 = memory.getDataList().get(0);
			float[] point2 = memory.getDataList().get(
					memory.getDataList().size() - 1);
			drawLine(point1[0], point1[1], point2[0], point2[1], paint2);
			if (isWriting || recovery) {
				addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
				isWriting = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 绘制一个箭头(矩形删除调用其方法ArrayList) */
	public void drawALDelete(HistoryMemory memory, ArrayList<Integer> mids) {
		try {
			Paint paint2 = memory.getPaint();
			for (int i = 0; i < mids.size(); i++) {
				if (memory.getMid() == mids.get(i)) {
					paint2.setColor(Color.RED);
				}
			}
			float[] point1 = memory.getDataList().get(0);
			float[] point2 = memory.getDataList().get(
					memory.getDataList().size() - 1);
			drawAL(point1[0], point1[1], point2[0], point2[1], paint2);
			if (isWriting || recovery) {
				addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
				isWriting = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 绘制一个椭圆(矩形删除调用其方法ArrayList) */
	public void drawOvalDelete(HistoryMemory memory, ArrayList<Integer> mids) {
		try {
			Paint paint2 = memory.getPaint();
			for (int i = 0; i < mids.size(); i++) {
				if (memory.getMid() == mids.get(i)) {
					paint2.setColor(Color.RED);
				}
			}
			float[] point1 = memory.getDataList().get(0);
			float[] point2 = memory.getDataList().get(
					memory.getDataList().size() - 1);
			drawOval(point1[0], point1[1], point2[0], point2[1], paint2);
			if (isWriting || recovery) {
				addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
				isWriting = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 绘制矩形(矩形删除调用其方法ArrayList) */
	public void drawRectDelete(HistoryMemory memory, ArrayList<Integer> mids) {
		try {
			Paint paint2 = memory.getPaint();
			for (int i = 0; i < mids.size(); i++) {
				if (memory.getMid() == mids.get(i)) {
					paint2.setColor(Color.RED);
				}
			}
			float[] point1 = memory.getDataList().get(0);
			float[] point2 = memory.getDataList().get(
					memory.getDataList().size() - 1);
			drawRectangle(point1[0], point1[1], point2[0], point2[1], paint2);
			if (isWriting || recovery) {
				addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
				isWriting = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 绘制一条线 (矩形删除调用其方法HashMap) */
	public void drawPath(HistoryMemory memory, HashMap<Integer, Integer> map) {
		try {
			Paint paint2 = memory.getPaint();
			Iterator<Entry<Integer, Integer>> iterator = map.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Integer> next = iterator.next();
				if (memory.getMid() == next.getKey()) {
					paint2.setColor(next.getValue());
				}
			}
			cacheCanvas.drawPath(memory.getPath(), paint2);
			if (isWriting || recovery) {
				ArrayList<Float> listx = new ArrayList<Float>();
				ArrayList<Float> listy = new ArrayList<Float>();
				for (int i = 0; i < memory.getDataList().size(); i++) {
					float fx = memory.getDataList().get(i)[0];
					float fy = memory.getDataList().get(i)[1];
					listx.add(fx);
					listy.add(fy);
				}
				addPencilPathBean(listx, listy, memory);
				isWriting = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 绘制一个箭头(矩形删除调用其方法HashMap) */
	public void drawAL(HistoryMemory memory, HashMap<Integer, Integer> map) {
		try {
			Paint paint2 = memory.getPaint();
			Iterator<Entry<Integer, Integer>> iterator = map.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Integer> next = iterator.next();
				if (memory.getMid() == next.getKey()) {
					paint2.setColor(next.getValue());
				}
			}
			float[] point1 = memory.getDataList().get(0);
			float[] point2 = memory.getDataList().get(
					memory.getDataList().size() - 1);
			drawAL(point1[0], point1[1], point2[0], point2[1], paint2);
			if (isWriting || recovery) {
				addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
				isWriting = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 绘制一条直线(矩形删除调用其方法HashMap) */
	public void drawLine(HistoryMemory memory, HashMap<Integer, Integer> map) {
		try {
			Paint paint2 = memory.getPaint();
			Iterator<Entry<Integer, Integer>> iterator = map.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Integer> next = iterator.next();
				if (memory.getMid() == next.getKey()) {
					paint2.setColor(next.getValue());// next.getValue()
				}
			}
			float[] point1 = memory.getDataList().get(0);
			float[] point2 = memory.getDataList().get(
					memory.getDataList().size() - 1);
			drawLine(point1[0], point1[1], point2[0], point2[1], paint2);
			if (isWriting || recovery) {
				addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
				isWriting = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 绘制一个椭圆(矩形删除调用其方法HashMap) */
	public void drawOval(HistoryMemory memory, HashMap<Integer, Integer> map) {
		try {
			Paint paint2 = memory.getPaint();
			Iterator<Entry<Integer, Integer>> iterator = map.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Integer> next = iterator.next();
				if (memory.getMid() == next.getKey()) {
					paint2.setColor(next.getValue());
				}
			}
			float[] point1 = memory.getDataList().get(0);
			float[] point2 = memory.getDataList().get(
					memory.getDataList().size() - 1);
			drawOval(point1[0], point1[1], point2[0], point2[1], paint2);
			if (isWriting || recovery) {
				addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
				isWriting = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 绘制一个矩形(矩形删除调用其方法HashMap) */
	public void drawRect(HistoryMemory memory, HashMap<Integer, Integer> map) {
		try {
			Paint paint2 = memory.getPaint();
			Iterator<Entry<Integer, Integer>> iterator = map.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<Integer, Integer> next = iterator.next();
				if (memory.getMid() == next.getKey()) {
					paint2.setColor(next.getValue());
				}
			}
			float[] point1 = memory.getDataList().get(0);
			float[] point2 = memory.getDataList().get(
					memory.getDataList().size() - 1);
			drawRectangle(point1[0], point1[1], point2[0], point2[1], paint2);
			if (isWriting || recovery) {
				addPathBean(point1[0], point1[1], point2[0], point2[1], memory);
				isWriting = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行OnDraw方法 之后执行的回调
	 * 
	 * @author Administrator
	 */
	public interface OnDrawListener {
		void onDrawAfter();
	}

	/** 获取Path的所以坐标点 ----start----- */
	private FlaotPoint[] getPoints(Path path) {
		PathMeasure pm = new PathMeasure(path, false);
		int length = (int) pm.getLength();
		FlaotPoint[] pointArray = new FlaotPoint[length];
		float[] aCoordinates = new float[2];
		for (int i = 0; i < length; i++) {
			pm.getPosTan(i, aCoordinates, null);
			pointArray[i] = new FlaotPoint(aCoordinates[0], aCoordinates[1]);
		}
		return pointArray;
	}

	private double determinant(double v1, double v2, double v3, double v4) // 行列式
	{
		return (v1 * v4 - v2 * v3);
	}

	/**
	 * 比较两个点，是否相交
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private boolean compirePoint(FlaotPoint a, FlaotPoint b, FlaotPoint c,
			FlaotPoint d) {
		double delta = determinant(b.getX() - a.getX(), c.getX() - d.getX(),
				b.getY() - a.getY(), c.getY() - d.getY());
		if (delta <= (1e-6) && delta >= -(1e-6)) // delta=0，表示两线段重合或平行
		{
			return false;
		}
		double namenda = determinant(c.getX() - a.getX(), c.getX() - d.getX(),
				c.getY() - a.getY(), c.getY() - d.getY()) / delta;
		if (namenda > 1 || namenda < 0) {
			return false;
		}
		double miu = determinant(b.getX() - a.getX(), c.getX() - a.getX(),
				b.getY() - a.getY(), c.getY() - a.getY())
				/ delta;
		if (miu > 1 || miu < 0) {
			return false;
		}
		return true;
	}

	/** 获取Path的所以坐标点 ----end----- */

	@Override
	protected void onAttachedToWindow() {
		if (!isRegister) {
			eventBus.register(this);
			isRegister = true;
		}
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		if (isRegister) {
			eventBus.unregister(this);
			isRegister = false;
		}
		super.onDetachedFromWindow();
	}

}
