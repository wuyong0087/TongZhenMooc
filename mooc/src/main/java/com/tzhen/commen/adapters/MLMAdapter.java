package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tzhen.mooc.R;
import com.tzhen.mooc.main.mlm.FeaturedWorksListFrag_;
import com.tzhen.mooc.main.mlm.MyCollectionWorksListFrag_;
import com.tzhen.mooc.main.mlm.SharedWorksListFrag_;

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
                frag = FeaturedWorksListFrag_.builder().build();
                break;
            case 1:
                frag = SharedWorksListFrag_.builder().build();
                break;
            case 2:
                frag = MyCollectionWorksListFrag_.builder().build();
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
