package com.tzhen.mooc.countrylist.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tzhen.mooc.R;
import com.tzhen.mooc.countrylist.widget.ItemInterface;
import com.tzhen.mooc.countrylist.widget.ContactListAdapter;

import java.util.List;

public class CityAdapter extends ContactListAdapter
{

	public CityAdapter(Context _context, int _resource,
			List<ItemInterface> _items)
	{
		super(_context, _resource, _items);
	}

	public void populateDataForRow(View parentView, ItemInterface item,
			int position)
	{
		View infoView = parentView.findViewById(R.id.infoRowContainer);
		TextView nicknameView = (TextView) infoView
				.findViewById(R.id.cityName);

		nicknameView.setText(item.getCountryInfo().getName());
	}

}
