package com.lib.play_rec.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lib.play_rec.R;
import com.lidroid.xutils.BitmapUtils;

/**
 * 图片展示适配器
 * @author Administrator
 *
 */
public class CameraImageAdapter extends BaseAdapter {

	private List<String> list;
	private LayoutInflater inflater;
	private RelativeLayout.LayoutParams params;
	private HashMap<Integer, Boolean> selMap;
	private List<String> selCBPosList;
	private Context context;
	
	private boolean isShowDelete = false;

	public CameraImageAdapter(Context context, List<String> list, int w, int h) {
		this.context=context;
		this.list = list;
		inflater = LayoutInflater.from(context);

		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.width = w;
		params.height = h;

		selMap = new HashMap<Integer, Boolean>();
		selCBPosList = new ArrayList<String>();
	}

	public void setShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
		selCBPosList.clear();
		selMap.clear();
		for (int i = 0; i < list.size(); i++) {
			selMap.put(i, false);
		}
		notifyDataSetChanged();
	}

	public boolean isShowDelete() {
		return isShowDelete;
	}

	public HashMap<Integer, Boolean> getSelMap() {
		return selMap;
	}

	public List<String> getSelCBPosList() {
		selCBPosList.clear();
		for (Map.Entry<Integer, Boolean> entry : selMap.entrySet()) {
			if (entry.getValue()) {
				selCBPosList.add(list.get(entry.getKey()));
			}
		}
		return selCBPosList;
	}

	public void setList(List<String> list) {
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
			convertView = inflater.inflate(R.layout.camera_image_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imgView = (ImageView) convertView
					.findViewById(R.id.imgcamera);
			viewHolder.imgView.setLayoutParams(params);
			viewHolder.delCB = (CheckBox) convertView
					.findViewById(R.id.camera_del_cb);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String url = list.get(position);
		BitmapUtils bitmapUtils=new BitmapUtils(context);
		bitmapUtils.display(viewHolder.imgView, url); 
		if (isShowDelete) {
			viewHolder.delCB.setChecked(selMap.get(position));
			viewHolder.delCB.setVisibility(View.VISIBLE);
		} else {
			viewHolder.delCB.setChecked(false);
			viewHolder.delCB.setVisibility(View.GONE);
		}
		return convertView;
	}

	public class ViewHolder {
		public ImageView imgView;
		public CheckBox delCB;
	}

}
