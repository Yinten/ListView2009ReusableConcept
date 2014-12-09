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

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * IPaginatedAdapter - Adapter to interact with user for loading and removing
 * data
 * 
 * @author rmattison1@gmail.com
 */
public class IPaginatedAdapter extends BaseAdapter
{

	private IPaginatedDataSource _ds;
	private boolean _loadDynamically = true;
	public static final int BOTTOM = 22;
	public static final int TOP = 44;
	private static final String TAG = "IPaginatedAdapter";

	

	
	public IPaginatedAdapter(IPaginatedDataSource ds)
	{
		super();
		init(ds, true);
	}

	public boolean hasHeader()
	{
		return false;
	}

	public IPaginatedAdapter(IPaginatedDataSource ds, boolean loadDynamically)
	{
		super();
		init(ds, loadDynamically);
	}

	private void init(IPaginatedDataSource ds, boolean loadDynamically)
	{
		_ds = ds;
		_loadDynamically = loadDynamically;
	}

	public int getPageSize()
	{
		return _ds.getItems().getPageSize();
	}

	public boolean getLoadDynamically()
	{
		return _loadDynamically;
	}

	public IPaginatedDataSource getDataSource()
	{
		return _ds;
	}

	public IPaginatedBusinessObject getFirstItem()
	{

		return (IPaginatedBusinessObject) getItem(0);
	}

	public IPaginatedBusinessObject getLastItem()
	{
		return (IPaginatedBusinessObject) getItem(getCount() - 1);
	}

	private int getKeyToLoad()
	{

		return getLastItem().keyToLoad();

	}

	private void setItemLoading()
	{

		getLastItem().setIsLoading(true);

	}

	public boolean startLoad(int side)
	{

		if (shouldLoadSide())
		{
			setItemLoading();
			//notifyDataSetChanged();
			Handler _dataSetHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					super.handleMessage(msg);
					notifyDataSetChanged();
				}
			};
			Log.i(TAG, "Key To Load: " + getKeyToLoad()); 
			_ds.load(_dataSetHandler, getKeyToLoad());

			return true;
		}
		return false;
	}

	private boolean shouldLoadSide()
	{

		return getLastItem().isLoadDialog();

	}

	public int getCount()
	{
		return _ds.getItems().size();
	}

	public IPaginatedBusinessObject getItem(int position)
	{
		return _ds.getItems().get(position);
	}

	public long getItemId(int position)
	{
		return getItem(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{

		View row = convertView;
		
		if (getItem(position).isLoading())
		{
			row = _ds.getLoading(parent);
			row.setTag("nonreusable");
		}
		else if (getItem(position).isLoadDialog())
		{
			if (_loadDynamically)
			{
				row = _ds.getLoading(parent);
			}
			else
			{
				row = _ds.getSelect(parent);
			}
			row.setTag("nonreusable");
		}
		else if (row == null || (row.getTag() instanceof String && row.getTag().toString().equalsIgnoreCase("nonreusable")))
		{
			row = _ds.getView(parent);
			_ds.initializeView(row);
			_ds.setValues(getItem(position), row, position);
		}
		else
		{
			_ds.setValues(getItem(position), row, position);
		}

		return row;
	}

}
