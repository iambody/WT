/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-11 下午2:41:27
 * 
 */

package io.vtown.WeiTangApp.comment.view.address.widget.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * Wheel items adapter interface
 */
public interface WheelViewAdapter {
	 
	public int getItemsCount();
	
	 
	public View getItem(int index, View convertView, ViewGroup parent);

	 
	public View getEmptyItem(View convertView, ViewGroup parent);
	
	/**
	 * Register an observer that is called when changes happen to the data used by this adapter.
	 * @param observer the observer to be registered
	 */
	public void registerDataSetObserver(DataSetObserver observer);
	
	/**
	 * Unregister an observer that has previously been registered
	 * @param observer the observer to be unregistered
	 */
	void unregisterDataSetObserver (DataSetObserver observer);
}
