package com.lib.play_rec.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.RecommendBean;
import com.lib.play_rec.net.AsynImageLoader;

public class PPTImageAdapter extends BaseAdapter {

	private GridView gridView;
	private List<RecommendBean> list;
	private LayoutInflater inflater;
	private AsynImageLoader imageLoader;
	private RelativeLayout.LayoutParams params;
	private HashMap<Integer, Boolean> selMap;
	private List<RecommendBean> selCBPosList;

	public PPTImageAdapter(Context context, List<RecommendBean> list,
			GridView gridView, int w, int h) {
		this.list = list;
		this.gridView = gridView;
		inflater = LayoutInflater.from(context);

		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.width = w;
		params.height = h;

		selMap = new HashMap<Integer, Boolean>();
		imageLoader = new AsynImageLoader();
		selCBPosList=new ArrayList<RecommendBean>();
	}

	public HashMap<Integer, Boolean> getSelMap() {
		return selMap;
	}

	public List<RecommendBean> getSelCBPosList() {
		selCBPosList.clear();
		
		for (Map.Entry<Integer, Boolean> entry : selMap.entrySet()) {
			if (entry.getValue()) {
				selCBPosList.add(list.get(entry.getKey()));
			}
		}
		
		for (int i = 0; i < selCBPosList.size();i++) {
			list.remove(selCBPosList.get(i));
		}
		
		return list;
	}

	public void setList(List<RecommendBean> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.ppt_image_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imgView = (ImageView) convertView
					.findViewById(R.id.imgppt);
			viewHolder.imgView.setLayoutParams(params);
			viewHolder.delCB = (CheckBox) convertView
					.findViewById(R.id.ppt_del_cb);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		RecommendBean bean = list.get(position);
		final String url = bean.getSmallImgUrl();
		viewHolder.imgView.setTag(url);
		Bitmap cacheBmp = imageLoader.loadImageAsyn(url, null,
				new AsynImageLoader.ImageCallback() {
					@Override
					public void loadImage(String path, Bitmap bitmap) {
						ImageView imgViewByTag = (ImageView) gridView
								.findViewWithTag(url);
						if (imgViewByTag != null) {
							imgViewByTag.setImageBitmap(bitmap);
						}
					}
				});
		if (cacheBmp == null) {
			viewHolder.imgView.setImageResource(R.drawable.loading);
		} else {
			viewHolder.imgView.setImageBitmap(cacheBmp);
		}
		viewHolder.delCB.setVisibility(View.VISIBLE);
		if (bean.isCheck()) {
			viewHolder.delCB.setChecked(true);
		}else {
			viewHolder.delCB.setChecked(false);
		}
		return convertView;
	}

	public class ViewHolder {
		public ImageView imgView;
		public CheckBox delCB;
	}

}
