package com.ryanmattison.android.uipaginatedlistlibrary.IExpandable;

import android.view.View;
import android.view.ViewGroup;

import com.ryanmattison.android.uipaginatedlistlibrary.IPaginatedBusinessObject;

public interface IExpandableExtensionMethods
{
	public void initializeChildView(View row);

	public void setChildValues(IPaginatedBusinessObject iPaginatedBusinessObject, View row, int groupPosition, int childPosition, boolean isLastChild);

	public View getChildView(ViewGroup parent);
	
	public View makeChildCall(int parentPos);

}
