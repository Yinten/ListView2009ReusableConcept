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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * IPaginatedDataSource - Datasource to manage the list & views
 * 
 * @author rmattison1@gmail.com
 */
public abstract class IPaginatedDataSource implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6467395217705297102L;
	private static final String TAG = "IPaginatedDataSource";

	public void saveState(String key, Bundle bundle)
	{
		Bundle b;
		if ((b = bundle.getBundle(key)) == null)
			b = new Bundle();

		b.putInt("curPage", getCurrentLoadingPage());
		b.putInt("numberOfPages", getNumOfPages());
		b.putSerializable("curList", getItems());
		b.putInt("pageSize", getPageSize());
		b.putInt("curPosition", getCurPosition());
		b.putParcelable("xItemState", getxItemState());
		b.putInt("xListPosition", getxListPosition());
		b.putInt("xItemPosition", getxItemPosition());

		bundle.putBundle(key, b);
	}

	public void restoreState(String key, Bundle bundle)
	{
		if (bundle == null || !bundle.containsKey(key))
		{
			return; 
		}
		Bundle b;
		if ((b = bundle.getBundle(key)) == null)
			return;

		if (b.containsKey("xListPosition"))
			setxListPosition(b.getInt("xListPosition"));

		if (b.containsKey("xItemPosition"))
			setxItemPosition(b.getInt("xItemPosition"));

		if (b.containsKey("xItemState"))
			setxItemState(b.getParcelable("xItemState"));
		setCurrentLoadingPage(b.getInt("curPage"));
		setNumOfPages(b.getInt("numberOfPages"));
		setPageSize(b.getInt("pageSize"));
		setCurPosition(b.getInt("curPosition"));
		ArrayList<IPaginatedBusinessObject> list = (ArrayList<IPaginatedBusinessObject>) b.getSerializable("curList");
		_pagedList = new IPaginatedList();
		_pagedList.addAll(list);
		_pagedList.setPageSize(getPageSize());
	}

	public static boolean hasRestorableState(String key, Bundle b)
	{
		if (b == null)
		{
			return false;
		}
		if (b.getBundle(key) == null)
		{
			return false;
		}

		return true;
	}

	private int xListPosition;
	private int xItemPosition;
	private Parcelable xItemState;

	public int getxListPosition()
	{
		return xListPosition;
	}

	public void setxListPosition(int xListPosition)
	{
		this.xListPosition = xListPosition;
	}

	public int getxItemPosition()
	{
		return xItemPosition;
	}

	public void setxItemPosition(int xItemPosition)
	{
		this.xItemPosition = xItemPosition;
	}

	public Parcelable getxItemState()
	{
		return xItemState;
	}

	public void setxItemState(Parcelable xItemState)
	{
		this.xItemState = xItemState;
	}

	private int curPosition = 0;

	public int getCurPosition()
	{
		return curPosition;
	}

	public void setCurPosition(int curPosition)
	{
		this.curPosition = curPosition;
	}

	private IPaginatedList _pagedList;
	private Context _context;
	private LayoutInflater _layoutInflater;
	private View _loading;
	private List<IPaginatedBusinessObject> _tempBOs;
	private int _curPage, _numOfPages, _pageSize = 0;

	public IPaginatedDataSource(Context context)
	{
		Log.i(TAG, "IPaginatedDataSource");
		_context = context;
		_layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_loading = _layoutInflater.inflate(R.layout.last_row, null);

	}

	public IPaginatedDataSource(Context context, int pageSize)
	{
		_context = context;
		_layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_loading = _layoutInflater.inflate(R.layout.last_row, null);
		_pagedList = new IPaginatedList();
		this._pageSize = pageSize;
		_pagedList.setPageSize(pageSize);

	}

	public int getPageSize()
	{
		return _pageSize;
	}

	public void setPageSize(int _pageSize)
	{
		this._pageSize = _pageSize;
	}

	public IPaginatedList getItems()
	{
		return _pagedList;
	}

	public int getCurrentLoadingPage()
	{
		return _curPage;
	}

	protected void setCurrentLoadingPage(int curPage)
	{
		_curPage = curPage;
	}

	public int getNumOfPages()
	{
		return _numOfPages;
	}

	protected void setNumOfPages(int numOfPages)
	{
		_numOfPages = numOfPages;
	}

	protected View getLoadingRow()
	{
		return _loading;
	}

	protected Context getContext()
	{
		return _context;
	}

	protected LayoutInflater getLayoutInflater()
	{
		return _layoutInflater;
	}

	protected void addToTempBOStore(IPaginatedBusinessObject pbo)
	{
		if (_tempBOs == null)
		{
			_tempBOs = new ArrayList<IPaginatedBusinessObject>();
		}

		_tempBOs.add(pbo);
	}

	protected List<IPaginatedBusinessObject> getTempStore()
	{
		if (_tempBOs == null)
		{
			_tempBOs = new ArrayList<IPaginatedBusinessObject>();
		}
		return _tempBOs;
	}

	protected void clearTempStore()
	{
		if (_tempBOs == null)
		{
			_tempBOs = new ArrayList<IPaginatedBusinessObject>();
		}
		_tempBOs.clear();
	}

	protected void finishedParsing()
	{

		if (getItems() != null && getItems().size() > 0)
		{
			if (getItems().get(getItems().size() - 1) != null && getItems().get(getItems().size() - 1).isLoading())
			{
				getItems().remove(getItems().size() - 1);
			}
		}
		if (getItems() != null && getItems().size() > 0)
		{
			if (getItems().get(0) != null && getItems().get(0).isLoading())
			{
				getItems().remove(0);
			}
		}

		if (getTempStore().size() > 0)
		{
			boolean addDummy = false;

			if (getCurrentLoadingPage() < getNumOfPages())
			{
				addDummy = true;
			}

			if (getTempStore().size() < _pagedList.getPageSize())
			{
				addDummy = false;
			}

			if (getItems() != null)
			{
				getItems().addToBack(getTempStore(), addDummy, getCurrentLoadingPage() + 1);
			}
			_curPage = getCurrentLoadingPage();
		}
		clearTempStore();
	}

	public void load(final Handler dataSetHandler, final int keyToLoad)
	{
		makeCall(keyToLoad, new IPageComplete()
		{
			@Override
			public void onWebServiceComplete()
			{
				if (_context != null && !((Activity) _context).isFinishing())
				{
					dataSetHandler.sendEmptyMessage(0);
				}
			}
		});
	}

	public void add(IPaginatedBusinessObject ipbo)
	{
		ipbo.setKey(getCurrentLoadingPage());
		addToTempBOStore(ipbo);
	}

	public void addTemporaryLoadingObject()
	{
		add(new IPaginatedBusinessObject()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 7934847909676117741L;

			@Override
			public boolean isLoading()
			{
				return true;
			};
		});
		boolean addDummy = false;
		int key = 0;
		getItems().addToBack(getTempStore(), addDummy, key);
		clearTempStore();

	}

	public abstract View getLoading(ViewGroup parent);

	public abstract View getView(ViewGroup parent);

	public abstract View getSelect(ViewGroup parent);

	public abstract void makeCall(int pageToLoad, IPageComplete iPageComplete);

	public abstract void initializeView(View row);

	public void initializeChildView(View row)
	{

	}

	public View getChildView(ViewGroup parent)
	{
		return null;
	}

	public abstract void setValues(IPaginatedBusinessObject group, View row, int groupPosition);

	public void setChildValues(IPaginatedBusinessObject child, View row, int groupPosition, int childPosition, boolean isLastChild)
	{
		// TODO Auto-generated method stub

	}

}
