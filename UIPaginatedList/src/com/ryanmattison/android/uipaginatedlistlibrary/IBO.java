package com.ryanmattison.android.uipaginatedlistlibrary;

public interface IBO
{
	public boolean isLoadDialog();
	public int keyToLoad();
	public void setIsLoadDialog(boolean isLoadDialog);
	public void setKey(int key);
	public boolean isLoading();
	public void setIsLoading(boolean isLoading);
	int getId();
	void setId(int id);
}
