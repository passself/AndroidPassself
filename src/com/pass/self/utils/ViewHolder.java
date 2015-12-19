package com.pass.self.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * 好用的ViewHolder
 * @author shihx
 * @date 2015-12-19
 * @todo TODO
 */
public class ViewHolder {
	/**
	 * 使用方式 ImageView bananaView = ViewHolder.get(convertView, R.id.banana);
	 * @param view
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view,int id){
		SparseArray<View> viewHolder = (SparseArray<View>)view.getTag();
		if(viewHolder == null){
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if(childView == null){
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}
