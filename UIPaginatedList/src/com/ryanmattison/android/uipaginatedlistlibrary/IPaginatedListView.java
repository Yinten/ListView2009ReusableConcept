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

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * IPaginatedListView - list view with some over rides to support dynamic
 * loading
 * 
 * @author rmattison1@gmail.com
 */
public class IPaginatedListView extends ListView
{

	private static final String TAG = "IPaginatedListView";
	private boolean _isLoading = false;
	private int _side = IPaginatedAdapter.BOTTOM;

	public IPaginatedListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void handleDataChanged()
	{

		super.handleDataChanged();

		_isLoading = false;
	}

	/*
	 * @Override protected void handleDataChanged() {
	 * 
	 * int top = getFirstVisiblePosition();
	 * 
	 * int move = top + getPaginationAdapter().getPageSize();
	 * 
	 * while (move > getLastVisiblePosition()) { move--; } move--;
	 * 
	 * if (move < getPaginationAdapter().getCount() && move >= 0) {
	 * setSelection(move); }
	 * 
	 * super.handleDataChanged(); }
	 */

	public IPaginatedListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();

	}

	public IPaginatedListView(Context context)
	{
		super(context);
		init();
	}

	public boolean isFirstItemVisible()
	{
		if (getPaginationAdapter() != null && getPaginationAdapter().getCount() > 0)
		{
			return getFirstVisiblePosition() == 0;
		}
		return false;
	}

	public boolean isLastItemVisible()
	{
		if (getPaginationAdapter() != null && getPaginationAdapter().getCount() > 0)
		{
			Log.v(TAG, "getPaginationAdapter().getCount(): " + getPaginationAdapter().getCount());
			Log.v(TAG, "getLastVisiblePosition(): " + getLastVisiblePosition());
			return (getPaginationAdapter().getCount() - 1 <= getLastVisiblePosition());
		}

		return false;
	}

	private void init()
	{

		super.setOnItemClickListener(new OnItemClickListener()
		{

			public void onItemClick(AdapterView<?> av, View v, int pos, long id)
			{
				Log.d(TAG, "OnPaginationASelected");
				onPaginationSelected(av, v, pos, id);
			}
		});

		this.setOnScrollListener(new OnScrollListener()
		{

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				shouldLoad();
			}

			public void onScrollStateChanged(AbsListView view, int scrollState)
			{

			}
		});
	}

	private boolean shouldLoad()
	{
		if (getPaginationAdapter() != null)
		{
			if (!_isLoading && getPaginationAdapter().getCount() != 1)
			{
				// Can't use AndroidLog because module dependencies.
				Log.v(TAG, "Is Last Item Visible: " + isLastItemVisible());
				if (isLastItemVisible())
				{
					_side = IPaginatedAdapter.BOTTOM;
					_isLoading = true;
					getPaginationAdapter().startLoad(_side);
				}
			}
		}

		return false;
	}

	private boolean onPaginationSelected(AdapterView<?> av, View v, int pos, long id)
	{
		Log.d(TAG, "onPaginationSelected !");
		IPaginatedBusinessObject pvo = (IPaginatedBusinessObject) getPaginationAdapter().getItem(pos);

		if (!getPaginationAdapter().getLoadDynamically() && pvo.isLoadDialog() && !pvo.isLoading())
		{
			Log.d(TAG, "onPaginationSelected Start Load!");
			getPaginationAdapter().startLoad(_side);
			return true;
		}

		if (pvo.isLoadDialog() || pvo.isLoading())
		{
			Log.d(TAG, "onPaginationSelected Is Loaded or Loading!");
			return true;
		}

		return false;
	}

	@Override
	public void setAdapter(ListAdapter adapter)
	{
		super.setAdapter(adapter);
		this.setTranscriptMode(TRANSCRIPT_MODE_DISABLED);

	}

	public IPaginatedAdapter getPaginationAdapter()
	{
		return (IPaginatedAdapter) super.getAdapter();
	}

	@Override
	public void setOnItemClickListener(final OnItemClickListener listener)
	{
		super.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> av, View v, int pos, long id)
			{
				boolean isPageItem = onPaginationSelected(av, v, pos, id);
				if (!isPageItem)
				{
					listener.onItemClick(av, v, pos, id);
				}
			}

		});
	}

	public interface ItemVisibilityListener
	{
		public boolean isItemVisible();
	}

}
