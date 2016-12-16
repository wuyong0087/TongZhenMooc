package com.lib.play_rec.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class WacomColorsAdapter extends BaseAdapter {

	private Context context;
	private Bitmap[] bmp;

	public Bitmap[] getBmp() {
		return bmp;
	}

	public void setBmp(Bitmap[] bmp) {
		this.bmp = bmp;
	}

	public WacomColorsAdapter(Context context,Bitmap[] bmp){
		this.context = context;
		this.bmp = bmp;
	}
	
	@Override
	public int getCount() {
		return bmp.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imgView;
		if(convertView==null){
			imgView = new ImageView(context);
			imgView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}else{
			imgView = (ImageView) convertView;
		}
		imgView.setImageBitmap(bmp[position]);
		return imgView;
	}

}
