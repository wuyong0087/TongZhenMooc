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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.rec.RecordViewGroup;

public class ToolBoxListener implements OnClickListener {

	private PopupWindow popupWindow;

	private RecordViewGroup viewGroup;
	private ImageView toolboxBtn;
	private Activity activity;
	private float density = 1f;
	private LinearLayout llToolbox;
	private ImageButton arrowsBt, lineBt, rectangleBt, roundBt;

	private Animation animation;

	public ToolBoxListener(ImageView toolboxBtn,
			final Activity activity) {
		this.toolboxBtn = toolboxBtn;
		this.activity = activity;
		density = GlobalInit.getInstance().getScreenDensity();

		View floatView = LayoutInflater.from(activity).inflate(
				R.layout.float_toolbox, null);
		llToolbox = (LinearLayout) floatView.findViewById(R.id.ll_toolbox);
		// 初始化粗细选择按钮
		arrowsBt = (ImageButton) floatView.findViewById(R.id.toolbox_arrows_bt);// 箭头
		lineBt = (ImageButton) floatView.findViewById(R.id.toolbox_line_bt);// 直线
		rectangleBt = (ImageButton) floatView
				.findViewById(R.id.toolbox_rectangle_bt);// 矩形
		roundBt = (ImageButton) floatView.findViewById(R.id.toolbox_round_bt);// 圆形

		// 设置粗细选择按钮的点击事件
		arrowsBt.setOnClickListener(this);
		lineBt.setOnClickListener(this);
		rectangleBt.setOnClickListener(this);
		roundBt.setOnClickListener(this);

		popupWindow = new PopupWindow(floatView, -2,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		// 设置为透明的背景
		ColorDrawable transparentBG = new ColorDrawable(Color.TRANSPARENT);
		popupWindow.setBackgroundDrawable(transparentBG);
		popupWindow.setOutsideTouchable(true);

	}

	public void showPopupWindow(View v, RecordViewGroup viewGroup) {
		this.viewGroup = viewGroup;
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		// 显示动画
		animation = AnimationUtils.loadAnimation(activity, R.anim.popshow_anim);
//		popupWindow.setAnimationStyle(R.style.scrBt_popupLeft);
		llToolbox.startAnimation(animation);
		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]
				+ (int) (activity.getResources().getDimension(R.dimen.record_pop_space) * density), location[1]);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id ==R.id.toolbox_arrows_bt) {
			viewGroup.setOperate(Config.OPER_ARROW);
			toolboxBtn.setImageResource(R.drawable.toolbox_arrows);
		}else if (id ==R.id.toolbox_line_bt) {
			viewGroup.setOperate(Config.OPER_LINE);
			toolboxBtn.setImageResource(R.drawable.toolbox_line);
		}else if (id ==R.id.toolbox_rectangle_bt) {
			viewGroup.setOperate(Config.OPER_RECT);
			toolboxBtn.setImageResource(R.drawable.toolbox_rectangle);
		}else if (id ==R.id.toolbox_round_bt) {
			viewGroup.setOperate(Config.OPER_ELLIPSE);
			toolboxBtn.setImageResource(R.drawable.toolbox_round);
		}
		popupWindow.dismiss();
	}

}
