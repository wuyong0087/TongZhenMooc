package com.tzhen.mooc.countrylist.model;

import com.tongzhen.mooc.entities.CountryInfo;
import com.tzhen.mooc.countrylist.widget.CountryItemInterface;

public class CountryItem implements CountryItemInterface
{
	private int id;
	private String countryName;
	private String fullName;

	public CountryItem(String countryName, String fullName, int id)
	{
		super();
		this.countryName = countryName;
		this.id = id;
		this.setFullName(fullName);
	}

	@Override
	public String getItemForIndex()
	{
		return fullName;
	}

	@Override
	public CountryInfo getCountryInfo()
	{
		return new CountryInfo.Builder().setId(id).setName(countryName).build();
	}

	public String getCountryName()
	{
		return countryName;
	}

	public void setCountryName(String countryName)
	{
		this.countryName = countryName;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
