package com.lib.play_rec.listener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lib.play_rec.R;
import com.lib.play_rec.emue.RecMeterialEvent;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.rec.JsonOperater;
import com.lib.play_rec.rec.RecordViewGroup;
import com.lib.play_rec.utils.DateUtil;

import java.io.File;

@SuppressLint("NewApi")
public class FodderSetListener implements OnClickListener {

	private Activity activity;
	private ImageView opttypeBtn;
	private PopupWindow popupWindow;
	private RecordViewGroup viewGroup;
	private float density = 1f;

	private LinearLayout llFodder;
	private Animation animation;

	public FodderSetListener(final Activity activity, ImageView opttypeBtn) {
		this.activity = activity;
		this.opttypeBtn = opttypeBtn;
		density = GlobalInit.getInstance().getScreenDensity();

		View floatView = LayoutInflater.from(activity).inflate(
				R.layout.float_fodderset, null);
		llFodder = (LinearLayout) floatView.findViewById(R.id.ll_fodder);

		ImageView textImg = (ImageView) floatView
				.findViewById(R.id.iv_fodder_text);// 文字
		textImg.setOnClickListener(this);
		ImageView photoImg = (ImageView) floatView
				.findViewById(R.id.iv_fodder_photo);// 相片
		photoImg.setOnClickListener(this);
		ImageView cameraImg = (ImageView) floatView
				.findViewById(R.id.iv_fodder_camera);// 照相机
		cameraImg.setOnClickListener(this);

		popupWindow = new PopupWindow(floatView,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		// 设置透明的背景色
		ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
		popupWindow.setBackgroundDrawable(colorDrawable);
		popupWindow.setOutsideTouchable(false);
	}

	public void showPopupWindow(final View v, RecordViewGroup viewGroup) {
		this.viewGroup = viewGroup;
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		// 显示动画
		animation = AnimationUtils.loadAnimation(activity,
				R.anim.popshow_fodder_anim);
		llFodder.startAnimation(animation);
		popupWindow.showAtLocation(
				v,
				Gravity.NO_GRAVITY,
				location[0]
						- (int) (activity.getResources().getDimension(
								R.dimen.record_fodder_space) * density),
				location[1]);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.iv_fodder_text) {// 文字
			opttypeBtn.setImageResource(R.drawable.opt_hand);
			viewGroup.editTextColor = Color.BLACK;
			viewGroup.editTextSize = 26;
			viewGroup.setOperate(Config.OPER_TEXT_ADD);
		} else if (id == R.id.iv_fodder_photo) {// 读取相册
			try {
				Intent pic = new Intent(Intent.ACTION_PICK);
				// 开启Pictures画面Type设定为image
				pic.setType("image/*");
				/**
				 * intent.setType("audio/*"); 选择音频 intent.setType("video/*");
				 * 选择视频 （mp4 3gp 是android支持的视频格式）
				 * intent.setType("video/*;image/*"); 同时选择视频和图片
//				 */
			// TODO 素材按钮闪动
			activity.startActivityForResult(pic,
						RecMeterialEvent.PHONE_PIC.getTag());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (id == R.id.iv_fodder_camera) {// 拍照
			try {
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					JsonOperater.getInstance().setTempImgName(
							DateUtil.getTimeMillisStr()
									+ Config.LOCAL_IMAGE_SUFFIX);
					camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							.fromFile(new File(GlobalInit.getInstance()
									.getTempPath()
									+ JsonOperater.getInstance()
											.getTempImgName())));
					activity.startActivityForResult(camera,
							RecMeterialEvent.PHONE_CAMERA.getTag());
				} else {
					Toast.makeText(activity, "SD卡是否准备好"
					// R.string.SD_sure
							, Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.destroy();
	}

	public void destroy() {
		popupWindow.dismiss();
	}

}
