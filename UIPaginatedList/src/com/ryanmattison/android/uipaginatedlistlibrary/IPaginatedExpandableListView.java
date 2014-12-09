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
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

import com.ryanmattison.android.uipaginatedlistlibrary.IExpandable.IExpandablePaginatedAdapter;

/**
 * IPaginatedListView - list view with some over rides to support dynamic
 * loading
 * 
 * @author rmattison1@gmail.com
 */
public class IPaginatedExpandableListView extends ExpandableListView
{

	@SuppressWarnings("unused")
	private static final String TAG = "IPaginatedExpandableListView.java";
	private boolean _isLoading = false;
	private int _side = IPaginatedAdapter.BOTTOM;

	public IPaginatedExpandableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();

	}

	public IPaginatedExpandableListView(Context context)
	{
		super(context);
		init();
	}

	public IPaginatedExpandableListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public void saveMe(String key, Bundle bundle)
	{
		View itemView = getChildAt(0);
		
		Parcelable mListState = this.onSaveInstanceState();
		if(getPaginationAdapter() != null && getPaginationAdapter().getDataSource() != null)
		{
			getPaginationAdapter().getDataSource().setxItemState(mListState);
			getPaginationAdapter().getDataSource().setxItemPosition(itemView == null ? 0 : itemView.getTop());
			getPaginationAdapter().getDataSource().setxListPosition(getFirstVisiblePosition());
			getPaginationAdapter().getDataSource().saveState(key, bundle);
		}
	}
	
	public void restoreMe(String key, Bundle state)
	{
		
		if(getPaginationAdapter() != null && getPaginationAdapter().getDataSource() != null)
		{
			onRestoreInstanceState(getPaginationAdapter().getDataSource().getxItemState());
			this.setSelectionFromTop(getPaginationAdapter().getDataSource().getxListPosition(), getPaginationAdapter().getDataSource().getxItemPosition());
		}
		
	}
	
	@Override
	protected void handleDataChanged()
	{

		if (_isLoading)
		{
			int top = getFirstVisiblePosition();

			int move = 0;
			if (_side == IPaginatedAdapter.BOTTOM)
			{
				move = top - getPaginationAdapter().getPageSize();

			}
			else if (_side == IPaginatedAdapter.TOP)
			{
				move = getPaginationAdapter().getPageSize() + top;
			}
			if (move < getPaginationAdapter().getGroupCount() && move >= 0)
			{
				setSelection(move);

			}
		}
		else
		{
			if (getPaginationAdapter() != null && getPaginationAdapter().getDataSource() != null && getPaginationAdapter().getDataSource().getCurPosition() > 0 && getPaginationAdapter().getDataSource().getCurPosition() < getPaginationAdapter().getDataSource().getItems().size())
			{

				setSelection(getPaginationAdapter().getDataSource().getCurPosition());
			}
		}

		super.handleDataChanged();

		_isLoading = false;
	}

	private void init()
	{

	

		this.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				if (getPaginationAdapter() != null && getPaginationAdapter().getDataSource() != null)
				{
					getPaginationAdapter().getDataSource().setCurPosition(firstVisibleItem);
				}

			}

			
		});

	}

	

	@Override
	public void setAdapter(ListAdapter adapter)
	{
		super.setAdapter(adapter);
		this.setTranscriptMode(TRANSCRIPT_MODE_DISABLED);

	}

	public IExpandablePaginatedAdapter getPaginationAdapter()
	{
		return (IExpandablePaginatedAdapter) super.getExpandableListAdapter();
	}

	

	

	

	public interface ItemVisibilityListener
	{
		public boolean isItemVisible();
	}

}
