package com.tzhen.mooc.countrylist.widget;

import com.tongzhen.mooc.entities.CountryInfo;
import com.tongzhen.mooc.entities.UserInfo;

public interface ItemInterface
{
	String getItemForIndex();
	CountryInfo getCountryInfo();
	UserInfo getUserInfo();
}
