package com.lib.play_rec.listener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.lib.play_rec.R;
import com.lib.play_rec.adapter.WacomColorsAdapter;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.rec.RecordEditText;
import com.lib.play_rec.rec.RecordViewGroup;
import com.lib.play_rec.utils.BitmapUtil;

public class EditColorSetListener implements OnItemClickListener{
	
	private PopupWindow popupWindow;
	private int[] colors = {Color.argb(255, 70, 214, 166),Color.argb(255, 255, 255, 0),
			Color.argb(255, 128, 128, 255),Color.argb(255, 255, 255, 255),
			Color.argb(255, 255, 128, 128),Color.argb(255,255,128,0),
			Color.argb(255,0,42,128),Color.argb(255,0,36,255),
			Color.argb(255,255,0,0),Color.argb(255,11,11,11),
			Color.argb(255,214,22,255),Color.argb(255,64,154,255)};
	
	private Bitmap [] bmp;
	private float density = 1f;
	private RecordEditText met;
	
	public EditColorSetListener(Activity context,final EditOperateListener editOperate){
		density = GlobalInit.getInstance().getScreenDensity();
		bmp = new Bitmap[colors.length];
		for(int i=0;i<colors.length;i++){
			bmp[i] = BitmapUtil.getRoundedCornerBitmap((int) (40 * density),
					(int) (40 * density), colors[i],(int)(10 * density));
		}
		
		View floatView = View.inflate(context,R.layout.float_edit_colorset, null);
		GridView gridView = (GridView) floatView.findViewById(R.id.gridview);
		gridView.setAdapter(new WacomColorsAdapter(context,bmp));
		gridView.setOnItemClickListener(this);
		popupWindow = new PopupWindow(floatView,(int)(300*density),(int)(100*density));
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.edit_popup_bg));
		popupWindow.setOutsideTouchable(true);
		
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				editOperate.showPopupWindow();
			}
		});
	}
	
	public void showPopupWindow(RecordViewGroup viewGroup,RecordEditText met){
		this.met = met;
		int[] location = new int[2];  
		viewGroup.getLocationOnScreen(location);  
		
		/**
		 * 上方：popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getHeight());
		 * 下方：popupWindow.showAsDropDown(v);
		 * 左边：popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]-popupWindow.getWidth(), location[1]);  
		 * 右边：popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]+v.getWidth(), location[1]);  
		 */
		
		popupWindow.showAtLocation(viewGroup, Gravity.NO_GRAVITY, 
				 location[0]+(viewGroup.getWidth()-popupWindow.getWidth())/2, 
				 viewGroup.getHeight()-popupWindow.getHeight()-10);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		if(colors[position]!=met.getColor()){
			met.setColor(colors[position]);
		}
		popupWindow.dismiss();
	}

}
