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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.rec.RecordView;
import com.lib.play_rec.rec.RecordViewGroup;
import com.ypy.eventbus.EventBus;

public class ColorSetListener implements OnClickListener {

    private Activity activity;
    private RecordViewGroup viewGroup;
    private PopupWindow popupWindow;
    private float density = 1f;

    private LinearLayout llColorSet;
    private ImageView blackBtn, blueBtn, redBtn, yellowBtn, greenBtn,
            purpleBtn
//            ,whiteBtn
                    ;
    private ImageView opttypeBtn, toolboxBtn, colorBtn, colorMarkBtn;
    private Animation animation = null;// 设置动画
    private EventBus eventBus;
    private boolean isRegiser;

    public ColorSetListener(final Activity activity,
                            RecordViewGroup viewGroup, ImageView opttypeBtn,
                            ImageView toolboxBtn, ImageView colorBtn, ImageView colorClearBtn) {
        this.activity = activity;
        this.viewGroup = viewGroup;
        this.opttypeBtn = opttypeBtn;
        this.toolboxBtn = toolboxBtn;
        this.colorBtn = colorBtn;
        this.colorMarkBtn = colorClearBtn;
        density = GlobalInit.getInstance().getScreenDensity();
        View floatView = LayoutInflater.from(activity).inflate(
                R.layout.float_colorset, null);
        llColorSet = (LinearLayout) floatView.findViewById(R.id.ll_colors);

        /** 得到相应的颜色按钮 */
        blackBtn = (ImageView) floatView.findViewById(R.id.color_black);
        blueBtn = (ImageView) floatView.findViewById(R.id.color_blue);
        redBtn = (ImageView) floatView.findViewById(R.id.color_red);
        yellowBtn = (ImageView) floatView.findViewById(R.id.color_yellow);
        greenBtn = (ImageView) floatView.findViewById(R.id.color_green);
//        whiteBtn = (ImageView) floatView.findViewById(R.id.color_white);
        purpleBtn = (ImageView) floatView.findViewById(R.id.color_purple);
        blackBtn.setOnClickListener(this);
        blueBtn.setOnClickListener(this);
        redBtn.setOnClickListener(this);
        yellowBtn.setOnClickListener(this);
        greenBtn.setOnClickListener(this);
//        whiteBtn.setOnClickListener(this);
        purpleBtn.setOnClickListener(this);

        popupWindow = new PopupWindow(floatView, -2, -2);
        ColorDrawable colorDrawable = new ColorDrawable(0000000);
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        eventBus = EventBus.getDefault();
        if (!isRegiser) {
            eventBus.register(this);
            isRegiser = true;
        }
    }

    public void showPopupWindow(final View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        // 显示动画
        animation = AnimationUtils.loadAnimation(activity, R.anim.popshow_anim);
        llColorSet.startAnimation(animation);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]
                + (int) (activity.getResources().getDimension(R.dimen.record_pop_space) * density), location[1]);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        /** 重置画笔颜色 */
        if (id == R.id.color_black) {
            eventBus.post(Color.argb(255, 11, 11, 11));
        } else if (id == R.id.color_blue) {
            eventBus.post(Color.argb(255, 0, 36, 255));
        } else if (id == R.id.color_red) {
            eventBus.post(Color.argb(255, 255, 00, 0));
        } else if (id == R.id.color_yellow) {
            eventBus.post(Color.argb(255, 255, 255, 0));
        } else if (id == R.id.color_green) {
            eventBus.post(Color.argb(255, 70, 214, 116));
        } else if (id == R.id.color_purple) {
            eventBus.post(Color.argb(255, 214, 22, 255));
        }
        toolboxBtn.setImageResource(R.drawable.toolbox);
        if (!Config.isDigital) {// 不是蓝牙笔操作的话，改变按钮的背景和操作类型
            viewGroup.setOperate(Config.OPER_PEN);
            opttypeBtn.setImageResource(R.drawable.opt_pencil);
            colorBtn.setImageResource(R.drawable.colorset_bt_clear);
            colorMarkBtn.setBackgroundColor(RecordView.paintColor);
        }
        popupWindow.dismiss();

        if (isRegiser) {
            eventBus.unregister(this);
            isRegiser = false;
        }
    }

    public void onEventAsync(Object object) {

    }

    /**
     * 销毁对象
     */
    public void destroy() {
        popupWindow.dismiss();
    }

}
