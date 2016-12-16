package com.lib.play_rec.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * 不可滑动自定义ViewPager
 * 
 * @author Administrator
 * 
 */
public class CantScrollPager extends ViewPager {
	private boolean isCanScroll = true;

	public boolean isCanScroll() {
		return isCanScroll;
	}

	public void setCanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	public CantScrollPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CantScrollPager(Context context) {
		super(context);
	}

	@Override
	public void scrollTo(int x, int y) {
		if (isCanScroll) {
			super.scrollTo(x, y);
		}
	}

}
