package com.lib.play_rec.listener;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.lib.play_rec.R;
import com.lib.play_rec.emue.RecPenEvent;
import com.lib.play_rec.entity.GlobalInit;
import com.ypy.eventbus.EventBus;

public class StrokeSetListener implements OnClickListener {

	private PopupWindow popupWindow;

	private Activity activity;
	private float density = 1f;
	private LinearLayout llStroke;
	private ImageButton ibtnSmall, ibtnMidle, ibtnBig;
	private Animation animation = null;// 设置动画
	
	// 事件分发总线
	private EventBus eventBus;
	private boolean isRegister;
	public StrokeSetListener(final Activity activity) {
		this.activity = activity;
		density = GlobalInit.getInstance().getScreenDensity();

		View floatView = LayoutInflater.from(activity).inflate(
				R.layout.float_strokeset, null);
		llStroke = (LinearLayout) floatView.findViewById(R.id.ll_stroke);
		// 初始化粗细选择按钮
		ibtnSmall = (ImageButton) floatView
				.findViewById(R.id.ibtn_stroke_small);
		ibtnMidle = (ImageButton) floatView
				.findViewById(R.id.ibtn_stroke_middle);
		ibtnBig = (ImageButton) floatView.findViewById(R.id.ibtn_stroke_big);
		// 设置粗细选择按钮的点击事件
		ibtnSmall.setOnClickListener(this);
		ibtnMidle.setOnClickListener(this);
		ibtnBig.setOnClickListener(this);

		popupWindow = new PopupWindow(floatView, -2,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		// 设置为透明的背景
		ColorDrawable transparentBG = new ColorDrawable(Color.TRANSPARENT);
		popupWindow.setBackgroundDrawable(transparentBG);
		popupWindow.setOutsideTouchable(true);
		
		eventBus = EventBus.getDefault();
		if (!isRegister) {
			eventBus.register(this);
			isRegister = true;
		}
		
	}

	public void showPopupWindow(View v) {
		// popupWindow显示位置
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		// 显示动画
		animation = AnimationUtils.loadAnimation(activity, R.anim.popshow_anim);
		llStroke.startAnimation(animation);

		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]
				+ (int) (activity.getResources().getDimension(R.dimen.record_pop_space) * density), location[1]);
	}
	
	public void onEventAsync(Object object) {
		
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id ==R.id.ibtn_stroke_small) {
			eventBus.post(RecPenEvent.STROKE_MIN);
		}else if (id ==R.id.ibtn_stroke_middle) {
			eventBus.post(RecPenEvent.STROKE_MID);
		}else if (id ==R.id.ibtn_stroke_big) {
			eventBus.post(RecPenEvent.STROKE_MAX);
		}
		popupWindow.dismiss();
		
		if (isRegister) {
			eventBus.unregister(this);
			isRegister = false;
		}
	}
	
}
