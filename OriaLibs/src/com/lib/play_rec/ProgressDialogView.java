package com.lib.play_rec;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lib.play_rec.play.VideoPlayerBaseAty;

public class ProgressDialogView extends Dialog {
	
	private Context context;
	private TextView msgTv;
	
	/*public ProgressSimpleView(Context context) {
		super(context, R.style.MyDialogTheme);
		this.context = context;
		init("");
	}*/
	public ProgressDialogView(Context context,String loginning) {
		super(context, R.style.MyDialogTheme);
		this.context = context;
		init(loginning);
	}
	
	void init(String msg){
		this.setCancelable(false);
		LayoutInflater inflater = (LayoutInflater)context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.ui_progress_view, null);
		msgTv = (TextView) view.findViewById(R.id.ui_progress_tv);
		msgTv.setText(msg);
		this.setContentView(view);
	}
	public ProgressDialogView(Context context,int loginning) {
		super(context, R.style.MyDialogTheme);
		this.context = context;
		init(loginning);
	}
	
	void init(int loginning){
		this.setCancelable(false);
		LayoutInflater inflater = (LayoutInflater)context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.ui_progress_view, null);
		msgTv = (TextView) view.findViewById(R.id.ui_progress_tv);
		msgTv.setText(loginning);
		this.setContentView(view);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (context instanceof VideoPlayerBaseAty) {
//			VideoPlayerBaseAty penBaseActivity = (VideoPlayerBaseAty) context;
//			penBaseActivity.mHandler.sendEmptyMessage(PenBaseActivity.PRESS_BACK_KEY); // TODO 数码笔相关的操作
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
