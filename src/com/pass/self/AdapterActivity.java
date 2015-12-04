package com.pass.self;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pass.self.adapter.CommonAdapter;
import com.pass.self.adapter.ViewHolder;

public class AdapterActivity extends Activity {
	
	ListView mListView;
	private List<String> mDatas = new ArrayList<String>(Arrays.asList("Hello","World","Welcome"));
	CommonAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adapter_list_layout);
		mListView = (ListView)findViewById(R.id.id_lv_listview);
		
		mListView.setAdapter(adapter = new CommonAdapter<String>(getApplicationContext(), mDatas, R.layout.adapter_layout) {

			@Override
			public void convert(ViewHolder helper,String item) {
				// TODO Auto-generated method stub
				TextView view = helper.getView(R.id.id_tv_title);  
                view.setText(item); 
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), SwipeActivity.class);
				startActivity(intent);
			}
		});
	}
}
