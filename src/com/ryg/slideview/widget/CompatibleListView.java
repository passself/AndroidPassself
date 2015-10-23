package com.ryg.slideview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 在ScrollView 里的listView正常显示
 * @author shihx1
 *
 */
public class CompatibleListView extends ListView {
	
	public CompatibleListView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}
	
	public CompatibleListView(Context context, AttributeSet attrs
			) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	

	public CompatibleListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
