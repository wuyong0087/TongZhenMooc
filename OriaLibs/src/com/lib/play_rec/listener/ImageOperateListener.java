package com.lib.play_rec.listener;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.rec.HistoryMemory;
import com.lib.play_rec.rec.RecordImageView;
import com.lib.play_rec.rec.RecordViewGroup;

public class ImageOperateListener implements OnClickListener {

	private PopupWindow popupWindow;
	private ImageButton imageDelBtn, rotateBtn, imageLockBtn;
	private RecordImageView mimg;

	public ImageOperateListener(Context context) {

		View floatView = LayoutInflater.from(context).inflate(
				R.layout.float_imageview, null);
		imageDelBtn = (ImageButton) floatView.findViewById(R.id.image_del_btn);
		rotateBtn = (ImageButton) floatView.findViewById(R.id.rotate_btn);
		imageLockBtn = (ImageButton) floatView
				.findViewById(R.id.image_lock_btn);

		imageDelBtn.setOnClickListener(this);
		rotateBtn.setOnClickListener(this);
		imageLockBtn.setOnClickListener(this);

		popupWindow = new PopupWindow(floatView, (int) (180 * GlobalInit
				.getInstance().getScreenDensity()), (int) (60 * GlobalInit
				.getInstance().getScreenDensity()));
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.edit_popup_bg));
		popupWindow.setOutsideTouchable(true);

		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// met.setBackgroundResource(R.drawable.edit_bg);
			}
		});
	}

	public void showPopupWindow(View v, RecordImageView mimg) {
		// met.setBackgroundResource(R.drawable.edit_bg_sel);
		this.mimg = mimg;
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
				location[0] + (v.getWidth() - popupWindow.getWidth()) / 2,
				v.getHeight() - popupWindow.getHeight() - 10);
	}

	public boolean isShowPopupWindow() {
		return popupWindow.isShowing();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id== R.id.image_del_btn) {// 删除操作
			((RecordViewGroup) (mimg.getParent())).delImageView(mimg, true);
			popupWindow.dismiss();
		}else if (id==R.id.rotate_btn) {
			mimg.rotate90(true);
		}else if (id==R.id.image_lock_btn) {
			if (mimg.isLock()) {
				mimg.setLock(false);
				imageLockBtn.setBackgroundResource(R.drawable.lock_bg);
			} else {
				mimg.setLock(true);
				imageLockBtn.setBackgroundResource(R.drawable.unlock_bg);
			}

			HistoryMemory memory = new HistoryMemory(3);
			memory.setOperaterType(Config.OPER_IMG_LOCK);
			memory.setLock(mimg.isLock());
			memory.setMimg(mimg);
			((RecordViewGroup) (mimg.getParent())).historyOperater
					.setLock(memory);
		}
	}

}
