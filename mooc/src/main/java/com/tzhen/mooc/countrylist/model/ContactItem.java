package com.tzhen.mooc.countrylist.model;

import com.tongzhen.mooc.entities.CountryInfo;
import com.tongzhen.mooc.entities.UserInfo;
import com.tzhen.mooc.countrylist.widget.ItemInterface;

public class ContactItem implements ItemInterface
{
	private String uid;
	private String userName;
	private String fullName;
	private String header;

	public ContactItem(String countryName, String fullName, String uid, String header)
	{
		super();
		this.userName = countryName;
		this.uid = uid;
		this.setFullName(fullName);
		this.header = header;
	}

	@Override
	public String getItemForIndex()
	{
		return fullName;
	}

	@Override
	public CountryInfo getCountryInfo()
	{
		return null;
	}

	@Override
	public UserInfo getUserInfo() {
		return new UserInfo.Builder().setUid(uid).setName(userName).setHead(header).build();
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
