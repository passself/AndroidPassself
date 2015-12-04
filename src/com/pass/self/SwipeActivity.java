package com.pass.self;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SwipeActivity extends SwipeBaseActivity {
	private List<String> list = new ArrayList<String>();  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swipe_layout);
		setSwipeAnyWhere(true);
		initData();
	}
	
	private void initData(){
		for (int i = 0; i <= 30; i++) {  
            list.add("测试数据" + i);  
        }  
  
        ListView mListView = (ListView) findViewById(R.id.listView);  
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(  
        		SwipeActivity.this, android.R.layout.simple_list_item_1, list);  
        mListView.setAdapter(adapter); 
	}
}
