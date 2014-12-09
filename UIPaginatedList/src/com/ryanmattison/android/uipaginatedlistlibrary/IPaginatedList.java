/*******************************************************************************
 * Copyright 2010 Ryan Mattison -  rmattison1@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.ryanmattison.android.uipaginatedlistlibrary;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * IPaginatedList - manages the complexity of a dynamic paginated list
 * 
 * @author rmattison1@gmail.com
 */
public class IPaginatedList extends ArrayList<IPaginatedBusinessObject>
{

	private static final long serialVersionUID = 1L;

	private static final String TAG = "IPaginatedList";

	private int _pageSize = 0;

	public IPaginatedList()
	{

	}

	public void setPageSize(int pageSize)
	{
		_pageSize = pageSize;
	}

	public boolean addToBack(List<IPaginatedBusinessObject> paginatedBusinessObjectList, boolean moreToLoad, int key)
	{

		IPaginatedBusinessObject lastItem = getLast();
		if (lastItem != null && (lastItem.isLoadDialog() || lastItem.isLoading()))
		{
			deleteLastItem();
		}

		super.addAll(paginatedBusinessObjectList);
		if (moreToLoad)
		{
			Log.d(TAG, "More to load was true");
			super.add(getNewLoadDialog(key));
		}

		Log.d(TAG, "More to load was false");

		return true;
	}

	public void removeIsLoadingItem()
	{
		IPaginatedBusinessObject lastItem = getLast();
		if (lastItem != null && (lastItem.isLoadDialog() || lastItem.isLoading()))
		{
			deleteLastItem();
		}
	}

	public void addIsLoadingItem()
	{
		super.add(getNewLoadingDialog(0));
	}

	private int getActualEndNode()
	{
		return size() - 1;
	}

	private void deleteLastItem()
	{
		this.remove(getActualEndNode());
	}

	private IPaginatedBusinessObject getLast()
	{
		if (!this.isEmpty())
		{
			return this.get(getActualEndNode());
		}
		else
		{
			return null;
		}
	}

	private IPaginatedBusinessObject getNewLoadDialog(final int key)
	{
		IPaginatedBusinessObject current = new IPaginatedBusinessObject()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 991701485956390422L;

			@Override
			public boolean isLoadDialog()
			{
				return true;
			}

			@Override
			public int keyToLoad()
			{
				return key;
			}

		};
		return current;
	}

	private IPaginatedBusinessObject getNewLoadingDialog(final int key)
	{
		IPaginatedBusinessObject current = new IPaginatedBusinessObject()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -6016355926883718804L;

			@Override
			public boolean isLoadDialog()
			{
				return true;
			}

			@Override
			public boolean isLoading()
			{
				return true;
			}

			@Override
			public int keyToLoad()
			{
				return key;
			}

		};
		return current;
	}

	public int getPageSize()
	{
		return _pageSize;
	}

}
