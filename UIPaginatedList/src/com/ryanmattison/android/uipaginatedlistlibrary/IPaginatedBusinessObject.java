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

/**
 * IPaginatedBusinessObject - Business Object needed to support headers,
 * footers, loading, and view content management
 * 
 * @author rmattison1@gmail.com
 */
public abstract class IPaginatedBusinessObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8389123703529642133L;
	private boolean _isLoadDialog, _isLoading;
	private int _key, _id;
	private int groupId;
	private String groupWSId;
	private boolean _isExpanded; 
	public boolean isExpanded()
	{
		return _isExpanded;
	}

	public void setExpanded(boolean isExpanded)
	{
		this._isExpanded = isExpanded;
	}

	private ArrayList<IPaginatedBusinessObject> children;

	public boolean isLoadDialog()
	{
		return _isLoadDialog;
	}

	public int keyToLoad()
	{
		return _key;
	}

	public void setIsLoadDialog(boolean isLoadDialog)
	{
		_isLoadDialog = isLoadDialog;
	}

	public void setKey(int key)
	{
		_key = key;
	}

	public boolean isLoading()
	{

		return _isLoading;
	}

	public void setIsLoading(boolean isLoading)
	{
		_isLoading = isLoading;

	}

	public int getId()
	{
		return _id;
	}

	public void setId(int id)
	{
		_id = id;
	}

	public int getGroupId()
	{
		return groupId;
	}

	public void setGroupId(int groupId)
	{
		this.groupId = groupId;
	}

	public String getGroupWSId()
	{
		return groupWSId;
	}

	public void setGroupWSId(String groupWSId)
	{
		this.groupWSId = groupWSId;
	}

	public ArrayList<IPaginatedBusinessObject> getChildren()
	{
		return children;
	}

	public void setChildren(ArrayList<IPaginatedBusinessObject> children)
	{
		this.children = children;
	}

}
