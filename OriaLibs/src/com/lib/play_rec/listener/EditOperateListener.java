package com.lib.play_rec.listener;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.GlobalInit;
import com.lib.play_rec.rec.HistoryMemory;
import com.lib.play_rec.rec.RecordEditText;
import com.lib.play_rec.rec.RecordViewGroup;

public class EditOperateListener implements OnClickListener {
	
	private Activity context;
	private PopupWindow popupWindow;
	private ImageButton editEditBtn,editSmallBtn,editBigBtn,editColorBtn,editDelBtn,editLockBtn;
	private RecordEditText met;
	private RecordViewGroup viewGroup;
	private boolean isEditTextBgChange=true;  // 用于在popup关闭时是否改变文本框的背景
	private EditColorSetListener editColorSet;
	
	public int size,color;
	private boolean isDel = false;
	public EditOperateListener(Activity context){
		this.context = context;
		View floatView = LayoutInflater.from(context).inflate(R.layout.float_edit, null);
		editEditBtn = (ImageButton)floatView.findViewById(R.id.edit_edit_btn);
		editSmallBtn = (ImageButton)floatView.findViewById(R.id.edit_small_btn);
		editBigBtn = (ImageButton)floatView.findViewById(R.id.edit_big_btn);
		editColorBtn = (ImageButton)floatView.findViewById(R.id.edit_color_btn);
		editDelBtn = (ImageButton)floatView.findViewById(R.id.edit_del_btn);
		editLockBtn = (ImageButton)floatView.findViewById(R.id.edit_lock_btn);
		
		editEditBtn.setOnClickListener(this);
		editSmallBtn.setOnClickListener(this);
		editBigBtn.setOnClickListener(this);
		editColorBtn.setOnClickListener(this);
		editDelBtn.setOnClickListener(this);
		editLockBtn.setOnClickListener(this);
		
		editColorSet = new EditColorSetListener(context,this);
		
		popupWindow = new PopupWindow(floatView,
				(int)(380*GlobalInit.getInstance().getScreenDensity()),
				(int)(60*GlobalInit.getInstance().getScreenDensity()));
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.edit_popup_bg));
		popupWindow.setOutsideTouchable(true);
		
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				if(isEditTextBgChange){
					met.setBackgroundResource(R.drawable.edit_bg);
					save();
				}
			}
		});
	}
	
	/**  用于从颜色选择框转向的  */
	public void showPopupWindow(){
		isEditTextBgChange = true;
		int[] location = new int[2];  
		viewGroup.getLocationOnScreen(location); 
//		viewGroup.setOperate(Config.OPER_TEXT_EDIT);
		popupWindow.showAtLocation(viewGroup, Gravity.NO_GRAVITY, 
				 location[0]+(viewGroup.getWidth()-popupWindow.getWidth())/2, 
				 viewGroup.getHeight()-popupWindow.getHeight()-10);
	}
	
	/**  用于从文本选择转向的   */
	public void showPopupWindow(RecordViewGroup viewGroup,RecordEditText met){
		met.setBackgroundResource(R.drawable.edit_bg_sel);
		/**  文本大小判断  */
		if(met.getSize()>=met.maxTextSize){
			editBigBtn.setEnabled(false);
		}
		if(met.getSize()<=met.minTextSize){
			editSmallBtn.setEnabled(false);
		}
		size = met.getSize();
		color = met.getColor();
		isDel = false;
		isEditTextBgChange = true;
		this.met = met;
		this.viewGroup = viewGroup;
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
	
	public boolean isShowPopupWindow(){
		return popupWindow.isShowing();
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id ==R.id.edit_del_btn) {// 删除操作
//			viewGroup.setOperate(Config.OPER_TEXT_DELETE);
			save();
			((RecordViewGroup)(met.getParent())).delEditText(met,true);
			isDel = true;
			popupWindow.dismiss();
		}else if (id ==R.id.edit_color_btn) {// 设置颜色操作
			isEditTextBgChange = false;
			editColorSet.showPopupWindow(viewGroup,met);
			popupWindow.dismiss();
		}else if (id ==R.id.edit_small_btn) {// 缩小字号操作
			if(!met.reduceTextSize()){
				editSmallBtn.setEnabled(false);
			}
			editBigBtn.setEnabled(true);
		}else if (id ==R.id.edit_big_btn) {// 加大字号操作
			if(!met.enlargeTextSize()){
				editBigBtn.setEnabled(false);
			}
			editSmallBtn.setEnabled(true);
		}else if (id ==R.id.edit_edit_btn) {// 编辑文本操作
			
//			context.pauseRecord();  //暂停录制
			met.setFocusable(true);
			met.setFocusableInTouchMode(true);
			met.requestFocus();
			met.requestFocusFromTouch();
			met.setSelection(met.getText().length());
			met.setOldText(met.getTextStr());
			InputMethodManager imm = (InputMethodManager) context.
					getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.showSoftInput(met,InputMethodManager.SHOW_FORCED);  
			popupWindow.dismiss();
		}else if (id ==R.id.edit_lock_btn) {
			if(met.isLock()){
				met.setLock(false);
				editLockBtn.setBackgroundResource(R.drawable.lock_bg);
			}else{
				met.setLock(true);
				editLockBtn.setBackgroundResource(R.drawable.unlock_bg);
			}
			HistoryMemory memory = new HistoryMemory(2);
			memory.setOperaterType(Config.OPER_TEXT_LOCK);
			memory.setLock(met.isLock());
//			memory.setMet(met); // TODO 设置控件对象，保存锁定操作数据对象
//			((RecordViewGroup)(met.getParent())).historyOperater.setLock(memory);
		}
	}
	
	private void save(){
		if(isDel){
			return;
		}
		if(met.getSize()!=size || met.getColor()!=color){
			if (met.getOldText() == null) {
				met.setOldText(met.getTextStr());
			}
			if (met.getOldSize() < 1) {
				met.setOldSize(met.getSize());
			}
			((RecordViewGroup)(met.getParent())).modifyEditText(met,met.getOldText(),
					met.getTextStr(),size,met.getSize(),color,met.getColor(),true);
		}
	}

}
