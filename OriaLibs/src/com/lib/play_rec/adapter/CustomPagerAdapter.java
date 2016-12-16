package com.lib.play_rec.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class CustomPagerAdapter extends PagerAdapter {

	private List<View> viewList;

	public List<View> getViewList() {
		return viewList;
	}

	public void setViewList(List<View> viewList) {
		this.viewList = viewList;
	}

	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public Object instantiateItem(View view, int pos) {
		((ViewPager) view).addView(viewList.get(pos), 0);
		return viewList.get(pos);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		if (getCount() > 1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.removeView(viewList.get(arg1));
		}
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
