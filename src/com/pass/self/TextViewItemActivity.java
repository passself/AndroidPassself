package com.pass.self;


import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextViewItemActivity extends Activity {
	String TAG = this.getClass().getName();
	LinearLayout container;
	
	private boolean isFristTime = true;  
	  
    /** 标签之间的间距 px */  
    final int itemMargins = 50;  
  
    /** 标签的行间距 px */  
    final int lineMargins = 50;  
    
    private String[] tags = { "大约在冬季", "漂洋过海的来看你", "天下有情人", "我很认真", "夜夜夜夜", "想你的夜", "背叛", "趁早", "旧情绵绵", "谁明浪子心", "安妮",  
            "说谎的爱人", "不浪漫的罪名", "不愿一个人", "风吹麦浪" };  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textview_item_layout);
		container = (LinearLayout)findViewById(R.id.container);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus && isFristTime){
			isFristTime = false;
			final int containerWidth = container.getMeasuredWidth() - container.getPaddingLeft() - container.getPaddingRight();
			
			final LayoutInflater inflater = getLayoutInflater();
			
			/*用来测量字符的速度*/
			final Paint paint = new Paint();
			final TextView textView = (TextView)inflater.inflate(R.layout.textview_item, null);
			
			final int itemPadding = textView.getCompoundPaddingLeft() + textView.getCompoundPaddingRight();
			final LinearLayout.LayoutParams tvparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			tvparams.setMargins(0, 0, itemMargins, 0);
			
			paint.setTextSize(textView.getTextSize());
			
			LinearLayout layout = new LinearLayout(this);
			layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			layout.setOrientation(LinearLayout.HORIZONTAL);
			container.addView(layout);
			
			final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, lineMargins, 0, 0);
			
			/**一行剩下的空间**/
			int remainWidth = containerWidth;
			for(int i =0; i< tags.length; ++i){
				Log.i(TAG, "i is:"+i);
				final String text = tags[i];
				final float itemWidth = paint.measureText(text) + itemPadding;
				if(remainWidth>itemWidth){
					addItemView(inflater, layout, tvparams, text);
				}else{
					resetTextViewMarginsRight(layout);
					layout = new LinearLayout(this);
					layout.setLayoutParams(params);
					layout.setOrientation(LinearLayout.HORIZONTAL);
					
					/*---------将前面那一个textView加入新的一行----------*/
					addItemView(inflater, layout, tvparams, text);
					container.addView(layout);
					remainWidth = containerWidth;
				}
				remainWidth = (int)(remainWidth - itemWidth + 0.5f) - itemMargins;
			}
			resetTextViewMarginsRight(layout);
		}
	}

	/***************** 将每行最后一个textview的MarginsRight去掉 *********************************/  
	private void resetTextViewMarginsRight(ViewGroup viewGroup){
		final TextView tempTextView = (TextView)viewGroup.getChildAt(viewGroup.getChildCount() - 1);
		tempTextView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	private void addItemView(LayoutInflater inflater,ViewGroup viewGroup,LayoutParams params,String text){
		TextView tvItem = (TextView)inflater.inflate(R.layout.textview_item, null);
		tvItem.setText(text);
		viewGroup.addView(tvItem, params);
	}
}
