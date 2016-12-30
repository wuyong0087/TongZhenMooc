package com.tzhen.mooc.countrylist.widget;

import java.util.Comparator;

public class ContactItemComparator implements Comparator<ItemInterface>
{

	@Override
	public int compare(ItemInterface lhs, ItemInterface rhs)
	{
		if (lhs.getItemForIndex() == null || rhs.getItemForIndex() == null)
			return -1;

		return (lhs.getItemForIndex().compareTo(rhs.getItemForIndex()));

	}

}
