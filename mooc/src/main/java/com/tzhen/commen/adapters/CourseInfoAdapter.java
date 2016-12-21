package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tzhen.mooc.R;

import java.util.List;

/**
 * Created by wuyong on 2016/12/21.
 */
public class CourseInfoAdapter extends FragmentPagerAdapter {
    private String[] types;
    private List<Fragment> frags;
    public CourseInfoAdapter(Context context, FragmentManager fm, List<Fragment> frags) {
        super(fm);
        this.frags = frags;
        types = context.getResources().getStringArray(R.array.course_info_type);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return frags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return types[position];
    }
}
