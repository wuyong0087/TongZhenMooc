package com.lib.play_rec.rec;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.play_rec.ProgressDialogView;
import com.lib.play_rec.R;
import com.lib.play_rec.adapter.CameraImageAdapter;
import com.lib.play_rec.adapter.RecordPagerAdapter;
import com.lib.play_rec.emue.RecBackgroudTheme;
import com.lib.play_rec.emue.RecBtnStatusEvent;
import com.lib.play_rec.emue.RecMeterialEvent;
import com.lib.play_rec.entity.BrushPathBean;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.entity.RecommendBean;
import com.lib.play_rec.entity.WorksBean;
import com.lib.play_rec.listener.ColorSetListener;
import com.lib.play_rec.listener.FodderSetListener;
import com.lib.play_rec.listener.OptTypeSetListener;
import com.lib.play_rec.listener.SaveSetListener;
import com.lib.play_rec.listener.StrokeSetListener;
import com.lib.play_rec.listener.ToolBoxListener;
import com.lib.play_rec.net.HttpRequest;
import com.lib.play_rec.play.VideoPlayerBaseAty;
import com.lib.play_rec.rec.RecordView.OnDrawListener;
import com.lib.play_rec.utils.BitmapUtil;
import com.lib.play_rec.utils.CommUtil;
import com.lib.play_rec.utils.DateUtil;
import com.lib.play_rec.utils.HexUtil;
import com.lib.play_rec.utils.ScreenManager;
import com.ypy.eventbus.EventBus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作品的制作和重新编辑
 * 
 * @author Administrator
 * 
 */
public class AtyRecord extends VideoPlayerBaseAty implements OnClickListener,
		OnDrawListener, OnPageChangeListener {
	// 数码笔对象
	protected static PenDigitalBean penBean = null;
	// 录制控件容器
	private ViewPager recordPager;
	// 容器适配器
	private RecordPagerAdapter pagerAdapter;
	// 录制布局对象
	protected RecordViewGroup viewGroup = null;
	// 录制布局对象集合
	public List<RecordViewGroup> viewGroupList;
	// 菜单操作选项。1-画笔颜色选择框；2-当前画笔颜色；3-画笔粗细；4-工具箱；5-数码笔
	private ImageView colorBtn, colorMarkBtn, strokeBtn, toolboxBtn,
			digitalBtn;
	// 前进和回退操作
	private ImageView backBtn, preBtn;
	// 上一页和下一页
	private ImageView frontBtn, nextBtn;
	// 录制和暂停操作控件对象
	private ImageView recBtn;
	// private ImageButton resetBtn;
	// 保存操作
	public static ImageView saveBtn;
	// private ImageButton quitBtn;
	// 页数显示控件
	private TextView pageTV;
	// 当前录制时间显示控件
	private TextView recTimeTV;
	/** 素材操作 */
	// 素材选择
	private ImageView fodderBtn;
	// 操作类型选择
	private ImageView opttypeBtn;
	// 索套虚线框
	private TextView deletebrush;
	// 删除操作对象
	private BrushPathBean bean;
	// 删除操作对象集合
	private List<BrushPathBean> beans;
	// 多媒体播放器
	private MediaCapture mediaCapture;
	// 进度控件
	private ProgressDialogView progress;

	/** 全局变量 */
	public static int arrowStroke = 4; // 箭头线宽
	public static int shapeStroke = 4; // 图形线宽
	public int backgroundOrder = 1; // 背景的序号，默认第一张

	/** 翻页变量 */
	private int i = 0;

	public static boolean nextPage = false;// 判断是否下一页
	public static boolean recorded = false;// 判断是否已经开始录制变量(默认可以导入)
	public static boolean isImports = false;
	public static boolean recording = false; // 视频录制状态

	/** 各种操作弹出对象 */
	// 素材库弹出框
	private FodderSetListener fodderSet = null;
	// 颜色选择弹出框
	private ColorSetListener colorSet = null;
	// 操作类型弹出框
	private OptTypeSetListener opttypeSet = null;
	// 笔画粗细
	private StrokeSetListener penStrSet = null;
	// 工具箱
	private ToolBoxListener toolBoxSet = null;
	// 保存视频弹出框
	private SaveSetListener saveSet = null;
	// private AnewRecordListener anewRecord = null;// 录制暂停弹出框

	private Handler penHandler;
	/** Handler操作标示 */
	public final int DONE = 0x1; // 保存成功
	// public final int ISWRITE = 90; // 完成操作
	public final int SAVEWRONG = 0x2; // 保存视频失败
	public final int SAVEIMGSUCC = 0x3;
	public final int SAVEIMGFAIL = 0x4;
	public final int DELETEBRUSH = 0x5;// 删除画笔
	public final int DELETEBRUSH1 = 0x6;// 隐藏deletebrush
	public final int SAVE_MATERIAL_OK = 0x7;//
	public final int SAVEIMGFAIL1 = 0x8;//
	public final int SAVEIMGSUCC2 = 0x9;
	public final int SAVEIMGSUCC3 = 0xa;
	public final int DIGITAL_PEN = 0xb; // 数码笔状态
	public final int NORMAL_PEN = 0xc; // 手写状态
	public final int ADD_BAD_IMG = 0xd; // 素材资源有问题

	public final int RESET_NOTE_WACOMVIEW = 0x12; //
	public final int SAVE_RECORD = 0x13; // 保存视频

	private WakeLock wakeLock = null;
	private List<RecommendBean> selCBPosList;

	public static boolean isRamote = false; // 是否是远程开启

	// 讲义
	private JSONArray commandArray;
	protected ArrayList<HistoryMemory> memoryList = null;
	private NoteWacomViewGroup noteWacomView;
	private int noteColor, noteStroke;
	private WorksBean worksBean;
	private String notePath;// 讲义路径
	protected boolean isEdit = false;
	private ArrayList<ArrayList<HistoryMemory>> arrayLists = new ArrayList<ArrayList<HistoryMemory>>();
	private int groupPager;//
	private ArrayList<Integer> bgList = null;

	private JsonOperater jsonOperater;
	private List<String> selList;
	private CameraImageAdapter imgAdapter;
	private boolean isDraft = false;// 是否是录制草稿
	private ArrayList<String> PPTImagePaths;// 素材路径保存
	private boolean isMarkStart; // 是否已经存储Start指令

	// 事件分发对象
	private EventBus eventBus;
	private boolean isRegister;
	// 截图时要确定的当前的最大页数
	private int maxPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initConfig();
		initView();
		initViewGroup();
		setPageTV();
		// 设置屏幕的配置
		setScreenConfig();

		// 注册事件分发总线对象
		eventBus = EventBus.getDefault();
		if (!isRegister) {
			eventBus.register(this);
			isRegister = true;
		}
	}

	private void initConfig() {
		currPager = -1;
		PowerManager powerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"My Lock");
		getWindow().setBackgroundDrawable(null);
		penHandler = new Handler();
		penBean = new PenDigitalBean();
		jsonOperater = JsonOperater.getInstance();
		mediaCapture = new MediaCapture();
		WorksBean vb = new WorksBean();
		globalInit.setVideoBean(vb); // 保存视频对象信息
		worksBean = (WorksBean) getIntent().getSerializableExtra("noteBean");
		viewGroupList = new ArrayList<RecordViewGroup>();
		pagerAdapter = new RecordPagerAdapter();
		pagerAdapter.setViewGroupList(viewGroupList);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void onEventMainThread(RecBtnStatusEvent event) {
		switch (event) {
		case REDO_ON_UNDO_OFF:
			backBtn.setEnabled(false);
			backBtn.setAlpha(0.2f);// 0--255为透明度值
			preBtn.setEnabled(true);
			preBtn.setAlpha(1f);// 0--255为透明度值
			break;
		case REDO_ON:
			preBtn.setEnabled(true);
			preBtn.setAlpha(1f);// 0--255为透明度值
			break;
		case UNDO_ON_REDO_OFF:
			backBtn.setEnabled(true);
			backBtn.setAlpha(1f);// 0--255为透明度值
			preBtn.setEnabled(false);
			preBtn.setAlpha(0.2f);// 0--255为透明度值
			break;
		case UNDO_ON:
			backBtn.setEnabled(true);
			backBtn.setAlpha(1f);// 0--255为透明度值
			break;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		colorBtn = (ImageView) findViewById(R.id.color_btn);
		colorMarkBtn = (ImageView) findViewById(R.id.color_btn_mark);
		strokeBtn = (ImageView) findViewById(R.id.stroke_btn);
		toolboxBtn = (ImageView) findViewById(R.id.toolbox_btn);
		backBtn = (ImageView) findViewById(R.id.back_btn);
		preBtn = (ImageView) findViewById(R.id.pre_btn);
		frontBtn = (ImageView) findViewById(R.id.front_btn);
		nextBtn = (ImageView) findViewById(R.id.next_btn);
		recBtn = (ImageView) findViewById(R.id.rec_btn);
		// resetBtn = (ImageButton) findViewById(R.id.reset_btn);
		saveBtn = (ImageView) findViewById(R.id.save_btn);
		// quitBtn = (ImageButton) findViewById(R.id.quit_btn);
		pageTV = (TextView) findViewById(R.id.page_text);
		recTimeTV = (TextView) findViewById(R.id.rec_time_tv);
		fodderBtn = (ImageView) findViewById(R.id.fodder_btn);
		opttypeBtn = (ImageView) findViewById(R.id.opttype_btn);

		digitalBtn = (ImageView) findViewById(R.id.digital_btn);
		digitalBtn.setOnClickListener(this);
		recordPager = (ViewPager) findViewById(R.id.paint_pager);
		recordPager.setAdapter(pagerAdapter);
		wacomView = new RecordView(this);
		wacomView.setDrawListener(this);
		// 设置不可点击
		frontBtn.setAlpha(0.2f);
		frontBtn.setEnabled(false);

		strokeBtn.setOnClickListener(this);
		toolboxBtn.setOnClickListener(this);
		colorBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		preBtn.setOnClickListener(this);
		fodderBtn.setOnClickListener(this);
		recBtn.setOnClickListener(this);
		// resetBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		// quitBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		opttypeBtn.setOnClickListener(this);
		frontBtn.setOnClickListener(this);
		frontBtn.setEnabled(false);
	}

	/**
	 * 设置屏幕的配置
	 */
	private void setScreenConfig() {
		penBean.initVal(5000, 100, -5361, 13777);
		penBean.calculateScale(
				globalInit.getScreenWidth() - globalInit.dip2px(61 + 61),
				GlobalInit.getInstance().getScreenHeight());
	}

	/** 在翻页的时候判断回退和前进按钮是否可以点击 */
	private void setBtEnablea() {
		if (currPager == 0) {
			frontBtn.setAlpha(0.2f);
			frontBtn.setEnabled(false);
		} else {
			frontBtn.setAlpha(1f);
			frontBtn.setEnabled(true);
		}
		if (viewGroup.n > 0) {
			backBtn.setEnabled(true);// 设置回退按钮为可以点击
			backBtn.setAlpha(1f);
		} else {
			backBtn.setEnabled(false);// 设置回退按钮为不可点击
			backBtn.setAlpha(0.2f);
		}
		if (viewGroup.preBtnIsTrue) {
			preBtn.setEnabled(true);
			preBtn.setAlpha(1f);
		} else {
			preBtn.setEnabled(false);
			preBtn.setAlpha(0.2f);
		}
	}

	private void initViewGroup() {
		int bg = 1;
		if (viewGroup != null) {
			bg = viewGroup.getBackgroundOrder();
		}
		viewGroup = new RecordViewGroup(this);
		if (bgList != null && bgList.size() > 0) {
			bg = bgList.get(0);
//			viewGroup.setBackground(bg, false, true);
			bgList.remove(0);// 删除
		} else {
//			viewGroup.setBackground(bg, false, true);
		}
		viewGroupList.add(++currPager, viewGroup);
		if (currPager != viewGroupList.size() - 1) {
			recordPager.removeAllViews();
		}
		pagerAdapter.notifyDataSetChanged();
		// 设置回退按钮为不可点击
		backBtn.setEnabled(false);
		backBtn.setAlpha(0.2f);// 0--255为透明度值
		preBtn.setEnabled(false);
		preBtn.setAlpha(0.2f);// 0--255为透明度值

		opttypeBtn.setImageResource(R.drawable.opt_pencil);// 新建一页，设置操作类型和图片为画笔

		viewGroup.setBackground(RecBackgroudTheme.WHITE);
		wacomView.setGroup(viewGroup);
		viewGroup.addView(wacomView);
		recordPager.setCurrentItem(currPager);
		recordPager.setOnPageChangeListener(this);
		recordPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.digital_btn) {// 连接数码笔
		} else if (id == R.id.opttype_btn) {// 选中操作类型
			if (opttypeSet == null) {
				opttypeSet = new OptTypeSetListener(this, opttypeBtn);
			}
			opttypeSet.showPopupWindow(v, viewGroup);
		} else if (id == R.id.color_btn) {// 颜色设置
			if (colorSet == null) {
				colorSet = new ColorSetListener(this, viewGroup, opttypeBtn,
						toolboxBtn, colorBtn, colorMarkBtn);
			}
			colorSet.showPopupWindow(colorBtn);
		} else if (id == R.id.stroke_btn) {// 笔画粗细设置
			if (penStrSet == null) {
				penStrSet = new StrokeSetListener(this);
			}
			penStrSet.showPopupWindow(v);
			viewGroup.setOperate(Config.OPER_PEN);
		} else if (id == R.id.toolbox_btn) {// 工具箱
			if (toolBoxSet == null) {
				toolBoxSet = new ToolBoxListener(toolboxBtn, this);
			}
			toolBoxSet.showPopupWindow(v, viewGroup);
			viewGroup.setOperate(Config.OPER_PEN);
		} else if (id == R.id.back_btn) {// 回退操作
			viewGroup.undo();
		} else if (id == R.id.pre_btn) {// 前进操作
			viewGroup.redo();
		} else if (id == R.id.fodder_btn) {// 素材库
			if (fodderSet == null) {
				fodderSet = new FodderSetListener(this, opttypeBtn);
			}
			fodderSet.showPopupWindow(v, viewGroup);
			if (recording) {
				pauseRecord();
			}
		} else if (id == R.id.rec_btn) {// 录制视频或暂停录制
			if (recording) {
				pauseRecord();
			} else {
				if (!isMarkStart) {
					jsonOperater.startOrder();
				}
				startRecord();
			}
		} else if (id == R.id.save_btn) {// 完成按钮
			if (saveSet == null) {
				saveSet = new SaveSetListener(this, worksBean);
			}
			jsonOperater.endOrder();
			saveSet.showPopupWindow(saveBtn);
		} else if (id == R.id.front_btn) {
			toolboxBtn.setImageResource(R.drawable.toolbox);
			frontPage();
		} else if (id == R.id.next_btn) {
			toolboxBtn.setImageResource(R.drawable.toolbox);
			nextPage();
		}
	}

	/** 回复到开始录的状态，清空已有数据 */
	public void delMemories() {
		try {
			// Constant.flag = true; // TODO 何用？
			jsonOperater.delCommandArray();
			for (int i = 0; i < viewGroupList.size(); i++) {
				RecordViewGroup group = viewGroupList.get(i);
				group.historyOperater.delMemories(wacomView);
			}
			wacomView.recovery = false;
			recBtn.setImageResource(R.drawable.rec_start_bt);
			recording = false;
			recTimeHandler.removeCallbacks(runnalbe); // 停止录制时间
			recTimeTV.setText("00:00");
			mediaCapture.clearRecord(); // 清空音频
			mediaCapture.pauseRecord();// 关闭录音 设置为空
			mediaCapture.stopRecord();
			mediaCapture = null;
			if (mediaCapture == null) {// 重新新建录音对象
				mediaCapture = new MediaCapture();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 加载推荐图片到本地
	public synchronized void loadRecommend2(final String path) {
		progress = new ProgressDialogView(this, R.string.image_inputing);
		progress.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String fileName = DateUtil.getTimeMillisStr()
						+ Config.LOCAL_IMAGE_SUFFIX;
				String savePath = jsonOperater.getVideoFullPath() + fileName;
				BitmapUtil.saveImageFile(path, savePath);
				jsonOperater.setTempImgName(fileName);
				try {
					Message msg = new Message();
					msg.what = SAVEIMGSUCC2;
					msg.obj = savePath;
					handler.sendMessage(msg);
				} catch (Exception e) {
					handler.sendEmptyMessage(SAVEIMGFAIL);
					CommUtil.delFile(savePath, true);
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 加载推荐图片到本地
	public synchronized void loadRecommend3(final List<String> selList,
			CameraImageAdapter imgAdapter) {
		this.selList = selList;
		this.imgAdapter = imgAdapter;
		progress = new ProgressDialogView(this, R.string.image_inputing);
		progress.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int j = 0; j < selList.size(); j++) {
					final String bean = selList.get(j);
					String fileCameraName = DateUtil.getTimeMillisStr()
							+ Config.LOCAL_IMAGE_SUFFIX;
					String savePath = jsonOperater.getVideoFullPath()
							+ fileCameraName;
					BitmapUtil.saveImageFile(bean, savePath);
					String[] str = { fileCameraName, savePath };
					try {
						Message msg = new Message();
						msg.what = SAVEIMGSUCC3;
						msg.obj = str;
						handler.sendMessage(msg);
					} catch (Exception e) {
						handler.sendEmptyMessage(SAVEIMGFAIL);
						CommUtil.delFile(savePath, true);
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == RecMeterialEvent.PHONE_PIC.getTag()) {
			loadResByPic(data);
		} else if (requestCode == RecMeterialEvent.PHONE_CAMERA.getTag()) {
			loadResByCamera();
		}
	}

	/**
	 * 从相机中加载图片
	 */
	private void loadResByCamera() {
		penHandler.post(new Runnable() {
			@Override
			public void run() {
				progress = new ProgressDialogView(AtyRecord.this,
						R.string.image_load);
				progress.show();
			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				String filePath = globalInit.getTempPath()
						+ jsonOperater.getTempImgName();
				String fileName = jsonOperater.getTempImgName();
				String savePath = jsonOperater.getVideoFullPath() + fileName;
				try {
					Bitmap bmp = BitmapUtil.getBitmapFromPath(filePath,
							savePath, viewGroup.getWidth(),
							viewGroup.getHeight(), 500);
					Message msg = new Message();
					msg.what = SAVEIMGSUCC;
					msg.obj = bmp;
					handler.sendMessage(msg);
				} catch (IOException e) {
					handler.sendEmptyMessage(SAVEIMGFAIL);
					e.printStackTrace();
					CommUtil.delFile(savePath, true);
				} finally {
					CommUtil.delFile(filePath, true);
				}
			}
		}).start();
	}

	/**
	 * 从相册中加载图片
	 * 
	 * @param data
	 *            数据库中获取到的图片数据
	 */
	private void loadResByPic(Intent data) {
		progress = new ProgressDialogView(AtyRecord.this, R.string.image_load);
		progress.show();
		final Intent dt = data;
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (dt != null) {
					Uri uri = dt.getData();
					if (uri != null) {
						String fileName = DateUtil.getTimeMillisStr()
								+ Config.LOCAL_IMAGE_SUFFIX;
						String savePath = jsonOperater.getVideoFullPath()
								+ fileName;
						try {
							String[] proj = { MediaStore.Images.Media.DATA };
							Cursor cursor = managedQuery(uri, proj, null, null,
									null);
							int imgIndex = cursor
									.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
							cursor.moveToFirst();
							String filePath = cursor.getString(imgIndex);
							jsonOperater.setTempImgName(fileName);
							Bitmap bmp = BitmapUtil.getBitmapFromPath(filePath,
									savePath, viewGroup.getWidth(),
									viewGroup.getHeight(), 500);
							Message msg = new Message();
							msg.what = SAVEIMGSUCC;
							msg.obj = bmp;
							handler.sendMessage(msg);
						} catch (Exception e) {
							handler.sendEmptyMessage(SAVEIMGFAIL);
							e.printStackTrace();
							CommUtil.delFile(savePath, true);
						}
					}
				}
			}
		}).start();
	}

	// 加载推荐图片到本地
	public synchronized void loadRecommend(final RecommendBean bean) {
		progress = new ProgressDialogView(this, R.string.image_load);
		progress.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String fileName = DateUtil.getTimeMillisStr()
						+ Config.LOCAL_IMAGE_SUFFIX;
				String filePath = globalInit.getTempPath() + fileName;
				String savePath = jsonOperater.getVideoFullPath() + fileName;
				HttpRequest.getInstance().downFile(bean.getCommImgUrl(),
						new File(filePath));
				jsonOperater.setTempImgName(fileName);
				try {
					Bitmap bmp = BitmapUtil.getBitmapFromPath(filePath,
							savePath, viewGroup.getWidth(),
							viewGroup.getHeight(), 500);
					Message msg = new Message();
					msg.what = SAVEIMGSUCC;
					msg.obj = bmp;
					handler.sendMessage(msg);
				} catch (IOException e) {
					handler.sendEmptyMessage(SAVEIMGFAIL);
					e.printStackTrace();
					CommUtil.delFile(savePath, true);
				} finally {
					CommUtil.delFile(filePath, true);
				}
			}
		}).start();
	}

	/**
	 * 添加多个云素材
	 * 
	 * @param selCBPosList
	 *            用户选中的云素材
	 */
	public synchronized void loadRecommends(
			final List<RecommendBean> selCBPosList) {
		PPTImagePaths = new ArrayList<String>();
		if (selCBPosList.size() <= 0) {
			handler.sendEmptyMessage(SAVEIMGFAIL1);
			return;
		}
		progress = new ProgressDialogView(this, R.string.image_load);
		progress.show();
		this.selCBPosList = selCBPosList;
		final int size = selCBPosList.size();
		new Thread() {
			public void run() {
				RecommendBean bean = null;
				for (int j = 0; j < size; j++) {
					bean = selCBPosList.get(j);
					String fileName = j + "image"
					// DateUtil.getTimeMillisStr()
							+ Config.LOCAL_IMAGE_SUFFIX;
					String savePath = jsonOperater.getVideoFullPath();
					savePath = savePath + fileName;
					try {
						HttpRequest.getInstance().downFile(
								bean.getCommImgUrl(), new File(savePath));
						jsonOperater.setTempImgName(fileName);
						PPTImagePaths.add(savePath);// PPT保存路径
						Message msg = new Message();
						msg.what = SAVE_MATERIAL_OK;
						msg.obj = savePath;
						handler.sendMessage(msg);
						Thread.sleep(200);
					} catch (Exception e) {
						handler.sendEmptyMessage(SAVEIMGFAIL1);
						CommUtil.delFile(savePath, true);
						e.printStackTrace();
					}
				}
				copyPPTImages();
			}
		}.start();
	}

	/** 复制ppt图片到指定文件 */
	private void copyPPTImages() {
		for (int i = 0; i < PPTImagePaths.size(); i++) {
			File file = new File(jsonOperater.getVideoFullPath() + i
					+ Config.LOCAL_IMAGE_SUFFIX);
			if (!file.exists()) {
				BitmapUtil.saveImageFile(PPTImagePaths.get(i), JsonOperater
						.getInstance().getVideoFullPath()
						+ i
						+ Config.LOCAL_IMAGE_SUFFIX);
			}
		}
	}

	/** 暂停录制 */
	public synchronized void pauseRecord() {
		if (recording) {
			recBtn.setImageResource(R.drawable.rec_start_bt);
			mediaCapture.pauseRecord();
			jsonOperater.setSumTime(); // 暂停后，记录总录制时间
			recording = false;
			jsonOperater.setRecording(recording);
			recTimeHandler.removeCallbacks(runnalbe); // 暂停录制时间

			// 提醒用户已暂停
			showMessage(R.string.record_pause); // TODO 暂停的操作
			startFlick(recBtn, 1000);
		}
	}

	/** 开始录制 */
	public synchronized void startRecord() {
		if (!recording) {
			wacomView.recovery = true;// 设置可以一键恢复
			recBtn.setImageResource(R.drawable.rec_pause_bt);
			mediaCapture.startRecord(); // 开始录制音频
			jsonOperater.setStartTime(DateUtil.getTimeMillisLong()); // 记录开始录制时间
			recorded = true;
			recording = true;
			jsonOperater.setRecording(recording);
			recTimeHandler.postDelayed(runnalbe, 1000); // 显示录制时间
			// 关闭提醒
			stopFlick(recBtn); // 暂停取消相关
		}
	}

	/**
	 * 保存作品
	 * 
	 * @param worksDraft
	 *            区分保存的作品类型是微课还是讲义。如果是true，则保存讲义；如果是false，则保存微课。
	 */
	public synchronized void saveRecord(final boolean worksDraft) {
		this.isDraft = worksDraft;
		new Thread() {
			public void run() {
				Looper.prepare();
				progress = new ProgressDialogView(AtyRecord.this,
						R.string.vedio_saveing);
				progress.show();
				Looper.loop();
			};
		}.start();
		if (worksDraft)
			// 保存笔迹的截图功能
			takeScreenShot();
		else
			getAndSaveCurrentImage(viewGroup, currPager);
		if (worksDraft && PPTImagePaths != null) {
			copyPPTImages();
		}
		if (worksBean != null) {
			perfectInstruct();
		} else {
			handler.sendEmptyMessage(SAVE_RECORD);
		}
	}

	/**
	 * 获取截图
	 */
	private void takeScreenShot() {
		isFliping = true;
		if (currPager < maxPage) {
			// 将标识设置为true，不进行JSon数据的保存
			for (int i = 0; i < maxPage; i++) {
				nextPage();
			}

		}
		getAndSaveCurrentImage(viewGroup, currPager);
		isSave = true;
		for (int i = 0; i <= maxPage; i++) {
			frontPage();
		}
		isFliping = false;
		isSave = false;
	}

	/** 完善json数据 */
	private void perfectInstruct() {
		int num = currPager;
		for (int i = 0; i < num; i++) {
			jsonOperater.flipPage(-1, currPager);
			currPager--;
			if (currPager < arrayLists.size()) {
				ArrayList<HistoryMemory> arrayList = arrayLists.get(currPager);
				RecordViewGroup group = viewGroupList.get(currPager);
				group.historyOperater.resetMemorier(wacomView, arrayList);
			}
		}
		handler.sendEmptyMessage(SAVE_RECORD);
	}

	// 如果是保存操作，则该标识为true，进行截图，上下翻页不必要将数据添加到Json文件中
	private boolean isFliping, isSave;

	/** ---------上一页操作------------- */
	public synchronized void frontPage() {
		if (isSave) {
			getAndSaveCurrentImage(viewGroup, currPager);// 截屏
		}
		if (currPager < 1) {
			currPager = 0;
			frontBtn.setEnabled(false);
			return;
		} else {
			currPager--;
			viewGroup.removeView(wacomView);
			recordPager.setCurrentItem(currPager);
			handler.sendEmptyMessage(DELETEBRUSH1);// 隐藏删除字体
			if (Config.isDigital) {
				opttypeBtn.setImageResource(R.drawable.digital_pen_focus);
			} else {
				opttypeBtn.setImageResource(R.drawable.opt_pencil);
			}
			if (currPager == 0) {
				frontBtn.setEnabled(false);
				frontBtn.setAlpha(0.2f);
			}
			// TODO 保持录制的背景不变
			// viewGroup.setBackground();
			if (!isFliping) {
				// 保存记录到文本
				jsonOperater.flipPage(-1, currPager);
			}
		}
	}

	/** ------------下一页操作--------- */
	public synchronized void nextPage() {
		nextPage = true;
		if (!jsonOperater.isShotScreen()) {
			jsonOperater.screenShot(viewGroup);
		}
		if (Config.isDigital) {
			opttypeBtn.setImageResource(R.drawable.digital_pen_focus);
		} else {
			opttypeBtn.setImageResource(R.drawable.opt_pencil);
		}
		// 隐藏删除字体
		handler.sendEmptyMessage(DELETEBRUSH1);
		if (currPager >= viewGroupList.size() - 1 && !isFliping) {
			// 新建一页
			newBlankPage();
			return;
		} else {
			currPager++;
			viewGroup.removeView(wacomView);
			recordPager.setCurrentItem(currPager);
			if (!frontBtn.isEnabled()) {
				frontBtn.setEnabled(true);
				frontBtn.setAlpha(1f);
			}
			// TODO 保持录制的背景不变
			viewGroup.setBackground(RecBackgroudTheme.WHITE);
			if (!isFliping) {
				// 保存记录到文本
				jsonOperater.flipPage(1, currPager);
			}
		}
	}

	/**
	 * 新建一页
	 */
	public synchronized void newBlankPage() {
		viewGroup.removeView(wacomView);
		initViewGroup();
		maxPage = currPager;
		recordPager.setCurrentItem(currPager);
		if (!frontBtn.isEnabled()) {
			frontBtn.setEnabled(true);
			frontBtn.setAlpha(1f);
		}
		// 保留记录到文本
		jsonOperater.newBlankPage(currPager - 1);
		if (bgList != null) {
			if (bgList.size() < 1) {
				bgList = null;
			}
			// 保存记录到修改背景
			jsonOperater.changeBackground(viewGroup.getBackgroundOrder());
		}
	}

	/** 设置页号文本 */
	public synchronized void setPageTV() {
		pageTV.setText((currPager + 1) + "/" + viewGroupList.size());
	}

	/** 设置录制时间文本 */
	public synchronized void setRecTimeTV() {
		try {
			long time = DateUtil.getTimeMillisLong()
					- jsonOperater.getStartTime() + jsonOperater.getSumTime();
			recTimeTV.setText(DateUtil.longToDate(time));
		} catch (Exception e) {
		}
	}

	/** 截屏（笔记） */
	private void getAndSaveCurrentImage(final View view, final int i) {
		Bitmap bmp = null;
		try {
			view.setDrawingCacheEnabled(true);
			bmp = Bitmap.createBitmap(view.getDrawingCache());
			view.setDrawingCacheEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 3.保存Bitmap
		try {
			FileOutputStream fos = null;
			fos = new FileOutputStream(jsonOperater.getVideoFullPath() + i
					+ Config.LOCAL_IMAGE_SUFFIX);
			if (null != fos && bmp != null) {
				bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bmp != null) {
				bmp.recycle();
			}
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DONE:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				if (worksBean == null) { // 根据录制的类型，跳转到对应类型的作品列表
					// Intent data = new Intent();
					// data.putExtra("isNoteRecorde", isDraft);
					AtyRecord.this.setResult(RESULT_OK);
				}
				// else if (isDraft) {
				// Constant.delLastVideoDraft = true; // TODO 讲义相关
				// Constant.delDraftBean = worksBean;
				// }
				// Intent intent = new Intent(AtyRecord.this, AtyPlay.class);
				// intent.putExtra("worksBean", globalInit.getVideoBean());
				// intent.putExtra("type", "PERSONAL");
				// startActivity(intent);
				ScreenManager.getInstance().popActivity(AtyRecord.this);
				break;
			case SAVEWRONG:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				Toast.makeText(getApplicationContext(), R.string.save_fail,
						Toast.LENGTH_SHORT).show();
				setResult(RESULT_CANCELED);
				ScreenManager.getInstance().popActivity(AtyRecord.this);
				break;
			case SAVEIMGSUCC: // 图片加载成功
				viewGroup.initImageView((Bitmap) msg.obj, JsonOperater
						.getInstance().getTempImgName(), JsonOperater
						.getInstance().getViewId());
				viewGroup.saveImageView();
				// 更改操作类型为选择操作
				viewGroup.setOperate(Config.OPER_SELECT);
				opttypeBtn.setImageResource(R.drawable.opt_hand);
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				break;
			case SAVE_MATERIAL_OK: // 图片加载成功
				addImgSuccess(msg);
				break;
			case SAVEIMGSUCC2: // 图片加载成功
				// newBlankPage();
				viewGroup
						.initImageView2(recordPager, (String) msg.obj,
								jsonOperater.getTempImgName(),
								jsonOperater.getViewId());
				viewGroup.saveImageView();
				// 更改操作类型为选择操作
				viewGroup.setOperate(Config.OPER_SELECT);
				opttypeBtn.setImageResource(R.drawable.opt_hand);
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
					progress = null;
				}
				break;
			case SAVEIMGSUCC3: // 图片加载成功
				if (selList.size() > 1 && i != 0) {
					newBlankPage();
				}
				String[] str = (String[]) msg.obj;
				String tempImgName = str[0];
				String savePath = str[1];
				int viewId = jsonOperater.getViewId();
				viewGroup.initImageView2(recordPager, savePath, tempImgName,
						viewId);
				viewGroup.saveImageView();
				// 更改操作类型为选择操作
				int j2 = i++;
				if (j2 >= selList.size() - 1) {
					viewGroup.setOperate(Config.OPER_SELECT);
					opttypeBtn.setImageResource(R.drawable.opt_hand);
					if (progress != null && progress.isShowing()) {
						// 隐藏删除框
						i = 0;
						imgAdapter.setShowDelete(false);
						progress.dismiss();
						progress = null;
					}
					return;
				}
				break;
			case SAVEIMGFAIL:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				showMessage(R.string.Image_load_fail);
				break;
			case ADD_BAD_IMG:
				if (progress != null) {
					progress.dismiss();
				}
				showMessage(R.string.Image_load_bad);
				break;
			case SAVEIMGFAIL1:
				if (progress != null && progress.isShowing()) {
					progress.dismiss();
				}
				break;
			// case DELETEBRUSH:
			// if (beans == null) {
			// beans = new ArrayList<BrushPathBean>();
			// }
			// deletebrush.setEnabled(true);
			// deletebrush.setVisibility(View.VISIBLE);
			// BrushPathBean bean = (BrushPathBean) msg.obj;
			// beans.add(bean);
			// break;
			// case DELETEBRUSH1:
			// deletebrush.setEnabled(false);
			// if (deletebrush.getVisibility() == View.VISIBLE) {
			// deletebrush.setVisibility(View.INVISIBLE);
			// beans.clear();
			// }
			// break;
			/** 数码笔与手写切换相关 */
			case DIGITAL_PEN:
				break;
			case NORMAL_PEN:
				break;
			case RESET_NOTE_WACOMVIEW:
				if (currPager < arrayLists.size()) {
					ArrayList<HistoryMemory> arrayList = arrayLists
							.get(currPager);
					RecordViewGroup group = viewGroupList.get(currPager);
					group.historyOperater.resetMemorier(wacomView, arrayList);
				}
				break;
			case SAVE_RECORD:
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							mediaCapture.stopRecord();
							jsonOperater.done(viewGroup.getWidth(),
									viewGroup.getHeight(), viewGroup,
									viewGroupList, isDraft);
							handler.sendEmptyMessage(DONE);
						} catch (Exception e) {
							e.printStackTrace();
							handler.sendEmptyMessage(SAVEWRONG);
						}
					}
				}).start();
				break;
			}
		}
	};

	/**
	 * 添加图片成功
	 * 
	 * @param msg
	 */
	private synchronized void addImgSuccess(Message msg) {
		boolean isSuccess = viewGroup.initImageView2(recordPager,
				(String) msg.obj, JsonOperater.getInstance().getTempImgName(),
				jsonOperater.getViewId());
		if (!isSuccess) {
			handler.sendEmptyMessage(ADD_BAD_IMG);
			return;
		}
		viewGroup.saveImageView();
		// 更改操作类型为选择操作
		opttypeBtn.setImageResource(R.drawable.opt_hand);

		int j = i++;
		int size = selCBPosList.size() - 1;
		if (j < size) {
			newBlankPage();
		}

		if (j >= size) {
			viewGroup.setOperate(Config.OPER_SELECT);
			// color = Color.argb(255, 255, 0, 0);
			// colorBtn.setImageResource(R.drawable.colorset_bt_clear);
			// colorMarkBtn.setBackgroundColor(AtyRecord.color);
			for (int i = 0; i < size; i++) {// 回退到第一页
				frontPage();
			}
			if (progress != null && progress.isShowing()) {
				progress.dismiss();
			}
		}
	}

	Handler recTimeHandler = new Handler();
	Runnable runnalbe = new Runnable() {
		@Override
		public void run() {
			setRecTimeTV();
			recTimeHandler.postDelayed(this, 1000);
		}
	};
	// private HintFinishDialog hintFinishDialog;
	public static boolean isDigital;

	@Override
	protected void onResume() {
		this.wakeLock.acquire();
		super.onResume();
	}

	@Override
	public void onPause() {
		this.wakeLock.release();
		if (recording) {
			pauseRecord();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		try {
			color = Color.argb(255, 11, 11, 11); // 笔触颜色
			stroke = 4; // 线宽
			arrowStroke = 4; // 箭头线宽
			shapeStroke = 4; // 图形线宽
			recorded = false;
			nextPage = false;
			isDraft = false;// 是否是录制草稿
			jsonOperater.destroy();
			if (fodderSet != null) {
				fodderSet.destroy();
			}
			if (colorSet != null) {
				colorSet.destroy();
			}
			recordPager = null;
			wacomView.destroy();
			if (viewGroupList != null) {
				viewGroupList.clear();
				viewGroupList = null;
			}
			viewGroup = null;
			mediaCapture = null;
			// Constant.isEdit = true; // TODO 是否是重新编辑笔迹
			isEdit = false;

			if (bmpMaps != null && bmpMaps.size() > 0) {
				for (Map.Entry<Integer, Bitmap> entry : bmpMaps.entrySet()) {
					entry.getValue().recycle();
				}
				bmpMaps.clear();
				System.gc();
			}
			if (isRegister) {
				eventBus.unregister(this);
				isRegister = false;
			}
		} catch (Exception e) {

		}
		super.onDestroy();
	}

	@Override
	public void onDrawAfter() {
		editNoteInfo();
	}

	/** 编辑笔记信息 */
	private void editNoteInfo() {
		if (worksBean != null) {
			isEdit = true;
			notePath = globalInit.getPersonalPath() + worksBean.getVideoPath()
					+ File.separator;
			copyPicture();// 拷贝图片
			resetWacomView(worksBean);
		}
	}

	/** 拷贝笔记图片 */
	private void copyPicture() {
		int pages = worksBean.getPages();
		for (int i = 0; i < pages; i++) {
			BitmapUtil.saveImageFile(notePath + i + ".pdt", JsonOperater
					.getInstance().getVideoFullPath() + i + ".pdt");
		}
	}

	/** 拿到json数据通过解析 生成HistoryMemory操作对象 */
	private void resetWacomView(WorksBean videoBean) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(globalInit.getPersonalPath()
					+ videoBean.getVideoPath() + File.separator
					+ videoBean.getVideoText());
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			String jsonText = new String(buffer);
			fis.close();
			imgPath = globalInit.getPersonalPath() + videoBean.getVideoPath()
					+ File.separator;
			final JSONObject videoJson = new JSONObject(jsonText);

			// 解析Json数据得到操作对象集合
			memoryList = resetViewGroup(videoJson);

			// 把memoryList集合放在不同的集合
			groupPager = noteWacomView.getGroupPager();
			bgList = noteWacomView.getpagerBgList();
			// TODO 0312 报异常，修改，吴勇
//			viewGroup.setBackground(bgList.get(0), true, true);// 设置第一页的背景
			bgList.remove(0);// 删除
			for (int i = 0; i <= groupPager; i++) {
				ArrayList<HistoryMemory> list = new ArrayList<HistoryMemory>();
				for (int j = 0; j < memoryList.size(); j++) {
					HistoryMemory memory = memoryList.get(j);
					if (memory.getViewGroupPager() == i) {
						list.add(memory);
					}
				}
				arrayLists.add(list);
			}

			// 新建页面
			for (int i = 0; i < arrayLists.size(); i++) {
				if (i > 0) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							handler.post(new Runnable() {
								@Override
								public void run() {
									newBlankPage();
								}
							});
						}
					}).start();
				}
				if (arrayLists.size() == 1) {// 在画布中画出生成的HistoryMemory对象
					handler.sendEmptyMessage(RESET_NOTE_WACOMVIEW);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取Json数据
	 * 
	 * @param videoJson
	 */
	private ArrayList<HistoryMemory> resetViewGroup(JSONObject videoJson) {
		try {
			noteWacomView = new NoteWacomViewGroup();
			commandArray = videoJson.getJSONArray("Commands");
			if (videoJson.has("screenWidth")) {
				videoWidth = videoJson.getInt("screenWidth");
			}
			if (videoJson.has("screenHeight")) {
				videoHeight = videoJson.getInt("screenHeight");
			}
			if (videoHeight > videoWidth) {
				int temp = videoWidth;
				videoWidth = videoHeight;
				videoHeight = temp;
			}
			JSONArray jsonArray = jsonOperater.delCommandArray(commandArray);
			calculateScale();
			for (int i = 0, j = jsonArray.length(); i < j; i++) {
				JSONObject currObj = jsonArray.getJSONObject(i);
				String type = currObj.getString("order");
				scale(currObj, type, false);
				play(type, currObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return noteWacomView.historyMemories;
	}

	private void play(String type, JSONObject currObj) {
		try {
			if (currObj.has("mid")) {
				jsonOperater.setViewId(currObj.getInt("mid"));// 设置viewId
			}
			if ("NewPage".equals(type)) { // 新建空白页
				noteWacomView.setViewGroupPager(1);
				noteWacomView.setPagers();
			} else if ("FlipPage".equals(type)) { // 移动一页
				JSONObject jsonObject = currObj.getJSONObject("detail");
				noteWacomView.setViewGroupPager(jsonObject.getInt("offset"));
			} else if ("Cbackground".equals(type)) { // 改变背景
				int cBackground = currObj.getJSONObject("detail").getInt(
						"cBackground");
				noteWacomView.setViewGroupBg(cBackground);
				// viewGroup.setBackground(cBackground, true, true);
			} else if ("Pencil".equals(type) || "Rubber".equals(type)) {
				JSONObject detailObj = currObj.getJSONObject("detail");
				mid = currObj.getInt("mid");
				noteColor = HexUtil.hexToColor(detailObj.getString("color"));
				if ("Pencil".equals(type)) {
					operate = Config.OPER_PEN;
				} else {
					operate = Config.OPER_RUBBER;
				}
				noteStroke = detailObj.getInt("thickness");
				JSONArray dataArray = detailObj.getJSONArray("data");
				for (int m = 0, n = dataArray.length(); m < n; m++) {
					JSONObject dataObj = dataArray.getJSONObject(m);
					if (m == 0) {
						noteWacomView.touch_start(
								(float) (dataObj.getDouble("x")),
								(float) (dataObj.getDouble("y")), noteColor,
								noteStroke, mid, operate);
					} else if (m == n - 1) {
						noteWacomView.touch_move(
								(float) (dataObj.getDouble("x")),
								(float) (dataObj.getDouble("y")), operate);
						noteWacomView.touch_up(operate);
					} else {
						noteWacomView.touch_move(
								(float) (dataObj.getDouble("x")),
								(float) (dataObj.getDouble("y")), operate);
					}
				}
			} else if ("Undo".equals(type)) { // 回退
				noteWacomView.undo();
			} else if ("Redo".equals(type)) { // 前进
				noteWacomView.redo();
			} else if ("AddText".equals(type)) { // 添加文本
				mid = currObj.getInt("mid");
				JSONObject detailObj = currObj.getJSONObject("detail");
				editTextColor = HexUtil.hexToColor(detailObj
						.getString("fontColor"));
				editTextSize = detailObj.getInt("fontSize");
				noteWacomView.initEditText(this, detailObj.getInt("x"),
						detailObj.getInt("y"), detailObj.getInt("width"),
						detailObj.getInt("height"), editTextColor,
						editTextSize, detailObj.getString("words"), mid);
			} else if ("ModifyText".equals(type)) { // 修改文本
				noteWacomView.modifyText(currObj);
			} else if ("DeleteText".equals(type)) { // 删除文本
				noteWacomView.delEditText(currObj.getInt("mid"));
			} else if ("MoveText".equals(type)) { // 移动文本
				noteWacomView.MoveText(currObj);
			} else if ("AddImage".equals(type)) { // 添加图片
				mid = currObj.getInt("mid");
				JSONObject detailObj = currObj.getJSONObject("detail");
				String source = detailObj.getString("source");
				String bitmapName = source.replace(
						Config.LOCAL_REAL_IMAGE_SUFFIX,
						Config.LOCAL_IMAGE_SUFFIX);
				String path = notePath + bitmapName;// 复制图片
				BitmapUtil.saveImageFile(path, jsonOperater.getVideoFullPath()
						+ bitmapName);
				noteWacomView.initImageView(this, bmpMaps.get(mid), bitmapName,
						mid);
			} else if ("DeleteImage".equals(type)) { // 删除图片
				noteWacomView.delImageView(currObj.getInt("mid"));
			} else if ("MoveImage".equals(type)) { // 移动图片
				noteWacomView.MoveImageView(currObj);
			} else if ("ScaleImage".equals(type)) { // 缩放图片
				noteWacomView.ScaleImageView(currObj);
			} else if ("RotateImage".equals(type)) {// 旋转图片
				noteWacomView.RotateImageView(currObj);
			} else { // 矩形,直线，箭头，椭圆
				int operType = 0;
				JSONObject detailObj = currObj.getJSONObject("detail");
				noteColor = HexUtil.hexToColor(detailObj.getString("color"));
				noteStroke = detailObj.getInt("thickness");
				mid = currObj.getInt("mid");
				JSONArray dataArray = detailObj.getJSONArray("data");
				for (int m = 0, n = dataArray.length(); m < n; m++) {
					JSONObject dataObj = dataArray.getJSONObject(m);
					if (m == 0) {
						noteWacomView.pathStart(
								(float) (dataObj.getDouble("x")),
								(float) (dataObj.getDouble("y")), noteColor,
								noteStroke, mid);
					} else if (m == n - 1) {
						noteWacomView.pathMove((float) dataObj.getDouble("x"),
								(float) dataObj.getDouble("y"));
						if ("Rect".equals(operType)) {
							operType = Config.OPER_RECT;
						} else if ("Ellipse".equals(operType)) {
							operType = Config.OPER_ELLIPSE;
						} else if ("Arrow".equals(operType)) {
							operType = Config.OPER_ARROW;
						} else if ("Line".equals(operType)) {
							operType = Config.OPER_LINE;
						}
						noteWacomView.pathUp((float) dataObj.getDouble("x"),
								(float) dataObj.getDouble("y"), operType);
					} else {
						noteWacomView.pathMove((float) dataObj.getDouble("x"),
								(float) dataObj.getDouble("y"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ViewPager的OnPageChangeListener start */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		viewGroup = viewGroupList.get(currPager);
		wacomView.setGroup(viewGroup);
		// TODO 存在绘制子view已经有富容器异常
		if (wacomView.getParent() == null) {
			viewGroup.addView(wacomView);
		}
		viewGroup.historyOperater.drawPath(wacomView);
		setBtEnablea();// 在翻页的时候判断回退和前进按钮是否可以点击
		setPageTV();
		// 设置操作类型为画线
		viewGroup.setOperate(Config.OPER_PEN);

		if (isEdit) {
			handler.sendEmptyMessage(RESET_NOTE_WACOMVIEW);
		}

	}

	/** ViewPager的OnPageChangeListener end */
	/**
	 * 暂停按钮的闪烁动画
	 * 
	 * @param view
	 * @param time
	 */
	private void startFlick(View view, int time) {
		if (null == view) {
			return;
		}
		Animation alphaAnimation = new AlphaAnimation(1f, 0.3f);
		alphaAnimation.setDuration(time);
		alphaAnimation.setInterpolator(new LinearInterpolator());
		alphaAnimation.setRepeatCount(Animation.INFINITE);
		alphaAnimation.setRepeatMode(Animation.REVERSE);
		view.startAnimation(alphaAnimation);
	}

	private void stopFlick(View view) {
		if (null == view) {
			return;
		}
		view.clearAnimation();
	}
}
