package com.lib.play_rec.oldplay;

import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lib.play_rec.R;
import com.lib.play_rec.entity.Config;
import com.lib.play_rec.entity.Constant;

public class PlayEditText extends EditText{
	
	/**  父控件传过来的值    */
	private VideoPlayerBaseAT player;
	
	/**  边界控制    */
	public final int maxTextSize=109,minTextSize=17;  // 文字最大最小值
	public final int sizeChangeMount = 3;  // 文本大小每次变化的量
	
	/**   对象属性    */
	private int mid;  // 自身id
	private long startTime;  // 开始时间
	private long endTime;   // 结束时间
	
	private int color;  // 文本颜色
	private int size=30;  // 文字大小
	public int posx,posy;  // 控件的位置
	public int viewWidth,viewHeight;  // 控件的宽高
	private boolean lock = false;  // 是否锁定
	private String textStr=""; // 文本内容
	
	private float[] move;
	private HistoryMemoryPlayer memory; 
	
	public interface OnFinishComposingListener{                 
		public void finishComposing();         
	} 

	public PlayEditText(VideoPlayerBaseAT player,int x,int y,
			int w,int h,int textColor,int textSize,String text,int mid) {
		super(player);
		this.player = player;
		this.posx = x;
		this.posy = y;
		this.viewWidth = w;
		this.viewHeight = h;
		this.color = textColor;
		this.size = textSize;
		this.textStr = text;
		this.mid = mid;
		
		ViewGroup.LayoutParams pa = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(pa);
		this.setSingleLine(false);
		this.setMaxLines(Integer.MAX_VALUE);
		this.setFocusable(false);
		this.setClickable(false);
		this.setFocusableInTouchMode(false);
		this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		this.setTextColor(color);
		this.setText(text);
		
		this.setHorizontallyScrolling(false);
		this.setBackgroundResource(R.drawable.edit_bg);  // 文本框背景
		this.setPadding(4, 7, 9, 7);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
	
	/**  移动文本  */
	public void startMoveText(){
		memory = new HistoryMemoryPlayer(2);
		memory.setStartTime(System.currentTimeMillis());
	}
	public void moveText(int center_x,int center_y,boolean refresh){
		setLayoutByMid(center_x, center_y,refresh);
		move = new float[]{center_x, center_y};
		memory.getMoveList().add(move);
		memory.getTimeList().add(System.currentTimeMillis());
	}
	
	public void endMoveText(){
		memory.setEndTime(System.currentTimeMillis());
		memory.setOperaterType(Config.OPER_TEXT_MOVE);
		memory.setMet(this);
		player.groupBean.setMove(memory);
	}
	
	/** 以文本中心点进行布局    */
	public void setLayoutByMid(int left,int top,boolean refresh){
		posx = (int) (left-viewWidth/2f+0.5);
		posy = (int) (top-viewHeight/2f+0.5);
		
		if(refresh){
			this.layout(posx, posy, posx+viewWidth, posy+viewHeight);
		}
	}
	
	public void setLayout(){
		this.layout(posx, posy, posx+viewWidth, posy+viewHeight);
	}
	
	/**  缩小文字大小,返回是否可继续缩小   */
	public boolean reduceTextSize(){
		if(size<=minTextSize){
			size = minTextSize;
			return false;
		}else{
			setSize(size-sizeChangeMount,true);
			if(size<=minTextSize){
				return false;
			}else{
				return true;
			}
		}
	}
	
	/**   放大文字大小 ，放回是否可继续放大   */
	public boolean enlargeTextSize(){
		if(size>=maxTextSize){
			size = maxTextSize;
			return false;
		}else{
			setSize(size+sizeChangeMount,true);
			if(size>=maxTextSize){
				return false;
			}else{
				return true;
			}
		}
	}
	
	/**  设置文本颜色  */
	public int getColor() {
		return color;
	}
	public void setColor(int color,boolean refresh) {
		this.color = color;
		player.editTextColor = color;  //保存字体颜色到父控件
		if(refresh){
			this.setTextColor(color);
		}
	}

	/**  设置文本大小  */
	public int getSize() {
		return size;
	}
	public void setSize(int size,boolean refresh) {
		this.size = size;
		player.editTextSize = size;  //保存字体大小到父控件
		if(refresh){
			this.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
		}
	}
	
	/**  设置文本   */
	public String getTextStr() {
		return textStr;
	}
	public void setTextStr(String textStr,boolean refresh) {
		this.textStr = textStr;
		if(refresh){
			this.setText(textStr);
		}
	}
	/**  设置控件锁定  */
	public boolean isLock() {
		return lock;
	}
	public void setLock(boolean lock) {
		this.lock = lock;
	}
	
	/**  设置最大宽度  */
	public void setViewWidthHeight(int viewWidth,int viewHeight,boolean refresh) {
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
		if(refresh){
			this.setMaxWidth(viewWidth);
		}
	}
	public void setViewMaxWidth(){
		this.setMaxWidth(viewWidth);
	}
	
	public int getMid() {
		return mid;
	}
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
