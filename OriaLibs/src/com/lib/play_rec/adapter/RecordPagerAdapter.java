package com.lib.play_rec.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lib.play_rec.rec.RecordViewGroup;

public class RecordPagerAdapter extends PagerAdapter {

	private List<RecordViewGroup> viewGroupList;

	public List<RecordViewGroup> getViewGroupList() {
		return viewGroupList;
	}

	public void setViewGroupList(List<RecordViewGroup> viewGroupList) {
		this.viewGroupList = viewGroupList;
	}

	@Override
	public int getCount() {
		return viewGroupList.size();
	}

	@Override
	public Object instantiateItem(View view, int pos) {
		((ViewPager) view).addView(viewGroupList.get(pos), 0);
		return viewGroupList.get(pos);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object view) {
		if (getCount() > 1) {
			((ViewPager) arg0).removeView(viewGroupList.get(arg1));
		}
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
