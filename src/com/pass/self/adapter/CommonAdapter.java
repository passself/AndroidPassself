package com.pass.self.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected final int mItemLayoutId;
	
	public CommonAdapter(Context context,List<T> datas,int itemLayoutId) {
		// TODO Auto-generated constructor stub
		this.mDatas = datas;
		mContext = context;
		this.mItemLayoutId = itemLayoutId;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder = getViewHolder(position, convertView, parent);
		convert(holder, getItem(position));
		return holder.getContentView();
	}
	
	public abstract void convert(ViewHolder helper,T item);
	
	private ViewHolder getViewHolder(int position,View convertView,ViewGroup parent){
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
	}

}
