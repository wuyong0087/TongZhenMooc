package com.lib.play_rec.play;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lib.play_rec.ProgressDialogView;
import com.lib.play_rec.R;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.entity.ViewGroupBean;
import com.lib.play_rec.entity.WorksBean;
import com.lib.play_rec.utils.DateUtil;
import com.lib.play_rec.utils.HexUtil;
import com.lib.play_rec.utils.ScreenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AtyPlay extends VideoPlayerBaseAty implements OnClickListener,
		SeekBar.OnSeekBarChangeListener {

	private LinearLayout controlLayout;
	private Button playBtn;
	private SeekBar videoSeekBar;
	private TextView playCurrTimeTv;
	private TextView playSumTimeTv;
	private int backgroundOrder = 1;
	// 设置controlLayout的alpha值
	private int mAlpha = 0;
	private boolean isHide;

	private MediaPlayer mediaPlayer = null;
	private int sumTime; // 总时长
	private long currentTime; // 记录暂停当前时间
	private long startTime; // 视频开始时间
	private boolean handlerRunning = false;
	private boolean videoPause = false; // 视频播放是否处于暂停状态
	private boolean stopCurrPlayThread = false; // 是否停止当前播放线程
	private PlayThread playThread;

	/** Handler操作标示 */
	private final int NO_WEBURL = 1000;// weburl还没有得到（没有上传完成）
	private final int WRONG = 1001;
	private final int REFRESHALL = 1002; // 刷新所有未刷新的界面
	private final int playControlTouchUp = 1020;
	private final int playControlTouchDown = 1030;

	private ProgressDialogView progress = null;
	private Handler postHandler;
	private JSONArray commandArray;
	private int seekProgres = 0;
	private String weburl; // 请求路径
	private WorksBean bean;
	private boolean hasException = false;// 是否出现播放异常

	private boolean isPuase = true;// activity 是否是活动状态
	private int orderMid = -2; // 当前播放指令的Mid,-2表示该指令没有Mid。

	private int width;
	private int height;
	private float startX;
	private float startY;
	private float endX;
	private float endY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
		// Constant.comeback4play = true;
		// mController = UMServiceFactory.getUMSocialService("com.umeng.share");

		postHandler = new Handler();
		controlLayout = (LinearLayout) findViewById(R.id.video_control_layout);
		playBtn = (Button) findViewById(R.id.video_play_btn);
		playBtn.setOnClickListener(this);
		videoSeekBar = (SeekBar) findViewById(R.id.video_seekbar);
		playCurrTimeTv = (TextView) findViewById(R.id.video_currtime_tv);
		playSumTimeTv = (TextView) findViewById(R.id.video_sumtime_tv);

		viewGroup = (PlayViewGroup) findViewById(R.id.player_group);
		viewGroup.setPlayer(this);
		playView = (PlayView) findViewById(R.id.player_view);
		playView.setPlayer(this);
		groupBean = new ViewGroupBean(viewGroup);
		viewGroupList = new ArrayList<ViewGroupBean>();
		viewGroupList.add(groupBean);

		progress = new ProgressDialogView(this, R.string.video_loading);
		progress.show();
		loadThread.start();

		dialog = new AlertDialog.Builder(AtyPlay.this).create();// 创建分享dialog
		dialog.setCancelable(false);// 设置除对话框按钮外不可点击
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// 弹出此对话框，如果用户点击返回按钮，则退出播放器
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					ScreenManager.getInstance().popActivity(AtyPlay.this);
				}
				return false;
			}
		});
	}

	@Override
	protected void onResume() {
		isPuase = false;
		super.onResume();
	}

	@Override
	public void onPause() {
		isPuase = true;
		super.onPause();
		pausePlay();
	}

	/**
	 * 视频加载线程，用于解析Json数据，获取播放宽高，计算缩放比例，获取图片资源，解析音频
	 */
	Thread loadThread = new Thread() {
		@Override
		public void run() {
			bmpMap = new HashMap<Integer, String>();
			FileInputStream fis;
			try {
				// step1 传入作品对象，通过作品ID从数据库中查询出作品的相关数据
				bean = (WorksBean) getIntent()
						.getSerializableExtra("worksBean");
				String videoType = getIntent().getStringExtra("type");
				String rootPath;
				rootPath = GlobalInit.getInstance().getPersonalPath();
				String sql = "select VIDEO_NAME,VIDEO_DESC,SCREENSHOT_L,WEB_URL from T_VIDEO where VIDEO_ID='"
						+ bean.getId() + "'";
				// step2 通过获取到的作品数据，加载指令文件，并解析成Json数据对象
				fis = new FileInputStream(rootPath + bean.getVideoPath()
						+ File.separator + bean.getVideoText());
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				String jsonText = new String(buffer);
				fis.close();
				JSONObject videoJson = new JSONObject(jsonText);
				// 计算缩放比例------------------------
				if (videoJson.has("screenWidth")) {
					videoWidth = videoJson.getInt("screenWidth");
				}
				if (videoJson.has("screenHeight")) {
					videoHeight = videoJson.getInt("screenHeight");
				}
				if (videoHeight > videoWidth) { // 保证是横屏的操作
					int temp = videoWidth;
					videoWidth = videoHeight;
					videoHeight = temp;
				}
				calculateScale();
				commandArray = videoJson.getJSONArray("Commands");
				// 加载音频
				parseAudio(rootPath, bean.getVideoVoice());
				sumTime = Integer.valueOf(bean.getSumTime());
				currentTime = 0;
				// step3 解析指令，并对相应的指令数据进行缩放处理，原因在于，不同屏幕录制的视频的宽高是不一样的。
				String order;
				double time = -1, timeNext = -1, endTime = -1;
				JSONObject commandObj;
				boolean pause = false;
				imgPath = rootPath + bean.getVideoPath() + File.separator;
				if (commandArray.length() > 0) {
					commandObj = commandArray.getJSONObject(0);
					order = commandObj.getString("order");
					time = commandObj.getDouble("startTime") * thousand;
					endTime = commandObj.getDouble("endTime") * thousand;
					if (time == 0 || isPause(order, time, endTime)) {
						pause = true;
					} else {
						pause = false;
					}
					// 添加属性是否为暂停录制
					commandObj.put("pause", pause);
					scale(commandObj, order, true);
					for (int i = 1, j = commandArray.length(); i < j; i++) {
						commandObj = commandArray.getJSONObject(i);
						order = commandObj.getString("order");
						timeNext = commandObj.getDouble("startTime") * thousand;
						endTime = commandObj.getDouble("endTime") * thousand;
						if (time == timeNext
								|| isPause(order, timeNext, endTime)) {
							pause = true;
						} else {
							pause = false;
						}
						// 添加属性是否为暂停录制
						commandObj.put("pause", pause);
						scale(commandObj, order, true);
						time = timeNext;
					}
				}
				// setp4 调用handler的post方法，开始进行渲染播放
				postHandler.post(new Runnable() {
					@Override
					public void run() {
						playSumTimeTv.setText(DateUtil.longToDate(sumTime));
						videoSeekBar.setMax(sumTime);
						videoSeekBar.setOnSeekBarChangeListener(AtyPlay.this);
						// step4.1----------更改播放器大小-----------
						RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewGroup
								.getLayoutParams();
						params.width = videoWidth;
						params.height = videoHeight;
						viewGroup.setLayoutParams(params);
						playView.setLayoutParams(params);
						viewGroup.setBackground(backgroundOrder);
						// step4.2-----------启动播放线程------------
						videoPause = false;
						playBtn.setBackgroundResource(R.drawable.video_player_pause);
						playThread = new PlayThread();
						playThread.start();

						if (progress != null && progress.isShowing()) {
							progress.dismiss();
						}
					}
				});
			} catch (Exception e) {
				hasException = true;
				e.printStackTrace();
				postHandler.post(new Runnable() {
					@Override
					public void run() {
						showMessage(R.string.video_load_failed);
						if (progress != null && progress.isShowing()) {
							progress.dismiss();
						}
					}
				});
			}
		}

		/**
		 * 加载音频
		 * 
		 * @param rootPath
		 * @param voiceName
		 */
		private void parseAudio(String rootPath, String voiceName) {
			try {
				mediaPlayer = new MediaPlayer();
				// TODO 将测试的音频文件去掉
				String mediaSource = rootPath + bean.getVideoPath()
						+ File.separator + voiceName;
				try {
					mediaPlayer.setDataSource(mediaSource);
				} catch (Exception e) {
					AssetFileDescriptor afd = getAssets().openFd(
							"1444717489.mp3");
					mediaPlayer.setDataSource(afd.getFileDescriptor());
				}
				mediaPlayer.setLooping(false);
				mediaPlayer.prepare();
			} catch (Exception e) {
				hasException = true;
				postHandler.post(new Runnable() {
					@Override
					public void run() {
						showMessage(R.string.voice_load_failed);
						if (progress != null && progress.isShowing()) {
							progress.dismiss();
							progress = null;
						}
					}
				});
			}
		}
	};

	/** 视频播放线程 */
	class PlayThread extends Thread {
		@Override
		public void run() {
			JSONObject currObj;
			String order;
			boolean pause = false;
			double time;
			try {
				// 循环播放下面的操作
				for (int i = 0, j = commandArray.length(); i < j; i++) {
					if (stopCurrPlayThread) {
						return;
					}
					try {
						synchronized (this) {
							if (videoPause) {
								if (mediaPlayer.isPlaying()) {
									mediaPlayer.pause();
								}
								currentTime = System.currentTimeMillis();
								wait();
							}
						}
						currObj = commandArray.getJSONObject(i);
						order = currObj.getString("order");
						time = currObj.getDouble("startTime") * thousand;
						pause = currObj.getBoolean("pause");
						if (time <= seekProgres || pause) { // 如果指令开始时间小于等于进度条的当前进度，或者当前状态为暂停，则不进行界面刷新
							// 暂停时播放不刷新
							pauseRecord = true;
							play(order, time, currObj);
						} else {
							if (playStatus == 0) { // 尚未开始播放时，启动多媒体播放音频，并将播放器的状态切换成正在播放。
								mediaPlayer.start();
								startTime = DateUtil.getTimeMillisLong();
								playStatus = 1;
								postHandler.postDelayed(showPlayTime, 1000);
							}
							if (pauseRecord) {
								// 如果前面是暂停录制，则刷新再播放完当前操作
								handlerRunning = true;
								operateType = REFRESHALL;
								postHandler.post(playRunable);
								// 等待界面更新完成
								while (handlerRunning) {
									synchronized (this) {
										this.wait(10);
										continue;
									}
								}
							}
							pauseRecord = false;
							play(order, time, currObj);
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("播放指令出错");
					}
				}
				if (pauseRecord) {
					// 如果前面是暂停录制，则刷新界面
					handlerRunning = true;
					operateType = REFRESHALL;
					postHandler.post(playRunable);
				}
				// 如果只有一个操作，且该操作为暂停操作，则播放音频
				if (playStatus == 0) {
					mediaPlayer.start();
					startTime = DateUtil.getTimeMillisLong();
					playStatus = 1;
					postHandler.postDelayed(showPlayTime, 1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (handler != null) {
					handler.sendEmptyMessage(WRONG);
				}
			} finally {
				currObj = null;
			}
		}
	};

	/**
	 * 播放
	 * 
	 * @param type
	 * @param time
	 * @param currObj
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	private void play(String type, double time, JSONObject currObj)
			throws JSONException, InterruptedException {
		if (currObj.has("mid")) {
			orderMid = currObj.getInt("mid");
		}
		while ((time - (System.currentTimeMillis() - startTime + seekProgres)) > 0) {
			if (stopCurrPlayThread) {
				return;
			}
			synchronized (playThread) {
				if (videoPause) {
					playThread.wait();
				} else {
					playThread.wait(10);
				}
			}
		}
		if (stopCurrPlayThread) {
			return;
		}
		if (Config.ORDER_NEWPAGE.equals(type)) { // 新建空白页
			newPage();
		} else if (Config.ORDER_FLIPPAGE.equals(type)) { // 移动一页
			flipPage(currObj);
		} else if (Config.ORDER_BACKGROUND.equals(type)) { // 改变背景
			changeBG(currObj);
		} else if (Config.ORDER_PENCIL.equals(type) || "".equals(type)) { // 画笔和橡皮擦
			pen(currObj, type);
		} else if (Config.ORDER_RUBBER.equals(type)) {
			if (pauseRecord) {
				JSONArray ids = currObj.getJSONArray("ids");
				int length = ids.length();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < length; i++) {
					sb.append(ids.getString(i)).append(",");
				}
				playView.rubberDraw(sb.toString(), false);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_RUBBER;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_RECT.equals(type)) { // 矩形操作
			rect(currObj);
		} else if (Config.ORDER_CURSOR.equals(type)) { // 光标操作
			cursor(currObj);
		} else if (Config.ORDER_CLEARRECT.equals(type)) { // 删除操作
			cleanRect(currObj);
		} else if (Config.ORDER_ELLIPSE.equals(type)) { // 椭圆操作
			Ellipse(currObj);
		} else if (Config.ORDER_ARROW.equals(type)) {// 箭头操作
			Arrow(currObj);
		} else if (Config.ORDER_LINE.equals(type)) {// 箭头操作
			Line(currObj);
		} else if (Config.ORDER_ADD_TEXT.equals(type)) { // 添加文本
			addText(currObj);
		} else if (Config.ORDER_EDIT_TEXT.equals(type)) { // 修改文本
			editText(currObj);
		} else if (Config.ORDER_DELETE_TEXT.equals(type)) { // 删除文本
			deleteText(currObj);
		} else if (Config.ORDER_MOVE_TEXT.equals(type)) { // 移动文本
			moveText(currObj);
		} else if (Config.ORDER_ADD_IMAGE.equals(type)) { // 添加图片
			addImage(currObj);
		} else if (Config.ORDER_DELETE_IMAGE.equals(type)) { // 删除图片
			deleteImage(currObj);
		} else if (Config.ORDER_MOVE_IMAGE.equals(type)) { // 移动图片
			moveImage(currObj);
		} else if (Config.ORDER_SCALE_IMAGE.equals(type)) { // 缩放图片
			scaleImage(currObj);
		} else if (Config.ORDER_ROTATION_IMAGE.equals(type)) {// 旋转图片
			rotateImage(currObj);
		} else if (Config.ORDER_UNDO_ADDTEXT.equals(type)) {
			if (pauseRecord) {
				groupBean.undoTextAdd(!pauseRecord, orderMid);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_TEXT_UNADD;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_REDO_ADDTEXT.equals(type)) {
			if (pauseRecord) {
				groupBean.redoTextAdd(!pauseRecord, orderMid);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_TEXT_READD;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_UNDO_EDITTEXT.equals(type)) {
			if (pauseRecord) {
				modifyText(currObj);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_TEXT_UNEDIT;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_REDO_EDITTEXT.equals(type)) {
			if (pauseRecord) {
				modifyText(currObj);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_TEXT_REEDIT;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_UNDO_DELETE_TEXT.equals(type)) {
			if (pauseRecord) {
				groupBean.undoTextDelete(!pauseRecord, orderMid);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_TEXT_UNDELETE;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_REDO_DELETE_TEXT.equals(type)) {
			if (pauseRecord) {
				groupBean.redoTextDelete(!pauseRecord, orderMid);
				operateType = Config.OPER_TEXT_REDELETE;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_UNDO_MOVETEXT.equals(type)
				|| Config.ORDER_REDO_MOVETEXT.equals(type)) {
			if (pauseRecord) {
				JSONObject detail = currObj.getJSONArray("detail")
						.getJSONObject(0);
				((PlayEditText) groupBean.viewMap.get(orderMid))
						.setLayoutByMid(detail.getInt("x"), detail.getInt("y"),
								!pauseRecord);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_TEXT_REMOVE;
				commandObj = currObj;
				postHandler.post(playRunable);
			}

			/* 图片相关 */
		} else if (Config.ORDER_UNDO_ADDIMAGE.equals(type)) {
			if (pauseRecord) {
				groupBean.undoImgAdd(!pauseRecord, orderMid);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_IMG_UNADD;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_REDO_ADDIMAGE.equals(type)) {
			if (pauseRecord) {
				groupBean.redoImgAdd(!pauseRecord, orderMid);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_IMG_READD;
				commandObj = currObj;
				postHandler.post(playRunable);
			}

		} else if (Config.ORDER_UNDO_MOVEIMAGE.equals(type)
				|| Config.ORDER_REDO_MOVEIMAGE.equals(type)) {
			if (pauseRecord) {
				PlayImageView img = (PlayImageView) groupBean.viewMap
						.get(orderMid);
				img.startMove();
				JSONObject data = currObj.getJSONArray("detail").getJSONObject(
						0);
				img.setLayoutByMid(data.getInt("x"), data.getInt("y"),
						!pauseRecord);
			} else {
				handlerRunning = true;
				if (Config.ORDER_UNDO_MOVEIMAGE.equals(type)) {
					operateType = Config.OPER_IMG_UNMOVE;
				} else {
					operateType = Config.OPER_IMG_REMOVE;
				}
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_UNDO_SCALEIMAGE.equals(type)
				|| Config.ORDER_REDO_SCALEIMAGE.equals(type)) {
			if (pauseRecord) {
				PlayImageView img = (PlayImageView) groupBean.viewMap
						.get(orderMid);
				img.startScaleImage();
				JSONObject data = currObj.getJSONArray("detail").getJSONObject(
						0);
				img.scaleImage(data.getDouble("scale"), !pauseRecord);
			} else {
				handlerRunning = true;
				if (Config.ORDER_UNDO_SCALEIMAGE.equals(type)) {
					operateType = Config.OPER_IMG_UNSCALE;
				} else {
					operateType = Config.OPER_IMG_RESCALE;
				}
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_UNDO_ROTATION_IMAGE.equals(type)
				|| Config.ORDER_REDO_ROTATION_IMAGE.equals(type)) {
			if (pauseRecord) {
				PlayImageView img = (PlayImageView) groupBean.viewMap
						.get(orderMid);
				img.startRotateImage();
				JSONObject data = currObj.getJSONArray("detail").getJSONObject(
						0);
				img.rotateImage((float) data.getDouble("angle"), !pauseRecord);
			} else {
				handlerRunning = true;
				if (Config.ORDER_UNDO_ROTATION_IMAGE.equals(type)) {
					operateType = Config.OPER_IMG_UNROTATE;
				} else {
					operateType = Config.OPER_IMG_REROTATE;
				}
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_UNDO_DELETE_IMAGE.equals(type)) {
			if (pauseRecord) {
				groupBean.undoImgDelete(!pauseRecord, orderMid);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_IMG_UNDELETE;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_REDO_DELETE_IMAGE.equals(type)) {
			if (pauseRecord) {
				groupBean.redoImgAdd(!pauseRecord, orderMid);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_IMG_REDELETE;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		}
		/* 画笔相关 */
		else if (Config.ORDER_UNDO_PENCIL.equals(type)) {
			if (pauseRecord) {
				groupBean.undoPen_Rubber(playView, !pauseRecord, orderMid);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_UNPEN;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_REDO_PENCIL.equals(type)) {
			if (pauseRecord) {
				groupBean.redoPen_Rubber(playView, !pauseRecord, orderMid);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_REPEN;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_REDO_RUBBER.equals(type)) {
			if (pauseRecord) {
				rubber(currObj, false);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_RERUBBER;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} else if (Config.ORDER_UNDO_RUBBER.equals(type)) {
			if (pauseRecord) {
				rubber(currObj, true);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_UNRUBBER;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		}
		// 等待界面更新完成
		while (handlerRunning) {
			synchronized (playThread) {
				playThread.wait(10);
				continue;
			}
		}
	}

	private void rotateImage(JSONObject currObj) throws JSONException,
			InterruptedException {
		double time;
		JSONArray dataArray = currObj.getJSONArray("detail");
		viewGroup.startRotateImage(currObj.getInt("mid"));
		if (pauseRecord) {
			double angle = 1.0;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				angle *= dataArray.getJSONObject(m).getDouble("angle");
			}
			viewGroup.rotateImage((float) angle, !pauseRecord);
		} else {
			operateType = Config.OPER_IMG_ROTATE;
			JSONObject dataObj;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				dataObj = dataArray.getJSONObject(m);
				floatRotation = dataObj.getDouble("angle");
				time = dataObj.getDouble("t") * thousand;
				while ((time - (System.currentTimeMillis() - startTime + seekProgres)) > 0) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						if (videoPause) {
							playThread.wait();
						} else {
							playThread.wait(5);
						}
					}
				}
				handlerRunning = true;
				postHandler.post(playRunable);
				// 等待界面更新完成
				while (handlerRunning) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						playThread.wait(5);
						continue;
					}
				}
			}
		}
	}

	private void scaleImage(JSONObject currObj) throws JSONException,
			InterruptedException {
		double time;
		JSONArray dataArray = currObj.getJSONArray("detail");
		viewGroup.startScaleImage(currObj.getInt("mid"));
		if (pauseRecord) {
			double scale = 1.0;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				scale *= dataArray.getJSONObject(m).getDouble("scale");
			}
			viewGroup.scaleImage((float) scale, !pauseRecord);
		} else {
			operateType = Config.OPER_IMG_SCALE;
			JSONObject dataObj;
			double saveTime = 0;
			double saveScale = 1;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				dataObj = dataArray.getJSONObject(m);
				floatVal = dataObj.getDouble("scale");
				time = dataObj.getDouble("t") * thousand;
				if (m != n - 1 && time - saveTime < 100) {
					saveScale *= floatVal;
					continue;
				} else {
					floatVal *= saveScale;
					while ((time - (System.currentTimeMillis() - startTime + seekProgres)) > 0) {
						if (stopCurrPlayThread) {
							return;
						}
						synchronized (playThread) {
							if (videoPause) {
								playThread.wait();
							} else {
								playThread.wait(5);
							}
						}
					}
					handlerRunning = true;
					postHandler.post(playRunable);
					// 等待界面更新完成
					while (handlerRunning) {
						if (stopCurrPlayThread) {
							return;
						}
						synchronized (playThread) {
							playThread.wait(5);
							continue;
						}
					}
				}
				saveScale = 1;
				saveTime = time;
			}
		}
		viewGroup.endScaleImage();
	}

	private void moveImage(JSONObject currObj) throws JSONException,
			InterruptedException {
		double time;
		JSONArray dataArray = currObj.getJSONArray("detail");
		viewGroup.startMoveImage(currObj.getInt("mid"));
		if (pauseRecord) {
			// 记录第一个位置和最后一个位置
			JSONObject dataObj = dataArray.getJSONObject(0);
			viewGroup.moveImage((float) (dataObj.getDouble("x")),
					(float) (dataObj.getDouble("y")), !pauseRecord);
			dataObj = dataArray.getJSONObject(dataArray.length() - 1);
			viewGroup.moveImage((float) (dataObj.getDouble("x")),
					(float) (dataObj.getDouble("y")), !pauseRecord);
		} else {
			operateType = Config.OPER_IMG_MOVE;
			double saveTime = 0;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				commandObj = dataArray.getJSONObject(m);
				time = commandObj.getDouble("t") * thousand;
				if (m != n - 1 && time - saveTime < 100) {
					continue;
				}
				while ((time - (System.currentTimeMillis() - startTime + seekProgres)) > 0) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						if (videoPause) {
							playThread.wait();
						} else {
							playThread.wait(10);
						}
					}
				}
				handlerRunning = true;
				postHandler.post(playRunable);
				// 等待界面更新完成
				while (handlerRunning) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						playThread.wait(5);
						continue;
					}
				}
				saveTime = time;
			}
		}
		viewGroup.endMoveImage();
	}

	private void deleteImage(JSONObject currObj) throws JSONException {
		commVal = currObj.getInt("mid");
		if (pauseRecord) {
			viewGroup.delImageView(
					(PlayImageView) (groupBean.viewMap.get(commVal)),
					!pauseRecord, true);
		} else {
			handlerRunning = true;
			operateType = Config.OPER_IMG_DELETE;
			postHandler.post(playRunable);
		}
	}

	private void addImage(JSONObject currObj) throws JSONException {
		commVal = currObj.getInt("mid");
		commandObj = currObj.getJSONObject("detail");
		width = commandObj.getInt("width");
		height = commandObj.getInt("height");
		if (pauseRecord) {
			// viewGroup.initImageView(bmpMap.get(commVal), commVal,
			// commandObj.getInt("x"), commandObj.getInt("y"),
			// !pauseRecord);
			viewGroup.initImageView2(viewGroup, this, bmpMap.get(commVal),
					commVal, commandObj.getInt("x"), commandObj.getInt("y"),
					width, height, !pauseRecord);
		} else {
			handlerRunning = true;
			operateType = Config.OPER_IMG_ADD;
			postHandler.post(playRunable);
		}
	}

	private void moveText(JSONObject currObj) throws JSONException,
			InterruptedException {
		double time;
		JSONArray dataArray = currObj.getJSONArray("detail");
		viewGroup.startMoveText(currObj.getInt("mid"));
		if (pauseRecord) {
			// 记录第一个位置和最后一个位置
			JSONObject dataObj = dataArray.getJSONObject(0);
			viewGroup.moveText(dataObj.getInt("x"), dataObj.getInt("y"),
					!pauseRecord);
			dataObj = dataArray.getJSONObject(dataArray.length() - 1);
			viewGroup.moveText(dataObj.getInt("x"), dataObj.getInt("y"),
					!pauseRecord);
		} else {
			operateType = Config.OPER_TEXT_MOVE;
			double saveTime = 0;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				commandObj = dataArray.getJSONObject(m);
				time = commandObj.getDouble("t") * thousand;
				if (m != n - 1 && time - saveTime < 100) {
					continue;
				}
				while ((time - (System.currentTimeMillis() - startTime + seekProgres)) > 0) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						if (videoPause) {
							playThread.wait();
						} else {
							playThread.wait(10);
						}
					}
				}
				handlerRunning = true;
				postHandler.post(playRunable);
				// 等待界面更新完成
				while (handlerRunning) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						playThread.wait(5);
						continue;
					}
				}
				saveTime = time;
			}
		}
		viewGroup.endMoveText();
	}

	private void deleteText(JSONObject currObj) throws JSONException {
		commVal = currObj.getInt("mid");
		if (pauseRecord) {
			viewGroup.delEditText(
					(PlayEditText) groupBean.viewMap.get(commVal),
					!pauseRecord, true);
		} else {
			handlerRunning = true;
			operateType = Config.OPER_TEXT_DELETE;
			postHandler.post(playRunable);
		}
	}

	private void editText(JSONObject currObj) throws JSONException {
		if (pauseRecord) {
			modifyText(currObj);
		} else {
			handlerRunning = true;
			operateType = Config.OPER_TEXT_EDIT;
			commandObj = currObj;
			postHandler.post(playRunable);
		}
	}

	private void addText(JSONObject currObj) throws JSONException {
		JSONObject detailObj = currObj.getJSONObject("detail");
		editTextColor = HexUtil.hexToColor(detailObj.getString("fontColor"));
		editTextSize = detailObj.getInt("fontSize");
		commVal = currObj.getInt("mid");
		commandObj = detailObj;
		handlerRunning = true;
		operateType = Config.OPER_TEXT_ADD;
		postHandler.post(playRunable);
		// }
	}

	/**
	 * 直线
	 * 
	 * @param currObj
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	private void Line(JSONObject currObj) throws JSONException,
			InterruptedException {
		double time;
		JSONObject detailObj = currObj.getJSONObject("detail");
		color = HexUtil.hexToColor(detailObj.getString("color"));
		stroke = detailObj.getInt("thickness");
		JSONArray dataArray = detailObj.getJSONArray("data");
		if (pauseRecord) {
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);

				if (m == 0) {
					startX = (float) dataObj.getDouble("x");
					startY = (float) dataObj.getDouble("y");
				} else if (m == n - 1) {
					endX = (float) dataObj.getDouble("x");
					endY = (float) dataObj.getDouble("y");
				}
			}
			playView.drawLine(startX, startY, endX, endY, color, stroke,
					orderMid);
		} else {
			operateType = Config.OPER_LINE;
			JSONObject dataObj;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				dataObj = dataArray.getJSONObject(m);
				time = dataObj.getDouble("t") * thousand;
				while ((time - (System.currentTimeMillis() - startTime + seekProgres)) > 0) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						if (videoPause) {
							playThread.wait();
						} else {
							playThread.wait(10);
						}
					}
				}
				if (m == 0) {
					handlerRunning = true;
					commandObj = dataObj;
					commVal = 13;
				} else if (m == n - 1) {
					handlerRunning = true;
					commVal = 14;
					lineCommandList.add(dataObj);
				} else {
					handlerRunning = false;
					commVal = 15;
					lineCommandList.add(dataObj);
				}
				postHandler.post(playRunable);
				// 等待界面更新完成
				while (handlerRunning) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						playThread.wait(5);
						continue;
					}
				}
			}
		}
	}

	/**
	 * 箭头操作
	 * 
	 * @param currObj
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	private void Arrow(JSONObject currObj) throws JSONException,
			InterruptedException {
		double time;
		JSONObject detailObj = currObj.getJSONObject("detail");
		color = HexUtil.hexToColor(detailObj.getString("color"));
		stroke = detailObj.getInt("thickness");
		JSONArray dataArray = detailObj.getJSONArray("data");
		if (pauseRecord) {
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				if (m == 0) {
					startX = (float) dataObj.getDouble("x");
					startY = (float) dataObj.getDouble("y");
				} else if (m == n - 1) {
					endX = (float) dataObj.getDouble("x");
					endY = (float) dataObj.getDouble("y");
				}
			}
			playView.drawArrow(startX, startY, endX, endY, color, stroke,
					orderMid);
		} else {
			operateType = Config.OPER_ARROW;
			JSONObject dataObj;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				dataObj = dataArray.getJSONObject(m);
				time = dataObj.getDouble("t") * thousand;
				while ((time - (System.currentTimeMillis() - startTime + seekProgres)) > 0) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						if (videoPause) {
							playThread.wait();
						} else {
							playThread.wait(10);
						}
					}
				}
				if (m == 0) {
					handlerRunning = true;
					commandObj = dataObj;
					commVal = 7;
				} else if (m == n - 1) {
					handlerRunning = true;
					commVal = 8;
					arrowCommandList.add(dataObj);
				} else {
					handlerRunning = false;
					commVal = 9;
					arrowCommandList.add(dataObj);
				}
				postHandler.post(playRunable);
				// 等待界面更新完成
				while (handlerRunning) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						playThread.wait(5);
						continue;
					}
				}
			}
		}
	}

	/**
	 * 圆形
	 * 
	 * @param currObj
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	private void Ellipse(JSONObject currObj) throws JSONException,
			InterruptedException {
		double time;
		JSONObject detailObj = currObj.getJSONObject("detail");
		color = HexUtil.hexToColor(detailObj.getString("color"));
		stroke = detailObj.getInt("thickness");
		JSONArray dataArray = detailObj.getJSONArray("data");
		if (pauseRecord) {
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);

				if (m == 0) {
					startX = (float) dataObj.getDouble("x");
					startY = (float) dataObj.getDouble("y");
				} else if (m == n - 1) {
					endX = (float) dataObj.getDouble("x");
					endY = (float) dataObj.getDouble("y");
				}
			}
			playView.drawOval(startX, startY, endX, endY, color, stroke,
					orderMid);
		} else {
			operateType = Config.OPER_ELLIPSE;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				time = dataObj.getDouble("t") * thousand;
				while ((time - (System.currentTimeMillis() - startTime + seekProgres)) > 0) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						if (videoPause) {
							playThread.wait();
						} else {
							playThread.wait(10);
						}
					}
				}
				if (m == 0) {
					handlerRunning = true;
					commandObj = dataObj;
					commVal = 1;
				} else if (m == n - 1) {
					handlerRunning = true;
					commVal = 2;
					ellipseCommandList.add(dataObj);
				} else {
					handlerRunning = false;
					commVal = 3;
					ellipseCommandList.add(dataObj);
				}
				postHandler.post(playRunable);
				// 等待界面更新完成
				while (handlerRunning) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						playThread.wait(5);
						continue;
					}
				}
			}
		}
	}

	/**
	 * 矩形虚线索套
	 * 
	 * @param currObj
	 * @throws JSONException
	 */
	private void cleanRect(JSONObject currObj) throws JSONException {
		midList = new ArrayList<String>();
		String str = currObj.getString("mids");
		String[] split = str.split(",");
		for (int i = 0; i < split.length; i++) {
			if (!midList.contains(split[i])) {
				midList.add(split[i]);
			}
		}
		if (pauseRecord) {
			playView.isContainMemory(midList);
			midList.clear();
		} else {
			handlerRunning = true;
			operateType = Config.OPER_CLEAN_RECT;
			postHandler.post(playRunable);
		}
	}

	/**
	 * 光标
	 * 
	 * @param currObj
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	private void cursor(JSONObject currObj) throws JSONException,
			InterruptedException {
		double time;
		JSONArray dataArray = currObj.getJSONArray("center");
		if (pauseRecord) {
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				if (m == 0) {
					playView.cursor_start((float) (dataObj.getDouble("x")),
							(float) (dataObj.getDouble("y")), !pauseRecord,
							orderMid);
				} else if (m == n - 1) {
					playView.cursor_move((float) dataObj.getDouble("x"),
							(float) dataObj.getDouble("y"), !pauseRecord);
					playView.cursor_up((float) dataObj.getDouble("x"),
							(float) dataObj.getDouble("y"), !pauseRecord);
				} else {
					playView.cursor_move((float) dataObj.getDouble("x"),
							(float) dataObj.getDouble("y"), !pauseRecord);
				}
			}
		} else {
			operateType = Config.OPER_CURSOR;
			JSONObject dataObj;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				dataObj = dataArray.getJSONObject(m);
				time = dataObj.getDouble("t") * thousand;
				while ((time - (System.currentTimeMillis() - startTime + seekProgres)) > 0) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						if (videoPause) {
							playThread.wait();
						} else {
							playThread.wait(10);
						}
					}
				}
				if (m == 0) {
					handlerRunning = true;
					commandObj = dataObj;
					commVal = 16;
				} else if (m == n - 1) {
					handlerRunning = true;
					commVal = 17;
					cursorCommandList.add(dataObj);
				} else {
					handlerRunning = false;
					commVal = 18;
					cursorCommandList.add(dataObj);
				}
				postHandler.post(playRunable);
				// 等待界面更新完成
				while (handlerRunning) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						playThread.wait(5);
						continue;
					}
				}
			}
		}
	}

	/**
	 * 矩形
	 * 
	 * @param currObj
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	private void rect(JSONObject currObj) throws JSONException,
			InterruptedException {
		double time;
		JSONObject detailObj = currObj.getJSONObject("detail");
		color = HexUtil.hexToColor(detailObj.getString("color"));
		stroke = detailObj.getInt("thickness");
		JSONArray dataArray = detailObj.getJSONArray("data");
		if (pauseRecord) {
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				if (m == 0) {
					startX = (float) dataObj.getDouble("x");
					startY = (float) dataObj.getDouble("y");
				} else if (m == n - 1) {
					endX = (float) dataObj.getDouble("x");
					endY = (float) dataObj.getDouble("y");
				}
			}
			playView.drawRect(startX, startY, endX, endY, color, stroke,
					orderMid);
		} else {
			operateType = Config.OPER_RECT;
			JSONObject dataObj;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				dataObj = dataArray.getJSONObject(m);
				time = dataObj.getDouble("t") * thousand;
				while ((time - (System.currentTimeMillis() - startTime + seekProgres)) > 0) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						if (videoPause) {
							playThread.wait();
						} else {
							playThread.wait(10);
						}
					}
				}
				if (m == 0) {
					handlerRunning = true;
					commandObj = dataObj;
					commVal = 10;
				} else if (m == n - 1) {
					handlerRunning = true;
					commVal = 11;
					rectangleCommandList.add(dataObj);
				} else {
					handlerRunning = false;
					commVal = 12;
					rectangleCommandList.add(dataObj);
				}
				postHandler.post(playRunable);
				// 等待界面更新完成
				while (handlerRunning) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						playThread.wait(5);
						continue;
					}
				}
			}
		}
	}

	/**
	 * 画笔，橡皮擦
	 * 
	 * @param currObj
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	private void pen(JSONObject currObj, String type) throws JSONException,
			InterruptedException {
		double time;
		color = HexUtil.hexToColor(currObj.getString("color"));
		stroke = currObj.getInt("thickness");
		JSONArray dataArray = currObj.getJSONArray("data");
		operate = Config.OPER_PEN;
		if (pauseRecord) {
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				JSONObject dataObj = dataArray.getJSONObject(m);
				if (m == 0) {
					playView.touch_start((float) (dataObj.getDouble("x")),
							(float) (dataObj.getDouble("y")), color, stroke,
							!pauseRecord, orderMid);
				} else if (m == n - 1) {
					playView.touch_move((float) (dataObj.getDouble("x")),
							(float) (dataObj.getDouble("y")), !pauseRecord);
					playView.touch_up(!pauseRecord);
				} else {
					playView.touch_move((float) (dataObj.getDouble("x")),
							(float) (dataObj.getDouble("y")), !pauseRecord);
				}
			}
		} else {
			operateType = operate;
			JSONObject dataObj;
			for (int m = 0, n = dataArray.length(); m < n; m++) {
				dataObj = dataArray.getJSONObject(m);
				time = dataObj.getDouble("t") * thousand;
				while ((time - (System.currentTimeMillis() - startTime + seekProgres)) > 0) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						if (videoPause) {
							playThread.wait();
						} else {
							playThread.wait(10);
						}
					}
				}
				if (m == 0) {
					handlerRunning = true;
					commandObj = dataObj;
					commVal = 1;
				} else if (m == n - 1) {
					handlerRunning = true;
					commVal = 2;
					pencilCommandList.add(dataObj);
				} else {
					handlerRunning = false;
					commVal = 3;
					pencilCommandList.add(dataObj);
				}
				postHandler.post(playRunable);
				// 等待界面更新完成
				while (handlerRunning) {
					if (stopCurrPlayThread) {
						return;
					}
					synchronized (playThread) {
						playThread.wait(5);
						continue;
					}
				}
			}
		}
	}

	private void rubber(JSONObject currObj, boolean isUndo) {
		try {
			if (pauseRecord) {
				JSONArray ids = currObj.getJSONArray("ids");
				int length = ids.length();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < length; i++) {
					sb.append(ids.getString(i)).append(",");
				}
				playView.rubberDraw(sb.toString(), isUndo);
			} else {
				handlerRunning = true;
				operateType = Config.OPER_RUBBER;
				commandObj = currObj;
				postHandler.post(playRunable);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改背景
	 * 
	 * @param currObj
	 * @throws JSONException
	 */
	private void changeBG(JSONObject currObj) throws JSONException {
		if (pauseRecord && seekProgres == 0) {
			// groupBean.backgroundOrder = currObj.getJSONObject("detail")
			// .getInt("cBackground");
			// backgroundOrder = groupBean.backgroundOrder;
			// } else {
			handlerRunning = true;
			operateType = Config.OPER_CHANGE_BG;
			backgroundOrder = currObj.getJSONObject("detail").getInt(
					"cBackground");
			postHandler.post(playRunable);
		}
	}

	/**
	 * 翻页操作
	 * 
	 * @param currObj
	 * @throws JSONException
	 */
	private void flipPage(JSONObject currObj) throws JSONException {
		if (pauseRecord) {
			int action = currObj.getInt("action");
			if (currPager == 0 && action == -1) {
			} else {
				currPager = currPager + action;
			}
			groupBean = viewGroupList.get(currPager);
		} else {
			handlerRunning = true;
			commVal = currObj.getInt("action");
			operateType = Config.OPER_FLIPPAGE;
			postHandler.post(playRunable);
		}
	}

	/**
	 * 新建一页
	 */
	private void newPage() {
		// backgroundOrder = groupBean.backgroundOrder;
		if (pauseRecord) {
			groupBean = new ViewGroupBean(viewGroup);
			currPager++;
			viewGroupList.add(currPager, groupBean);
			// groupBean.backgroundOrder = backgroundOrder;
		} else {
			handlerRunning = true;
			operateType = Config.OPER_NEWPAGE;
			postHandler.post(playRunable);
		}
	}

	JSONObject commandObj;
	int commVal;
	double floatVal;
	double floatRotation;
	int operateType;
	List<JSONObject> pencilCommandList = new ArrayList<JSONObject>();// Pencil
	List<JSONObject> arrowCommandList = new ArrayList<JSONObject>();// Arrow
	List<JSONObject> ellipseCommandList = new ArrayList<JSONObject>();// Ellipse
	List<JSONObject> rectangleCommandList = new ArrayList<JSONObject>();// Rectangle
	List<JSONObject> lineCommandList = new ArrayList<JSONObject>();// Line
	List<JSONObject> cursorCommandList = new ArrayList<JSONObject>();// Cursor

	public int getOrderType() {
		return operate = operateType;
	}

	Runnable playRunable = new Runnable() {
		@Override
		public void run() {
			try {
				switch (operateType) {
				case Config.OPER_NEWPAGE: // 新建空白页
					// 清空画笔对象
					playView.clearCanvas();
					// 清除界面控件
					viewGroup.removeAllViews();
					groupBean = new ViewGroupBean(viewGroup);
					currPager++;
					viewGroupList.add(currPager, groupBean);
					viewGroup.addView(playView);
					viewGroup.setBackground(backgroundOrder);
					break;
				case Config.OPER_FLIPPAGE: // 切换页面
					// 清空画笔对象
					playView.clearCanvas();
					// 清除界面控件
					viewGroup.removeAllViews();
					if (currPager == 0 && commVal == -1) {
					} else {
						currPager = currPager + commVal;
					}
					groupBean = viewGroupList.get(currPager);
					// 把当前界面上的控件渲染出来
					viewGroup.setBackground(backgroundOrder);
					List<View> list = groupBean.viewOrderList;
					for (int i = 0; i < list.size(); i++) {
						View view = list.get(i);
						view.setVisibility(View.VISIBLE);
						if (view instanceof PlayEditText) {
							PlayEditText et = (PlayEditText) view;
							et.setText(et.getTextStr());
							et.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									et.getSize());
							et.setTextColor(et.getColor());
							et.setViewMaxWidth();
							// et.setLayout();
						} else if (view instanceof PlayImageView) {
							// WacomImageViewPlayer img = (WacomImageViewPlayer)
							// view;
							// img.setLayout();
						}
						viewGroup.addView(view);
					}
					viewGroup.addView(playView);
					groupBean.drawPath(playView);
					break;
				case Config.OPER_CHANGE_BG: // 更换背景
					viewGroup.setBackground(backgroundOrder);
					break;
				case Config.OPER_RUBBER:
					JSONArray ids = commandObj.getJSONArray("ids");
					int length = ids.length();
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < length; i++) {
						sb.append(ids.getString(i)).append(",");
					}
					playView.rubberDraw(sb.toString(), false);
					break;
				case Config.OPER_PEN: // 画线
					if (commVal == 1) {
						playView.touch_start(
								(float) (commandObj.getDouble("x")),
								(float) (commandObj.getDouble("y")), color,
								stroke, !pauseRecord, orderMid);
					} else if (commVal == 2) {
						while (pencilCommandList.size() > 0) {
							commandObj = pencilCommandList.remove(0);
							if (commandObj != null) {
								playView.touch_move(
										(float) (commandObj.getDouble("x")),
										(float) (commandObj.getDouble("y")),
										!pauseRecord);
							}
						}
						playView.touch_up(!pauseRecord);
						pencilCommandList.clear();
					} else {
						while (pencilCommandList.size() > 0) {
							commandObj = pencilCommandList.remove(0);
							if (commandObj != null) {
								playView.touch_move(
										(float) (commandObj.getDouble("x")),
										(float) (commandObj.getDouble("y")),
										!pauseRecord);
							}
						}
					}
					break;
				case Config.OPER_ELLIPSE:
					if (commVal == 1) {
						playView.oval_start(
								(float) (commandObj.getDouble("x")),
								(float) (commandObj.getDouble("y")), color,
								stroke, !pauseRecord, orderMid);
					} else if (commVal == 2) {
						while (ellipseCommandList.size() > 0) {
							commandObj = ellipseCommandList.remove(0);
							playView.oval_move(
									(float) (commandObj.getDouble("x")),
									(float) (commandObj.getDouble("y")),
									!pauseRecord);
						}
						playView.oval_up((float) (commandObj.getDouble("x")),
								(float) (commandObj.getDouble("y")),
								!pauseRecord);
						ellipseCommandList.clear();
					} else {
						while (ellipseCommandList.size() > 0) {
							commandObj = ellipseCommandList.remove(0);
							playView.oval_move(
									(float) (commandObj.getDouble("x")),
									(float) (commandObj.getDouble("y")),
									!pauseRecord);
						}
					}
					break;
				case Config.OPER_ARROW:
					if (commVal == 7) {
						playView.arrow_start(
								(float) (commandObj.getDouble("x")),
								(float) (commandObj.getDouble("y")), color,
								stroke, !pauseRecord, orderMid);
					} else if (commVal == 8) {
						while (arrowCommandList.size() > 0) {
							commandObj = arrowCommandList.remove(0);
							playView.arrow_move(
									(float) (commandObj.getDouble("x")),
									(float) (commandObj.getDouble("y")),
									!pauseRecord);
						}
						playView.arrow_up((float) (commandObj.getDouble("x")),
								(float) (commandObj.getDouble("y")),
								!pauseRecord);
						arrowCommandList.clear();
					} else {
						while (arrowCommandList.size() > 0) {
							commandObj = arrowCommandList.remove(0);
							playView.arrow_move(
									(float) (commandObj.getDouble("x")),
									(float) (commandObj.getDouble("y")),
									!pauseRecord);
						}
					}
					break;
				case Config.OPER_RECT:
					if (commVal == 10) {
						playView.rect_start(
								(float) (commandObj.getDouble("x")),
								(float) (commandObj.getDouble("y")), color,
								stroke, !pauseRecord, orderMid);
					} else if (commVal == 11) {
						while (rectangleCommandList.size() > 0) {
							commandObj = rectangleCommandList.remove(0);
							playView.rect_move(
									(float) (commandObj.getDouble("x")),
									(float) (commandObj.getDouble("y")),
									!pauseRecord);
						}
						playView.rect_up((float) (commandObj.getDouble("x")),
								(float) (commandObj.getDouble("y")),
								!pauseRecord);
						rectangleCommandList.clear();
					} else {
						while (rectangleCommandList.size() > 0) {
							commandObj = rectangleCommandList.remove(0);
							playView.rect_move(
									(float) (commandObj.getDouble("x")),
									(float) (commandObj.getDouble("y")),
									!pauseRecord);
						}
					}
					break;
				case Config.OPER_LINE:
					if (commVal == 13) {
						playView.line_start(
								(float) (commandObj.getDouble("x")),
								(float) (commandObj.getDouble("y")), color,
								stroke, !pauseRecord, orderMid);
					} else if (commVal == 14) {
						while (lineCommandList.size() > 0) {
							commandObj = lineCommandList.remove(0);
							playView.line_move(
									(float) (commandObj.getDouble("x")),
									(float) (commandObj.getDouble("y")),
									!pauseRecord);
						}
						playView.line_up((float) (commandObj.getDouble("x")),
								(float) (commandObj.getDouble("y")),
								!pauseRecord);
						lineCommandList.clear();
					} else {
						while (lineCommandList.size() > 0) {
							commandObj = lineCommandList.remove(0);
							playView.line_move(
									(float) (commandObj.getDouble("x")),
									(float) (commandObj.getDouble("y")),
									!pauseRecord);
						}
					}
					break;
				case Config.OPER_CURSOR:
					if (commVal == 16) {
						playView.cursor_start(
								(float) (commandObj.getDouble("x")),
								(float) (commandObj.getDouble("y")),
								!pauseRecord, orderMid);
					} else if (commVal == 17) {
						while (cursorCommandList.size() > 0) {
							commandObj = cursorCommandList.remove(0);
							playView.cursor_move(
									(float) (commandObj.getDouble("x")),
									(float) (commandObj.getDouble("y")),
									!pauseRecord);
						}
						playView.cursor_up(
								(float) (commandObj.getDouble("x")),
								(float) (commandObj.getDouble("y")),
								!pauseRecord);
						cursorCommandList.clear();
					} else {
						while (cursorCommandList.size() > 0) {
							commandObj = cursorCommandList.remove(0);
							playView.cursor_move(
									(float) (commandObj.getDouble("x")),
									(float) (commandObj.getDouble("y")),
									!pauseRecord);
						}
					}
					break;
				case Config.OPER_CLEAN_RECT:
					playView.isContainMemory(midList);
					midList.clear();
					break;
				case Config.OPER_TEXT_ADD: // 添加文本
					viewGroup.initEditText(commandObj.getInt("x"),
							commandObj.getInt("y"), commandObj.getInt("width"),
							commandObj.getInt("height"),
							commandObj.getString("words"), commVal,
							!pauseRecord);
					break;
				case Config.OPER_TEXT_EDIT: // 修改文本
					modifyText(commandObj);
					break;
				case Config.OPER_TEXT_DELETE: // 删除文本
					viewGroup.delEditText(
							(PlayEditText) groupBean.viewMap.get(commVal),
							!pauseRecord, true);
					break;
				case Config.OPER_TEXT_MOVE: // 移动文本
					viewGroup.moveText(commandObj.getInt("x"),
							commandObj.getInt("y"), !pauseRecord);
					break;
				case Config.OPER_IMG_ADD: // 添加图片
					JSONObject detail = commandObj.getJSONObject("detail");
					viewGroup.initImageView2(viewGroup, AtyPlay.this,
							bmpMap.get(commVal), commVal, detail.getInt("x"),
							detail.getInt("y"), width, height, !pauseRecord);
					break;
				case Config.OPER_IMG_DELETE: // 删除图片
					viewGroup.delImageView(
							(PlayImageView) (groupBean.viewMap.get(commVal)),
							!pauseRecord, true);
					break;
				case Config.OPER_IMG_MOVE: // 移动图片
					viewGroup.moveImage(commandObj.getInt("x"),
							commandObj.getInt("y"), !pauseRecord);
					break;
				case Config.OPER_IMG_SCALE: // 缩放图片
					viewGroup.scaleImage(floatVal, !pauseRecord);
					break;
				case Config.OPER_IMG_ROTATE: // 旋转图片
					viewGroup.rotateImage((float) floatRotation, !pauseRecord);
					break;
				case REFRESHALL:
					// 清空画笔对象
					playView.clearCanvas();
					// 清除界面控件
					viewGroup.removeAllViews();
					groupBean = viewGroupList.get(currPager);
					// 把当前界面上的控件渲染出来
					viewGroup.setBackground(backgroundOrder);
					list = groupBean.viewOrderList;
					for (int i = 0; i < list.size(); i++) {
						View view = list.get(i);
						view.setVisibility(View.VISIBLE);
						if (view instanceof PlayEditText) {
							PlayEditText et = (PlayEditText) view;
							et.setText(et.getTextStr());
							et.setTextSize(TypedValue.COMPLEX_UNIT_PX,
									et.getSize());
							et.setTextColor(et.getColor());
							et.setViewMaxWidth();
							// et.setLayout();
						}
						/*
						 * else if(view instanceof WacomImageViewPlayer){
						 * WacomImageViewPlayer img = (WacomImageViewPlayer)
						 * view; //img.setLayout(); }
						 */
						viewGroup.addView(view);
					}
					viewGroup.addView(playView);
					groupBean.drawPath(playView);
					break;
				case Config.OPER_UNPEN:
					groupBean.undoPen_Rubber(playView, !pauseRecord, orderMid);
					break;
				case Config.OPER_REPEN:
					groupBean.redoPen_Rubber(playView, !pauseRecord, orderMid);
					break;
				case Config.OPER_UNRUBBER:
					JSONArray idsUn = commandObj.getJSONArray("ids");
					int lengthUn = idsUn.length();
					StringBuilder sbUn = new StringBuilder();
					for (int i = 0; i < lengthUn; i++) {
						sbUn.append(idsUn.getString(i)).append(",");
					}
					playView.rubberDraw(sbUn.toString(), true);
					break;
				case Config.OPER_RERUBBER:
					JSONArray idsRe = commandObj.getJSONArray("ids");
					int lengthRe = idsRe.length();
					StringBuilder sbRe = new StringBuilder();
					for (int i = 0; i < lengthRe; i++) {
						sbRe.append(idsRe.getString(i)).append(",");
					}
					playView.rubberDraw(sbRe.toString(), false);
					break;
				case Config.OPER_TEXT_UNADD:
					groupBean.undoTextAdd(!pauseRecord, orderMid);
					break;
				case Config.OPER_TEXT_READD:
					groupBean.redoTextAdd(!pauseRecord, orderMid);
					break;
				case Config.OPER_TEXT_REEDIT:
				case Config.OPER_TEXT_UNEDIT:
					modifyText(commandObj);
					break;
				case Config.OPER_TEXT_UNMOVE:
				case Config.OPER_TEXT_REMOVE:
					JSONObject detailMove = commandObj.getJSONArray("detail")
							.getJSONObject(0);
					((PlayEditText) groupBean.viewMap.get(orderMid))
							.setLayoutByMid(detailMove.getInt("x"),
									detailMove.getInt("y"), !pauseRecord);
					break;
				case Config.OPER_TEXT_UNDELETE:
					groupBean.undoTextDelete(!pauseRecord, orderMid);
					break;
				case Config.OPER_TEXT_REDELETE:
					groupBean.redoTextDelete(!pauseRecord, orderMid);
					break;
				case Config.OPER_IMG_UNADD:
					groupBean.undoImgAdd(!pauseRecord, orderMid);
					break;
				case Config.OPER_IMG_READD:
					groupBean.redoImgAdd(!pauseRecord, orderMid);
					break;
				case Config.OPER_IMG_UNMOVE:
				case Config.OPER_IMG_REMOVE:
					PlayImageView img = (PlayImageView) groupBean.viewMap
							.get(orderMid);
					img.startMove();
					JSONObject data = commandObj.getJSONArray("detail")
							.getJSONObject(0);
					img.setLayoutByMid(data.getInt("x"), data.getInt("y"),
							!pauseRecord);
					break;
				case Config.OPER_IMG_UNSCALE:
				case Config.OPER_IMG_RESCALE:
					PlayImageView imgScale = (PlayImageView) groupBean.viewMap
							.get(orderMid);
					imgScale.startScaleImage();
					JSONObject dataScale = commandObj.getJSONArray("detail")
							.getJSONObject(0);
					imgScale.scaleImage(dataScale.getDouble("scale"),
							!pauseRecord);
					break;
				case Config.OPER_IMG_UNROTATE:
				case Config.OPER_IMG_REROTATE:
					PlayImageView imgRotate = (PlayImageView) groupBean.viewMap
							.get(orderMid);
					imgRotate.startRotateImage();
					JSONObject dataRotate = commandObj.getJSONArray("detail")
							.getJSONObject(0);
					imgRotate
							.rotateImage((float) dataRotate.getDouble("angle"),
									!pauseRecord);
					break;

				case Config.OPER_IMG_UNDELETE:
					groupBean.undoImgDelete(!pauseRecord, orderMid);
					break;
				case Config.OPER_IMG_REDELETE:
					groupBean.redoImgAdd(!pauseRecord, orderMid);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				showMessage(R.string.play_error);
				System.out.println("-------异常");
			}
			handlerRunning = false;
			if (playThread.getState().equals(Thread.State.WAITING)
					|| playThread.getState().equals(Thread.State.RUNNABLE)) {
				synchronized (playThread) {
					playThread.notifyAll();
				}
			}
		}
	};

	/**
	 * 显示播放的时间进度
	 */
	Runnable showPlayTime = new Runnable() {
		@Override
		public void run() {
			long currentTimeMillis = System.currentTimeMillis();
			if (currentTimeMillis - startTime + seekProgres >= sumTime) {
				playCurrTimeTv.setText(DateUtil.longToDate(sumTime));
				videoSeekBar.setProgress(0);// sumTime
				showDialog();
				changeButton();
				seekProgres = 0;
			} else {
				playCurrTimeTv.setText(DateUtil.longToDate(System
						.currentTimeMillis() - startTime + seekProgres));
				videoSeekBar.setProgress((int) (System.currentTimeMillis()
						- startTime + seekProgres));
				postHandler.postDelayed(this, 1000);
			}
		}
	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				switch (msg.what) {
				case WRONG:
					if (progress != null && progress.isShowing()) {
						progress.dismiss();
					}
					showMessage(R.string.video_play_failed);
					break;
				case playControlTouchDown: // 展现播放控制
					if (mAlpha < 255 && !isHide) {
						// 通过设置不透明度设置按钮的渐显效果
						mAlpha += 50;
						if (mAlpha > 255) {
							mAlpha = 255;
						}
						controlLayout.setAlpha(mAlpha);
						controlLayout.invalidate();
						if (!isHide && mAlpha < 255)
							handler.sendEmptyMessageDelayed(
									playControlTouchDown, 100);
					}
					break;
				case playControlTouchUp: // 移除播放控制
					if (mAlpha > 0 && isHide) {
						mAlpha -= 50;
						if (mAlpha < 0) {
							mAlpha = 0;
						}
						controlLayout.setAlpha(mAlpha);
						controlLayout.invalidate();
						if (isHide && mAlpha > 0)
							handler.sendEmptyMessageDelayed(playControlTouchUp,
									800);
					}
					break;
				case NO_WEBURL:
					break;
				}
			} catch (Exception e) {
			}
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_DOWN:
			isHide = false;
			handler.sendEmptyMessage(playControlTouchDown);
			break;
		case MotionEvent.ACTION_UP:
			isHide = true;
			handler.sendEmptyMessageDelayed(playControlTouchUp, 1500);
			break;
		}
		return true;
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// if (hasException) {// 出现异常 返回键直接退出播放
	// ScreenManager.getInstance().popActivity(
	// VideoPlayerActivity.this);
	// } else {
	// new HintFinishDialog(VideoPlayerActivity.this).show();
	// pausePlay();// 暂停播放
	// }
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK: // 回退键
			if (hasException) {// 出现异常 返回键直接退出播放
				ScreenManager.getInstance().popActivity(AtyPlay.this);
			} else {
				// new HintFinishDialog(VideoPlayerActivity.this).show();
				pausePlay();// 暂停播放
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		try {
			hasException = false;
			groupBean.getHistoryList().clear();
			// 销毁播放器
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
				mediaPlayer.release();
				mediaPlayer = null;
			}
			playView.desctroy();
			if (viewGroupList != null) {
				viewGroupList.clear();
				viewGroupList = null;
			}
			if (playThread.isAlive()) {
				playThread.stop();
			}
			playThread = null;
		} catch (Exception e) {
		}
		try {
			commandArray = null;
			viewGroupList = null;
			viewGroup = null;
			if (bmpMap != null && bmpMap.size() > 0) {
				// for (Map.Entry<Integer, Bitmap> entry : bmpMap.entrySet()) {
				// entry.getValue().recycle();
				// }
				bmpMap.clear();
				System.gc();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.video_play_btn) {
			if (videoPause) {
				continuePlay();
			} else {
				pausePlay();
				showDialog();
			}
		}
	}

	/** 暂停播放 */
	private void pausePlay() {
		try {
			videoPause = true;
			mediaPlayer.pause();
			currentTime = System.currentTimeMillis();
			postHandler.removeCallbacks(showPlayTime);
			playBtn.setBackgroundResource(R.drawable.video_player_play);
		} catch (Exception e) {

		}
	}

	/** 继续播放 */
	public void continuePlay() {
		// 继续播放设置进度条为不可见状态
		// controlLayout.setAlpha(0);
		// controlLayout.invalidate();

		videoPause = false;
		mediaPlayer.start();
		startTime = System.currentTimeMillis() - currentTime + startTime;
		postHandler.post(showPlayTime);
		playBtn.setBackgroundResource(R.drawable.video_player_pause);
		synchronized (playThread) {
			playThread.notifyAll();
		}
	}

	/** 重新播放 */
	private void rePlay() {
		// if (ischangbt) {
		// playCurrTimeTv.setText(DateUtil.longToDate(0));// 设置时间归零
		// }
		// ischangbt = false;
		initVal();
		startTime = 0;
		viewGroup.removeAllViews();
		playView.clearCanvas();
		viewGroup.addView(playView);
		groupBean = new ViewGroupBean(viewGroup);
		viewGroupList.clear();
		viewGroupList.add(groupBean);
		stopCurrPlayThread = false;
		playThread = new PlayThread();
		playThread.start();
	}

	/** -------进度条监听--------- */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
			seekProgres = progress;
			playCurrTimeTv.setText(DateUtil.longToDate(seekProgres));
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// 在拖动中会调用此方法
		handlerRunning = false;
		stopCurrPlayThread = true;
		synchronized (playThread) {
			playThread.notifyAll();
		}
		pausePlay();
		seekProgres = 0;
		try {
			playThread.stop();
		} catch (Exception e) {
		}
		postHandler.removeCallbacks(playRunable);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// 停止拖动
		mediaPlayer.seekTo(seekProgres);
		videoPause = false;
		playBtn.setBackgroundResource(R.drawable.video_player_pause);
		rePlay();
	}

	private AlertDialog dialog; // 对话框
	private Button sharebt; // 分享按钮
	private Button againbt; // 重播按钮
	private boolean ischangbt = false; // 按钮改变标识
	private TextView quitTv; // 退出按钮
	private RelativeLayout quitLayout;// 退出

	/**
	 * 显示分享对话框
	 */
	private void showDialog() {
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.share_dialog);

		quitTv = (TextView) window.findViewById(R.id.quit_tv);// 退出播放器文本
		quitTv.setEnabled(false);
		againbt = (Button) window.findViewById(R.id.continue_bt);
		againbt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ischangbt) {
					groupBean.getHistoryList().clear();

					if (ischangbt) {
						playCurrTimeTv.setText(DateUtil.longToDate(0));// 设置时间归零
					}
					ischangbt = false;
					rePlay();
				} else {
					continuePlay();
				}
				dialog.dismiss();
			}
		});
		sharebt = (Button) window.findViewById(R.id.share_bt);
		sharebt.setOnClickListener(new OnClickListener() { // 分享按钮
			@Override
			public void onClick(View v) {
				if (weburl == null || "".equals(weburl)) { // 如果没有访问的url，则上传
					handler.sendEmptyMessage(NO_WEBURL);
				} else {// 否则，直接开启上传界面
					// new ShareListener(VideoPlayerActivity.this, mController,
					// (int) sharebt.getMeasuredHeight()).showPopupWindow(
					// v, VideoPlayerActivity.this);
				}
			}
		});
		quitTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// 退出播放器
				ScreenManager.getInstance().popActivity(AtyPlay.this);
			}
		});
		quitLayout = (RelativeLayout) window.findViewById(R.id.video_play_quit);
		quitLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// 退出播放器
				ScreenManager.getInstance().popActivity(AtyPlay.this);
			}
		});
	}

	/**
	 * 改变标识
	 */
	private void changeButton() {
		ischangbt = true;
		againbt.setBackgroundResource(R.drawable.replay);
		againbt.setText(R.string.video_replay);
	}
}
