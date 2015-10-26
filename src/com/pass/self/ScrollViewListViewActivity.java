package com.pass.self;

import java.util.List;

import com.pass.self.slideview.data.DataUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * ScrollView 内listview全部展开
 * @author leker
 *
 */
public class ScrollViewListViewActivity extends Activity {
	String TAG = this.getClass().getName();

	ListView listView;

	ArrayAdapter<String> adapter;
	
	public void freshListView(){
		int totalHeight = 0;
		for(int i = 0;i<adapter.getCount();i++){
			View itemView = adapter.getView(i, null, listView);
			itemView.measure(0, 0);
			totalHeight += itemView.getMeasuredHeight();
		}
		ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
		layoutParams.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() -1));
		listView.setLayoutParams(layoutParams);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scrollview_listview_layout);

		listView = (ListView) findViewById(R.id.scroll_listview);
		init();
	}

	private void init() {
		List<String> list = DataUtil.getInstance().getList(20);
		// Log.i(TAG, "list.size() is:"+list.size());
		adapter = new ArrayAdapter<String>(this, R.layout.slide_delete_item,
				R.id.list_item, list);
		listView.setAdapter(adapter);
		setListViewHeightBaseOnChildren(listView);
	}

	public void setListViewHeightBaseOnChildren(ListView listView) {
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listView.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
