package com.lib.play_rec.listener;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.rec.AtyRecord;
import com.lib.play_rec.rec.RecordViewGroup;

public class OptTypeSetListener implements OnClickListener {

	private Activity activity;
	private RecordViewGroup viewGroup;
	private PopupWindow popupWindow;
	private ImageView opttypeBtn;
	private LinearLayout optionLayout;
	private float density = 1f;
	private Animation animation;
	private ImageView pencilBtn, digPneBtn;

	public OptTypeSetListener(Activity activity, ImageView imageButton) {
		this.activity = activity;
		// 　录制界面的操作按钮
		this.opttypeBtn = imageButton;
		// 屏幕像素密度
		density = GlobalInit.getInstance().getScreenDensity();
		// 操作类型的根部局
		View floatView = LayoutInflater.from(activity).inflate(
				R.layout.float_opttypeset, null);
		// 画笔操作
		pencilBtn = (ImageView) floatView.findViewById(R.id.opt_pencil_btn);
		pencilBtn.setOnClickListener(this);
		// 数码笔相关
		digPneBtn = (ImageView) floatView.findViewById(R.id.opt_digital_btn);
		digPneBtn.setOnClickListener(this);
		// 手势操作
		ImageView handBtn = (ImageView) floatView
				.findViewById(R.id.opt_hand_btn);
		handBtn.setOnClickListener(this);
		// 橡皮擦
		ImageView wipeBtn = (ImageView) floatView
				.findViewById(R.id.opt_wipe_btn);
		wipeBtn.setOnClickListener(this);
		// 光标操作
		ImageView cursorBtn = (ImageView) floatView
				.findViewById(R.id.opt_cursor_btn);
		cursorBtn.setOnClickListener(this);
		// 多选删除矩形
		ImageView cleanRectBtn = (ImageView) floatView
				.findViewById(R.id.opt_clean_rect_btn);
		cleanRectBtn.setOnClickListener(this);
		// 数码笔操作
		ImageView digitalPrnBtn = (ImageView) floatView
				.findViewById(R.id.opt_digital_btn);
		digitalPrnBtn.setOnClickListener(this);
		// 箭头操作
		ImageView arrowBtn = (ImageView) floatView
				.findViewById(R.id.opt_arrow_btn);
		arrowBtn.setOnClickListener(this);
		// 椭圆操作
		ImageView ovalBtn = (ImageView) floatView
				.findViewById(R.id.opt_oval_btn);
		ovalBtn.setOnClickListener(this);
		// 矩形操作
		ImageView rectBtn = (ImageView) floatView
				.findViewById(R.id.opt_rectangle_btn);
		rectBtn.setOnClickListener(this);
		// 直线操作
		ImageView lineBtn = (ImageView) floatView
				.findViewById(R.id.opt_line_btn);
		lineBtn.setOnClickListener(this);

		optionLayout = (LinearLayout) floatView
				.findViewById(R.id.ll_option_type);

		popupWindow = new PopupWindow(floatView,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		ColorDrawable colorDrawable = new ColorDrawable(0000000);
		popupWindow.setBackgroundDrawable(colorDrawable);
		popupWindow.setOutsideTouchable(true);

		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
			}
		});
	}

	public void showPopupWindow(View v, RecordViewGroup viewGroup) {
		this.viewGroup = viewGroup;
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

		// if(ScreenCAPActivity.isDigital&&!Constant.isDigital){
		// digPneBtn.setVisibility(View.VISIBLE);
		// pencilBtn.setVisibility(View.GONE);
		// }else {
		// digPneBtn.setVisibility(View.GONE);
		// pencilBtn.setVisibility(View.VISIBLE);
		// }

		// 显示动画
		animation = AnimationUtils.loadAnimation(activity, R.anim.popshow_anim);
		optionLayout.startAnimation(animation);

		popupWindow.showAtLocation(
				v,
				Gravity.NO_GRAVITY,
				location[0]
						+ (int) (activity.getResources().getDimension(
								R.dimen.record_pop_space) * density),
				location[1]);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.rl_option_type) {
		
		// 手选状态 
		} else if (id == R.id.opt_hand_btn) {
			viewGroup.setOperate(Config.OPER_SELECT);
			opttypeBtn.setImageResource(R.drawable.opt_hand);
			
		// 橡皮擦
		} else if (id == R.id.opt_wipe_btn) {
			viewGroup.setOperate(Config.OPER_RUBBER);
			opttypeBtn.setImageResource(R.drawable.opt_wipe);
			
		// 画笔
		} else if (id == R.id.opt_pencil_btn) {
			viewGroup.setOperate(Config.OPER_PEN);
			opttypeBtn.setImageResource(R.drawable.opt_pencil);
			Config.isDigital = false;
			Toast.makeText(activity, "切换到手写状态", Toast.LENGTH_SHORT).show();
			
		// 数码笔
		} else if (id == R.id.opt_digital_btn) {
			// if (!PenConnectActivity.isConnected) {
			// Toast.makeText(activity, "请先连接数码笔", Toast.LENGTH_SHORT).show();
			// } else {
			// viewGroup.setOperate(Constant.PENCIL);
			// opttypeBtn.setImageResource(R.drawable.digital_pen_focus);
			// Constant.isDigital = true;
			// }
			
		// 箭头
		} else if (id == R.id.opt_arrow_btn) {
			viewGroup.setOperate(Config.OPER_ARROW); // TODO 修改选择后的背景图片
			// opttypeBtn.setImageResource(R.drawable.opt_arrow_sel);
			
		// 椭圆
		} else if (id == R.id.opt_oval_btn) {
			viewGroup.setOperate(Config.OPER_ELLIPSE);
			// opttypeBtn.setImageResource(R.drawable.opt_oval_sel);
			
		// 矩形
		} else if (id == R.id.opt_rectangle_btn) {
			viewGroup.setOperate(Config.OPER_RECT);
			// opttypeBtn.setImageResource(R.drawable.opt_arrow_sel);
		
		// 直线
		} else if (id == R.id.opt_line_btn) {
			viewGroup.setOperate(Config.OPER_LINE);
//			 opttypeBtn.setImageResource(R.drawable.opt_arrow_sel);
			
		// 光标
		} else if (id == R.id.opt_cursor_btn) {
			viewGroup.setOperate(Config.OPER_CURSOR);
			opttypeBtn.setImageResource(R.drawable.opt_cursor);
		// 矩形虚线索套
		} else if (id == R.id.opt_clean_rect_btn) {
			viewGroup.setOperate(Config.OPER_CLEAN_RECT);
			opttypeBtn.setImageResource(R.drawable.opt_wipe);
		}
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

}
