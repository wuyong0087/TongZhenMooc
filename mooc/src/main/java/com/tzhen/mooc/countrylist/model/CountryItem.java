package com.tzhen.mooc.countrylist.model;

import com.tongzhen.mooc.entities.CountryInfo;
import com.tongzhen.mooc.entities.UserInfo;
import com.tzhen.mooc.countrylist.widget.ItemInterface;

public class CountryItem implements ItemInterface
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

	@Override
	public UserInfo getUserInfo() {
		return null;
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
