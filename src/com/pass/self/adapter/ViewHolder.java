package com.pass.self.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 封装万能的ViewHolder和Adapter
 * 引用 http://blog.csdn.net/lmj623565791/article/details/38902805
 * @author leker
 *
 */
public class ViewHolder {
	private final SparseArray<View> mViews;
	
	private View mConvertView;
	
	private ViewHolder(Context context,ViewGroup parent,int layoutId,int position) {
		// TODO Auto-generated constructor stub
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		
		//setTag
		mConvertView.setTag(this);
	}
	
	public static ViewHolder get(Context context,View contentView,ViewGroup parent,int layoutId,int position){
		if(contentView==null){
			return new ViewHolder(context, parent, layoutId, position);
		}
		
		return (ViewHolder)contentView.getTag();
	}
	
	/**
	 * 通过控件ID获取对应的控件，如果没有则加入views
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if(view ==null){
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}
	
	public View getContentView(){
		return mConvertView;
	}
}
