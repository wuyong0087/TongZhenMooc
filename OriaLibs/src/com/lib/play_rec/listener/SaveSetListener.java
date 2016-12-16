package com.lib.play_rec.listener;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.entity.WorksBean;
import com.lib.play_rec.rec.AtyRecord;

public class SaveSetListener implements OnClickListener {

	private Activity activity;
	private WorksBean workBean;
	private PopupWindow popupWindow;
	private float density = 1f;

	public SaveSetListener(final Activity activity, WorksBean noteBean) {
		this.activity = activity;
		this.workBean = noteBean;
		density = GlobalInit.getInstance().getScreenDensity();

		View floatView = LayoutInflater.from(activity).inflate(
				R.layout.float_saveset, null);
		RelativeLayout saveVideoLayout = (RelativeLayout) floatView
				.findViewById(R.id.save_save_video);// 保存微课
		saveVideoLayout.setOnClickListener(this);
		RelativeLayout saveDraftLayout = (RelativeLayout) floatView
				.findViewById(R.id.save_save_draft);// 保存讲义
		saveDraftLayout.setOnClickListener(this);
		RelativeLayout quitLayout = (RelativeLayout) floatView
				.findViewById(R.id.save_quit_rec);// 放弃录制
		quitLayout.setOnClickListener(this);

		popupWindow = new PopupWindow(floatView, (int) (130 * density),
				LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(activity.getResources().getDrawable(// scr_save_dialog_bg
				R.drawable.scr_save_dialog_bg));// box_right_bottom_bg
		popupWindow.setOutsideTouchable(false);

	}

	public void showPopupWindow(final View v) {
		int[] location = new int[2];
		v.getLocationOnScreen(location);

		/**
		 * 上方：popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0],
		 * location[1]-popupWindow.getHeight());
		 * 下方：popupWindow.showAsDropDown(v); 左边：popupWindow.showAtLocation(v,
		 * Gravity.NO_GRAVITY, location[0]-popupWindow.getWidth(), location[1]);
		 * 右边：popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
		 * location[0]+v.getWidth(), location[1]);
		 */
		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
				(int) (location[0] - (density * 140)),
				(int) (location[1] - (density * 100)));
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.save_save_video) {// 保存视频
			if (AtyRecord.recorded == true) {
				if (AtyRecord.recording) {
					((AtyRecord)activity).pauseRecord();
				}
				new SaveVideoDialog(activity, false, workBean).show();
			} else {
				Toast.makeText(activity, R.string.no_recording,
						Toast.LENGTH_SHORT).show();
			}
		} else if (id == R.id.save_save_draft) {// 保存讲义
			if (AtyRecord.recording) {
				((AtyRecord)activity).pauseRecord();
			}
			new SaveVideoDialog(activity, true, workBean).show();

		} else if (id == R.id.save_quit_rec) {// 放弃录制
			if (AtyRecord.recording) {
				((AtyRecord)activity).pauseRecord();
			}
			// new HintFinishDialog(activity).show();
		}
		popupWindow.dismiss();
	}

}
