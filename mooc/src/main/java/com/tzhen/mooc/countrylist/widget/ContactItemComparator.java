package com.tzhen.mooc.countrylist.widget;

import java.util.Comparator;

public class ContactItemComparator implements Comparator<CountryItemInterface>
{

	@Override
	public int compare(CountryItemInterface lhs, CountryItemInterface rhs)
	{
		if (lhs.getItemForIndex() == null || rhs.getItemForIndex() == null)
			return -1;

		return (lhs.getItemForIndex().compareTo(rhs.getItemForIndex()));

	}

}
