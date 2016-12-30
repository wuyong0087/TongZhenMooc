package com.tzhen.commen.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tzhen.mooc.R;
import com.tzhen.mooc.countrylist.widget.ContactListAdapter;
import com.tzhen.mooc.countrylist.widget.ItemInterface;

import java.util.List;

/**
 * Created by wuyong on 16/12/30.
 */
public class ContactsAdapter extends ContactListAdapter {
    private Context context;
    public ContactsAdapter(Context _context, int _resource, List<ItemInterface> _items) {
        super(_context, _resource, _items);
        this.context = _context;
    }

    public void populateDataForRow(View parentView, ItemInterface item,
                                   int position)
    {
        View infoView = parentView.findViewById(R.id.infoRowContainer);
        TextView nicknameView = (TextView) infoView
                .findViewById(R.id.cityName);
        ImageView ivHeader = (ImageView) infoView.findViewById(R.id.iv_header);

        Glide.with(context).load(item.getUserInfo().getHead()).into(ivHeader);
        nicknameView.setText(item.getUserInfo().getName());
    }
}
