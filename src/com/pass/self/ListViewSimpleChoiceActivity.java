package com.pass.self;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pass.self.bean.Brand;
import com.pass.self.utils.IpEditText;

public class ListViewSimpleChoiceActivity extends Activity implements OnClickListener{
	
	Context mContext;
	
	private ListView mListView; //首页的ListView
    private List<Brand> namesList; //用于装载数据的集合
    private int selectPosition = -1;//用于记录用户选择的变量
    private Brand selectBrand; //用户选择的品牌
    
    Button list_btn;
    IpEditText ipEditText;
    ProgressBar head_progressBar;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.main_list_layout);
		initView();
	}
	Dialog dialog;
	private void showDialog(){
		if(dialog==null){
	        dialog = new Dialog(mContext,R.style.Theme_Light_Dialog);
	        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.listview_choice,null);
	        //获得dialog的window窗口
	        Window window = dialog.getWindow();
	        //设置dialog在屏幕底部
	        window.setGravity(Gravity.BOTTOM);
	        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
	        window.setWindowAnimations(R.style.wifi_pop_dialog_style);
	        window.getDecorView().setPadding(0, 0, 0, 0);
	        //获得window窗口的属性
	        android.view.WindowManager.LayoutParams lp = window.getAttributes();
	        //设置窗口宽度为充满全屏
	        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	        //设置窗口高度为包裹内容
	        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
	        //将设置好的属性set回去
	        window.setAttributes(lp);
	        //将自定义布局加载到dialog上
	        dialog.setContentView(dialogView);
	        mListView = (ListView)dialogView.findViewById(R.id.list_choice_view);
	        initDatas();
		}
		dialog.show();
	}
	
	
	private void initView(){
		list_btn = (Button)findViewById(R.id.list_btn);
		list_btn.setOnClickListener(this);
		ipEditText = (IpEditText)findViewById(R.id.ip_edit_text);
		
		head_progressBar = (ProgressBar)findViewById(R.id.index_header_sync_progressBar);
		head_progressBar.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.progress_loading_anim));
    }

    private void initDatas(){
        //初始化ListView适配器的数据
        namesList = new ArrayList<>();
        Brand brand0 = new Brand("apple");
        Brand brand1 = new Brand("sony");
        namesList.add(brand0);
        namesList.add(brand1);
        final MyAdapter myAdapter = new MyAdapter(this,namesList);
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                selectPosition = position;
                myAdapter.notifyDataSetChanged();
                selectBrand = namesList.get(position);
                Toast.makeText(ListViewSimpleChoiceActivity.this,"您选中的手机品牌是："+selectBrand.getBandname(),Toast.LENGTH_SHORT).show();
                if(dialog.isShowing()){
                	dialog.dismiss();
                }
            }
        });
    }

    public class MyAdapter extends BaseAdapter{
        Context context;
        List<Brand> brandsList;
        LayoutInflater mInflater;
        public MyAdapter(Context context,List<Brand> mList){
            this.context = context;
            this.brandsList = mList;
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return brandsList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.list_choice_item_layout,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView)convertView.findViewById(R.id.id_name);
                viewHolder.select = (ImageView)convertView.findViewById(R.id.id_select);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.name.setText(brandsList.get(position).getBandname());
            if(selectPosition == position){
                //viewHolder.select.setChecked(true);
            	viewHolder.select.setVisibility(View.VISIBLE);
            }
            else{
                //viewHolder.select.setChecked(false);
            	viewHolder.select.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }
    }
    public class ViewHolder{
        TextView name;
        ImageView select;
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(list_btn==v){
			String ip = ipEditText.getText().toString().trim();
			Toast.makeText(mContext, ipEditText.checkIP(ip) ? "IP地址合法":"IP地址不合法", 1).show();
			//showDialog();
		}
	}
}
