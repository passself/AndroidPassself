package com.pass.self;

import java.util.concurrent.ThreadPoolExecutor;

import com.pass.self.kprogresshud.KProgressHUD;
import com.pass.self.thread.DataTask;
import com.pass.self.thread.ThreadPool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * 索引界面
 * @author leker
 *
 */
@SuppressLint("NewApi")
public class IndexActivity extends Activity {
	
	protected View mDecorView;
	String TAG = "IndexActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ImageView imageView = new ImageView(this);
		imageView.setBackground(getDrawable(R.drawable.connect));
		setContentView(imageView);
		mDecorView = getWindow().getDecorView();
		hideNavigationBar();
		testThreadPool();
	}
	
	private void testThreadPool(){
		ThreadPoolExecutor executor =  ThreadPool.getInstance().getExecutor();
		for(int i=0;i<15;i++){
            MyTask myTask = new MyTask(i);
            executor.submit(myTask);
            Log.i(TAG, "线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
            executor.getQueue().size()+"，已执行完毕的任务数目："+executor.getCompletedTaskCount());
        }
        executor.shutdown();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		if (hasFocus)
			hideNavigationBar();
	}
	
	protected void hideNavigationBar() {
		if (mDecorView == null)
			return;
		
		int visibility = mDecorView.getSystemUiVisibility();
		visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		mDecorView.setSystemUiVisibility(visibility);
	}
	
	private void initData(){
		try {
			ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), 0);
			info.metaData.getString("data_name");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	class MyTask extends DataTask{
		private int taskNum;
		public MyTask(int type) {
			super(type);
			// TODO Auto-generated constructor stub
			this.taskNum = type;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i(TAG, "正在执行task "+taskNum);
	        try {
	            Thread.currentThread().sleep(4000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        Log.i(TAG,"task "+taskNum+"执行完毕");
	        super.run();
		}
	}
	
	private KProgressHUD hud;
	protected void showLoadingDialog(){
		/*if(dialog == null){
			dialog = new ProgressDialog(this);//.show(this, "Loading", "Please wait...", true);
			ImageView imageView = new ImageView(this);
			imageView.setBackground(getDrawable(R.drawable.reminder_loading));
			dialog.setContentView(imageView);
		}00000000000000
		dialog.show();*/
		if(hud == null){
			 hud = KProgressHUD.create(this)
	                 .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
		}
		hud.show();  
	}
	
	protected void dismissLoadingDialog(){
		if(hud != null && hud.isShowing())
			hud.dismiss();
		
	}
}
