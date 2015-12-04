package com.pass.self;

import java.util.ArrayList;
import java.util.List;

import com.pass.self.slideview.SlidingFinishLayout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AbsActivity extends Activity {
	private List<String> list = new ArrayList<String>();  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abslistview);
		
		for (int i = 0; i <= 30; i++) {  
            list.add("测试数据" + i);  
        }  
  
        ListView mListView = (ListView) findViewById(R.id.listView);  
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(  
                AbsActivity.this, android.R.layout.simple_list_item_1, list);  
        mListView.setAdapter(adapter); 
        
        SlidingFinishLayout mSlidingFinishLayout = (SlidingFinishLayout)findViewById(R.id.sildingFinishLayout);
        mSlidingFinishLayout.setOnSlidingFinishListener(new SlidingFinishLayout.OnSlidingFinishListener() {
			@Override
			public void onSlidingFinish() {
				// TODO Auto-generated method stub
				AbsActivity.this.finish();
			}
		});
        
        mSlidingFinishLayout.setTouchView(mListView);
	}
}
