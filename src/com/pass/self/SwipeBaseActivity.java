package com.pass.self;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * 滑动返回Activity
 * @author leker
 *
 */
@SuppressLint("NewApi") public class SwipeBaseActivity extends Activity {
	
	private SwipeLayout swipeLayout;

    /**
     * 是否可以滑动关闭页面
     */
    protected boolean swipeEnabled = true;

    /**
     * 是否可以在页面任意位置右滑关闭页面，如果是false则从左边滑才可以关闭。
     */
    protected boolean swipeAnyWhere = false;
    
    private boolean swipeFinished = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		swipeLayout = new SwipeLayout(this);
	}
	
	public void setSwipeAnyWhere(boolean swipeAnyWhere) {
        this.swipeAnyWhere = swipeAnyWhere;
    }

    public boolean isSwipeAnyWhere() {
        return swipeAnyWhere;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        this.swipeEnabled = swipeEnabled;
    }

    public boolean isSwipeEnabled() {
        return swipeEnabled;
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onPostCreate(savedInstanceState);
    	swipeLayout.replaceLayer(this);
    }
	
    @Override
    public void finish() {
    	// TODO Auto-generated method stub
    	if(swipeFinished){
    		super.finish();
    		overridePendingTransition(0, 0);
    	}else{
    		swipeLayout.cancelPendingInputEvents();
    		super.finish();
    		overridePendingTransition(0, R.anim.slide_out_right);
    	}
    }
	
	class SwipeLayout extends FrameLayout{
		
		//private View backgroundLayer;用来设置滑动时的背景色
        private Drawable leftShadow;
        
        boolean canSwipe = false;
        /**
         * 超过了touchslop仍然没有达到没有条件，则忽略以后的动作
         */
        boolean ignoreSwipe = false;
        View content;
        Activity mActivity;
        int sideWidthInDp = 16;
        int sideWidth = 72;
        int screenWidth = 1080;
        
        VelocityTracker tracker;
        
        float downX;
        float downY;
        float lastX;
        float currentX;
        float currentY;
        
        int touchSlopDp = 30;
        int touchSlop = 60;
		
		public SwipeLayout(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		public SwipeLayout(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}

		public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
			// TODO Auto-generated constructor stub
		}
		
		
		public void replaceLayer(Activity activity){
			leftShadow = activity.getResources().getDrawable(R.drawable.left_shadow);
			touchSlop = (int) (touchSlopDp * activity.getResources().getDisplayMetrics().density);
			sideWidth = (int) (sideWidthInDp * activity.getResources().getDisplayMetrics().density);
			mActivity = activity;
			screenWidth = getScreenWidth(activity);
			
			setClickable(true);
			final ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
			content = root.getChildAt(0);
			ViewGroup.LayoutParams params = content.getLayoutParams();
			ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(-1, -1);
			root.removeView(content);
			root.addView(content, params2);
			root.addView(this, params);
		}
		
		@Override
		protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
			// TODO Auto-generated method stub
			boolean result = super.drawChild(canvas, child, drawingTime);
			final int shadowWidth = leftShadow.getIntrinsicWidth();
			int left = (int)getContentX() - shadowWidth;
			leftShadow.setBounds(left, child.getTop(), left + shadowWidth, child.getBottom());
			leftShadow.draw(canvas);
			return result;
		}
		
		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			// TODO Auto-generated method stub
			if(swipeEnabled && !canSwipe && !ignoreSwipe){
				if(swipeAnyWhere){
					switch(ev.getAction()){
					case MotionEvent.ACTION_DOWN:
						downX = ev.getX();
						downY = ev.getY();
						currentX = downX;
						currentY = downY;
						lastX = downX;
						break;
					case MotionEvent.ACTION_MOVE:
						float dx = ev.getX() - downX;
						float dy = ev.getY() - downY;
						if(dx*dx + dy*dy > touchSlop * touchSlop){
							if(dy == 0f ||Math.abs(dx / dy) > 1){
								downX = ev.getX();
								downY = ev.getY();
								currentX = downX;
								currentY = downY;
								lastX = downX;
								canSwipe = true;
								tracker = VelocityTracker.obtain();
								return true;
							}else{
								ignoreSwipe = true;
							}
						}
						break;
					}
				}else if(ev.getAction() == MotionEvent.ACTION_DOWN && ev.getX() < sideWidth){
					canSwipe = true;
					tracker = VelocityTracker.obtain();
					return true;
				}
			}
			
			if(ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL){
				ignoreSwipe = false;
			}
			return super.dispatchTouchEvent(ev);
		}
		
		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {
			// TODO Auto-generated method stub
			return canSwipe || super.onInterceptTouchEvent(ev);
		}
		
		boolean hasIgnoreFirstMove;
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			if(canSwipe){
				tracker.addMovement(event);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX = event.getX();
					downY = event.getY();
					currentX = downX;
					currentY = downY;
					lastX = downX;
					break;
				case MotionEvent.ACTION_MOVE:
					currentX = event.getX();
					currentY = event.getY();
					float dx = currentX - lastX;
					if(dx !=0f && !hasIgnoreFirstMove){
						hasIgnoreFirstMove = true;
						dx = dx / dx;
					}
					if(getContentX() + dx < 0){
						setContentX(0);
					}else{
						setContentX(getContentX() +dx);
					}
					lastX = currentX;
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					tracker.computeCurrentVelocity(1000);
					tracker.computeCurrentVelocity(1000, 2000);
					canSwipe = false;
					hasIgnoreFirstMove = false;
					int mv = screenWidth/200 * 1000;
					if(Math.abs(tracker.getXVelocity()) > mv){
						animateFromVelocity(tracker.getXVelocity());
					}else{
						if(getContentX() > screenWidth/2){
							animateFinish(false);
						}else{
							animateBack(false);
						}
					}
					tracker.recycle();
					break;
				default:
					break;
				}
			}
			return super.onTouchEvent(event);
		}
		
		ObjectAnimator animator;
		private final int duration = 200;
		public void cancelPotentialAnimation(){
			if(animator != null){
				animator.removeAllListeners();
				animator.cancel();
			}
		}
		
		/**
		 * 弹回不关闭，因为left是0，所以setX和setTranslationX效果是一样的
		 * @param withVel
		 */
		private void animateBack(boolean withVel){
			cancelPendingInputEvents();
			animator = ObjectAnimator.ofFloat(this, "contentX", getContentX(), 0);
			int tmpDuration = withVel ? ((int)(duration * getContentX() / screenWidth)) : duration;
			if(tmpDuration < 100){
				tmpDuration = 100;
			}
			animator.setDuration(tmpDuration);
			animator.setInterpolator(new DecelerateInterpolator());
			animator.start();
		}
		
		private void animateFinish(boolean withVel){
			cancelPendingInputEvents();
			animator = ObjectAnimator.ofFloat(this, "contentX", getContentX(), screenWidth);
			int tmpDuration = withVel ? ((int) (duration * (screenWidth - getContentX()) / screenWidth)) : duration;
			if(tmpDuration < 100){
				tmpDuration = 100;
			}
			animator.setDuration(tmpDuration);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!mActivity.isFinishing()) {
                        swipeFinished = true;
                        mActivity.finish();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }
            });
            animator.start();
		}
		
		private int getScreenWidth(Context context){
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(metrics);
			return metrics.widthPixels;
		}
		
		public void setContentX(float x) {
            int ix = (int) x;
            content.setX(ix);
            invalidate();
        }

        public float getContentX() {
            return content.getX();
        }
        
        private void animateFromVelocity(float v) {
            if (v > 0) {
                if (getContentX() < screenWidth / 2 && v * duration / 1000 + getContentX() < screenWidth / 2) {
                    animateBack(false);
                } else {
                    animateFinish(true);
                }
            } else {
                if (getContentX() > screenWidth / 2 && v * duration / 1000 + getContentX() > screenWidth / 2) {
                    animateFinish(false);
                } else {
                    animateBack(true);
                }
            }

        }
	}
}
