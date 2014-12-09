ListView2009ReusableConcept
===========================

This is a Paginated List Control circa 2009 that was under creative commons that has an oddly similar feel to the RecyclerView model....  It supported reusable layouts and non-reusable layouts, onCreateViewHolder, onBindLayout etc

So very glad it's now retired. 

Resuable or nonreusable


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
	
	
	
	
	IPaginatedDataSource
	
	
	
	public abstract View getLoading(ViewGroup parent);

	public abstract View getView(ViewGroup parent);

	public abstract View getSelect(ViewGroup parent);

	public abstract void makeCall(int pageToLoad, IPageComplete iPageComplete);

	public abstract void initializeView(View row);
