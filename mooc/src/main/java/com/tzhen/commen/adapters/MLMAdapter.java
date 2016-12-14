package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tongzhen.mooc.entities.types.WorksListType;
import com.tzhen.mooc.R;
import com.tzhen.mooc.main.WorksListFragment_;

/**
 * Created by wuyong on 2016/12/14.
 */
public class MLMAdapter extends FragmentPagerAdapter {
    private int[] titles = {R.string.featured, R.string.shared, R.string.favorites};
    private Context context;

    public MLMAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = WorksListFragment_.builder().listType(WorksListType.FEATURED).build();
                break;
            case 1:
                frag = WorksListFragment_.builder().listType(WorksListType.SHARED).build();
                break;
            case 2:
                frag = WorksListFragment_.builder().listType(WorksListType.FAVORITES).build();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return titles.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(titles[position]);
    }
}
