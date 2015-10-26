package com.pass.self;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.pass.self.slideview.widget.SlideCutListView;
import com.pass.self.slideview.widget.SlideCutListView.RemoveDirection;
import com.pass.self.slideview.widget.SlideCutListView.RemoveListener;

/**
 * ListView滑动删除
 * @author leker
 *
 */
public class SlideDeleteActivity extends Activity implements RemoveListener{
	
	private SlideCutListView slideCutListView ;  
    private ArrayAdapter<String> adapter;  
    private List<String> dataSourceList = new ArrayList<String>();  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slide_delete);
		init();
	}
	
	private void init(){
		slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);  
        slideCutListView.setRemoveListener(this);  
          
        for(int i=0; i<20; i++){  
            dataSourceList.add("滑动删除" + i);   
        }  
          
        adapter = new ArrayAdapter<String>(this, R.layout.slide_delete_item, R.id.list_item, dataSourceList);  
        slideCutListView.setAdapter(adapter);  
          
        slideCutListView.setOnItemClickListener(new OnItemClickListener() {  
  
            @Override  
            public void onItemClick(AdapterView<?> parent, View view,  
                    int position, long id) {  
                Toast.makeText(SlideDeleteActivity.this, dataSourceList.get(position), Toast.LENGTH_SHORT).show();  
            }  
        });  
    }

	@Override
	public void removeItem(RemoveDirection direction, int position) {
		// TODO Auto-generated method stub
		adapter.remove(adapter.getItem(position));  
        switch (direction) {  
        case RIGHT:  
            Toast.makeText(this, "向右删除  "+ position, Toast.LENGTH_SHORT).show();  
            break;  
        case LEFT:  
            Toast.makeText(this, "向左删除  "+ position, Toast.LENGTH_SHORT).show();  
            break;  
  
        default:  
            break;  
        }
	}
}
