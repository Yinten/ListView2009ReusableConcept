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
package com.ryanmattison.android.uipaginatedlistlibrary.IExpandable;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.ryanmattison.android.uipaginatedlistlibrary.IPaginatedBusinessObject;
import com.ryanmattison.android.uipaginatedlistlibrary.IPaginatedDataSource;

public class IExpandablePaginatedAdapter extends BaseExpandableListAdapter
{
	private boolean _blockListeners = false; 
	private IPaginatedDataSource _ds;
	private boolean _loadDynamically = false;
	


	public IExpandablePaginatedAdapter(IPaginatedDataSource ds)
	{
		super();
		init(ds, false);
	}
	
	public boolean IsBlocked()
	{
		return _blockListeners;
	}


	public IExpandablePaginatedAdapter(IPaginatedDataSource ds, boolean loadDynamically)
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

	@Override
	public int getGroupCount()
	{
		return getDataSource().getItems().size();
	}

	// TODO this could be enhance significantly be overriding and keeping size
	// in parent.
	@Override
	public int getChildrenCount(int groupPosition)
	{
		if(getDataSource() == null) return 0; 
		if(getDataSource().getItems() == null) return 0; 
		if(getDataSource().getItems().get(groupPosition) == null) return 0; 
		if(getDataSource().getItems().get(groupPosition).getChildren() == null) return 0;

		return getDataSource().getItems().get(groupPosition).getChildren().size();
		
	}

	@Override
	public IPaginatedBusinessObject getGroup(int groupPosition)
	{
		return getDataSource().getItems().get(groupPosition);
	}

	@Override
	public IPaginatedBusinessObject getChild(int groupPosition, int childPosition)
	{
		return getDataSource().getItems().get(groupPosition).getChildren().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return getGroup(groupPosition).getGroupId();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return getChild(groupPosition, childPosition).getId();
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		View row = convertView;

		if (getGroup(groupPosition).isLoading())
		{
			row = _ds.getLoading(parent);
			row.setTag("nonreusable");
		}
		else if (getGroup(groupPosition).isLoadDialog())
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
			// TODO initializeView with ViewGroup
			_ds.initializeView(row);
			_ds.setValues(getGroup(groupPosition), row, groupPosition);
		}
		else
		{
			_ds.setValues(getGroup(groupPosition), row, groupPosition);
		}

		return row;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		View row = convertView;

		if (getChild(groupPosition, childPosition).isLoading())
		{
			row = _ds.getLoading(parent);
			row.setTag("nonreusable");
		}
		else if (getChild(groupPosition, childPosition).isLoadDialog())
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
			row = _ds.getChildView(parent);
			_ds.initializeChildView(row);
			_ds.setChildValues(getChild(groupPosition, childPosition), row,  groupPosition,  childPosition,  isLastChild);
		}
		else
		{
			_ds.setChildValues(getChild(groupPosition, childPosition), row,  groupPosition,  childPosition,  isLastChild);
		}

		return row;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}
	
	
	public void notifyDataSetChanged(ExpandableListView elv)
	{
		_blockListeners = true;
		super.notifyDataSetChanged();
		if (elv != null)
		{
			elv.post(new Runnable()
			{
				public void run()
				{
					_blockListeners = false;
				}
			});
		}
	}

}
