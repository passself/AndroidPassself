package com.pass.self.slideview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

public class SlideCutListView extends ListView {
	
	/** 
     * 当前滑动的ListView　position 
     */  
    private int slidePosition;
    /** 
     * 手指按下X的坐标 
     */  
    private int downY;  
    /** 
     * 手指按下Y的坐标 
     */  
    private int downX;  
    /** 
     * 屏幕宽度 
     */  
    private int screenWidth;  
    /** 
     * ListView的item 
     */  
    private View itemView;  
    /** 
     * 滑动类 
     */  
    private Scroller scroller;  
    private static final int SNAP_VELOCITY = 600;  
    
    /**
     * 速度追踪对象
     */
    private VelocityTracker velocityTracker;
    /**
     * 是否响应滑动,默认不响应
     */
    private boolean isSlide = false;
    
    /**
     * 认为用户滑动的最新距离
     */
    private int mTouchSlop;
    
    /** 
     *  移除item后的回调接口 
     */  
    private RemoveListener mRemoveListener;  
    
    private RemoveDirection removeDirection;
    
    public enum RemoveDirection{
    	RIGHT,LEFT
    };

    
    public SlideCutListView(Context context) {  
        this(context, null);  
    }  
  
    public SlideCutListView(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }  
    
	public SlideCutListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		screenWidth = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getWidth();
		
		scroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 设置滑动删除的回调接口
	 * @param listener
	 */
	public void setRemoveListener(RemoveListener listener){
		this.mRemoveListener = listener;
	}
	
	/**
	 * 分发事件，主要做的是判断点击的item是哪个，以及通过postDelayed来设置相应左右滑动事件
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			addVelocityTracker(event);
			//如果scroller滚动还没有结束，直接返回
			if(!scroller.isFinished()){
				return super.onTouchEvent(event);
			}
			downX = (int)event.getX();
			downY = (int)event.getY();
			
			slidePosition = pointToPosition(downX, downY);
			
			if(slidePosition == AdapterView.INVALID_POSITION){
				return super.dispatchTouchEvent(event); 
			}
			itemView = getChildAt(slidePosition - getFirstVisiblePosition());
			break;
		case MotionEvent.ACTION_MOVE:
			if(Math.abs(getScrollVelocity()) > SNAP_VELOCITY || 
					(Math.abs(event.getX() - downX) > mTouchSlop && Math.abs(event.getY() - downY) < mTouchSlop)){
				isSlide = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			break;
		}
		return super.dispatchTouchEvent(event);
	}
	
	private void scrollRight(){
		removeDirection = RemoveDirection.RIGHT;
		final int delta = (screenWidth + itemView.getScrollX());
		// 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
		scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,Math.abs(delta));
		postInvalidate();
	}
	/** 
     * 向左滑动，根据上面我们知道向左滑动为正值 
     */  
	private void scrollLeft(){
		removeDirection = RemoveDirection.LEFT;
		final int delta = (screenWidth - itemView.getScrollX());
		scroller.startScroll(itemView.getScrollX(), 0, delta, 0,Math.abs(delta));
		postInvalidate();
	}
	
	
	private void scrollByDistanceX(){
		//如果向左滚动的距离大于屏幕的二分之一，就让其删除
		if(itemView.getScrollX() >= screenWidth/2){
			scrollLeft();
		}else if(itemView.getScrollX() <= - screenWidth/2){
			scrollRight();
		}else{
			// 滚回到原始位置,为了偷下懒这里是直接调用scrollTo滚动  
            itemView.scrollTo(0, 0);  
		}
	}
	
	/** 
     * 处理我们拖动ListView item的逻辑 
     */  
    @Override  
    public boolean onTouchEvent(MotionEvent ev) {  
        if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {  
            requestDisallowInterceptTouchEvent(true);  
            addVelocityTracker(ev);  
            final int action = ev.getAction();  
            int x = (int) ev.getX();  
            switch (action) {  
            case MotionEvent.ACTION_DOWN:  
                break;  
            case MotionEvent.ACTION_MOVE:  
                MotionEvent cacelEvent = MotionEvent.obtain(ev);
				cacelEvent.setAction(MotionEvent.ACTION_CANCEL | 
						ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT);
				onTouchEvent(cacelEvent);
				
				int deltaX = downX - x;
				downX = x;
				
				//手指拖动itemView滚动，deltaX大于0向左滚动，小于0向左滚动
				itemView.scrollBy(deltaX, 0);
				return true;//拖动的时候ListView不滚动
            case MotionEvent.ACTION_UP:  
                int velocityX = getScrollVelocity();  
                if (velocityX > SNAP_VELOCITY) {  
                    scrollRight();  
                } else if (velocityX < -SNAP_VELOCITY) {  
                    scrollLeft();  
                } else {  
                    scrollByDistanceX();  
                }  
                  
                recycleVelocityTracker();  
                // 手指离开的时候就不响应左右滚动  
                isSlide = false;  
                break;  
            }  
        }  
  
        //否则直接交给ListView来处理onTouchEvent事件  
        return super.onTouchEvent(ev);  
    }  
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		//调用startScroll的时候scroller.computeScrollOffset()返回true
		if(scroller.computeScrollOffset()){
			// 让ListView item根据当前的滚动偏移量进行滚动
			itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
			//滚动动画结束的时候调用回调接口
			if(scroller.isFinished()){
				if(mRemoveListener==null){
					throw new NullPointerException("RemoveListener is null, we should called setRemoveListener()");
				}
				itemView.scrollTo(0, 0);
				mRemoveListener.removeItem(removeDirection, slidePosition);
			}
		}
		super.computeScroll();
	}
	
	/**
	 * 添加用户的速度跟踪器
	 * @param event
	 */
	private void addVelocityTracker(MotionEvent event){
		if(velocityTracker==null){
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(event);
	}
    
	/**
	 * 移除用户的速度跟踪器
	 */
	private void recycleVelocityTracker(){
		if(velocityTracker != null){
			velocityTracker.recycle();
			velocityTracker = null;
		}
	}
	
	private int getScrollVelocity(){
		velocityTracker.computeCurrentVelocity(1000);
		int velocity = (int)velocityTracker.getXVelocity();
		return velocity;
	}
	
	/**
	 * 
	 * 当ListView item滑出屏幕，回调这个接口
	 * 我们需要在回调方法removeItem()中移除该Item,然后刷新ListView
	 *
	 */
    public interface RemoveListener{
    	public void removeItem(RemoveDirection direction,int position);
    }
}
