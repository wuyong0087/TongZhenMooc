package com.tzhen.commen.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tzhen.mooc.R;
import com.tzhen.mooc.main.contacts.FansListFrag_;
import com.tzhen.mooc.main.contacts.FollowsListFrag_;
import com.tzhen.mooc.main.contacts.FriendsListFrag_;

/**
 * Created by wuyong on 16/12/30.
 */
public class ContactListAdapter extends FragmentPagerAdapter {
    private int[] title = {R.string.friends, R.string.fans, R.string.follows};
    private Context context;

    public ContactListAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = FriendsListFrag_.builder().build();
                break;
            case 1:
                frag = FansListFrag_.builder().build();
                break;
            case 2:
                frag = FollowsListFrag_.builder().build();
                break;
        }
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
