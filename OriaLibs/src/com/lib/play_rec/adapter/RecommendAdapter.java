package com.lib.play_rec.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.RecommendBean;
import com.lib.play_rec.net.AsynImageLoader;

public class RecommendAdapter extends BaseAdapter {

	private GridView gridView;
	private List<RecommendBean> list;
	private LayoutInflater inflater;  
	private AsynImageLoader imageLoader;
	private LinearLayout.LayoutParams params;

	public RecommendAdapter(Context context,List<RecommendBean> list,GridView gridView,int w,int h){
		this.list = list;
		this.gridView = gridView;
		inflater = LayoutInflater.from(context);
		imageLoader = new AsynImageLoader();
		
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = w;
		params.height = h;
	}
	
	public void setList(List<RecommendBean> list){
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
		if(convertView==null){
			convertView = inflater.inflate(R.layout.recommend_img_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imgView = (ImageView)convertView.findViewById(R.id.img);
			viewHolder.imgView.setLayoutParams(params);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		RecommendBean bean = list.get(position);
		final String url = bean.getSmallImgUrl();
		viewHolder.imgView.setTag(url);
		Bitmap cacheBmp = imageLoader.loadImageAsyn(url,null, new AsynImageLoader.ImageCallback() {
			@Override
			public void loadImage(String path, Bitmap bitmap) {
				ImageView imgViewByTag = (ImageView)gridView.findViewWithTag(url);
				if(imgViewByTag!=null){
					imgViewByTag.setImageBitmap(bitmap);
				}
			}
		});
		if(cacheBmp==null){
			viewHolder.imgView.setImageResource(R.drawable.loading);
		}else{
			viewHolder.imgView.setImageBitmap(cacheBmp);
		}
		return convertView;
	}
	
	public class ViewHolder {  
        ImageView imgView;  
    }

}
