package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tzhen.mooc.R;
import com.tzhen.mooc.fragments.QAListFragment_;

/**
 * Created by wuyong on 2016/12/29.
 */
public class QAAdapter extends FragmentPagerAdapter {
    private int[] title = {R.string.listed, R.string.involed, R.string.watch};
    private Context context;
    public QAAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = QAListFragment_.builder().build();
        return frag;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(title[position]);
    }
}
