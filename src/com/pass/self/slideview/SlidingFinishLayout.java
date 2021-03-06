package com.pass.self.slideview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * 为了实现Activity的滑动删除
 * @author leker
 *
 */
public class SlidingFinishLayout extends RelativeLayout implements OnTouchListener{
	
	/**
	 * SlidingFinishLayout布局的父布局
	 */
	private ViewGroup mParentView;
	
	/**
	 * 处理滑动的View
	 */
	private View touchView;
	
	/**
	 * 滑动的最小距离
	 */
	private int mTouchSlop;
	
	/**
	 * 按下点的X坐标
	 */
	private int downX;
	
	/**
	 * 按下点的Y坐标
	 */
	private int downY;
	
	/**
	 * 临时存储x坐标
	 */
	private int tempX;
	
	/**
	 * 滑动类
	 */
	private Scroller mScroller;
	
	/**
	 * SlidingFinishLayout的宽度
	 */
	private int viewWidth;
	/**
	 * 记录是否在滑动
	 */
	private boolean isSliding;
	
	OnSlidingFinishListener onSlidingFinishListener;
	private boolean isFinish;
	
	public SlidingFinishLayout(Context context, AttributeSet attrs
			) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	

	public SlidingFinishLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mScroller = new Scroller(context);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if(changed){
			//获取SlidingFinishLayout所在布局的父布局
			mParentView = (ViewGroup)this.getParent();
			viewWidth = this.getWidth();
		}
	}
	
	public void setOnSlidingFinishListener(OnSlidingFinishListener listener){
		this.onSlidingFinishListener = listener;
	}
	
	/**
	 * 设置touch的view
	 * @param touchView
	 */
	public void setTouchView(View touchView){
		this.touchView = touchView;
		touchView.setOnTouchListener(this);
	}
	
	public View getTouchView(){
		return touchView;
	}
	
	/**
	 * 滚动出界面
	 */
	private void scrollRight(){
		final int delta = viewWidth + mParentView.getScrollX();
		//调用startScroll方法来设置一些滚动参数，我们在computeScroll()方法中调用scrollTo来滚动item
		mScroller.startScroll(mParentView.getScrollX(), 0, -delta+1, 0,Math.abs(delta));
		postInvalidate();
	}
	
	/**
	 * 滚动到起始位置
	 */
	private void scrollOrigin(){
		int delta = mParentView.getScrollX();
		mScroller.startScroll(mParentView.getScrollX(), 0, -delta, 0,Math.abs(delta));
		postInvalidate();
	}
	
	/**
	 * touch的View是否是AbsListView, 例如ListView，GridView等其子类
	 * @return
	 */
	private boolean isTouchOnAbsListView(){
		return touchView instanceof AbsListView ? true : false;
	}
	
	/**
	 * touch 的view是否是ScrollView或其子类
	 * @return
	 */
	private boolean isTouchOnScrollView(){
		return touchView instanceof ScrollView ? true : false;
	}
	
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			downX = tempX = (int)event.getRawX();
			downY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int)event.getRawX();
			int deltaX = tempX - moveX;
			tempX = moveX;
			if(Math.abs(moveX-downX) > mTouchSlop && Math.abs((int)event.getRawY() - downY) < mTouchSlop){
				isSliding = true;
				
				//若touchView是AbsListView
				//则当手指滑动，取消item的点击事件,不然我们滑动也伴随着item点击事件发生
				MotionEvent cancelEvent = MotionEvent.obtain(event);
				cancelEvent.setAction(MotionEvent.ACTION_CANCEL | (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
				v.onTouchEvent(cancelEvent);
			}
			
			if(moveX-downX > 0 && isSliding){
				mParentView.scrollBy(deltaX, 0);
				
				//屏蔽在滑动过程中ListView ScrollView等自己的事件
				if(isTouchOnScrollView() || isTouchOnAbsListView()){
					return true;
				}
			}
			
			break;
		case MotionEvent.ACTION_UP:
			isSliding = false;
			if(mParentView.getScrollX() <= -viewWidth/2){
				isFinish = true;
				scrollRight();
			}else{
				scrollOrigin();
				isFinish = false;
			}
			break;
		}
		
		//假如touch的view是AbsListView或者ScrollView 我们处理完上面的操作
		//再交给AbsListView,ScrollView自己处理自己的逻辑
		if(isTouchOnScrollView() || isTouchOnAbsListView()){
			return v.onTouchEvent(event);
		}
		
		//其他情况直接返回true
		return true;
	}
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		//调用startScroll的时候scroller.computeScrollOffSet()返回true
		if(mScroller.computeScrollOffset()){
			mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
			
			if(mScroller.isFinished()){
				if(onSlidingFinishListener!=null && isFinish){
					onSlidingFinishListener.onSlidingFinish();
				}
			}
		}
	}


	public interface OnSlidingFinishListener{
		public void onSlidingFinish();
	}
}
