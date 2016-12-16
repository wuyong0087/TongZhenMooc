package com.lib.play_rec.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectBoxView extends LinearLayout {
	
	private TextView tv;
	private ImageView imgView;

	public SelectBoxView(Context context) {
		super(context);
		initView(context);
	}

	public SelectBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public SelectBoxView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	void initView(Context context){
		this.setPadding(7, 3, 7, 3);
		// 添加文本控件
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		tv = new TextView(context);
		tv.setLayoutParams(params);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
		tv.setTextColor(Color.BLACK);
		tv.setClickable(false);
		tv.setFocusable(false);
		this.addView(tv);
		
		// 添加图片控件
		imgView = new ImageView(context);
		params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		imgView.setLayoutParams(params);
		imgView.setClickable(false);
		imgView.setFocusable(false);
		this.addView(imgView);
	}
	
	public void setText(String text){
		tv.setText(text);
	}
	
	public void setTextColor(int color){
		tv.setTextColor(color);
	}
	
	public void setTextSize(int size){
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
	}
	
	public void setImgResouce(int res){
		imgView.setImageResource(res);
	}
	
}
