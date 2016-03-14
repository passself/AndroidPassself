package com.pass.self.utils;

import com.pass.self.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;


/**
 * 播放提示声音
 * @author shihx
 * @date 2016-1-22
 * @todo TODO
 */
@SuppressLint("NewApi")
public class PlayUtils {
	
	private static PlayUtils instance;
	private Context mContext;
	private SoundPool.Builder spb = new SoundPool.Builder();
	SoundPool soundPool;
	
	/**
	 * 请更换手指
	 */
	public static int CHANGE_FINGER = R.raw.change_finger;
	/**
	 * 录入成功
	 */
	public static int INPUT_SUCCESS = R.raw.input_success; 
	/**
	 * 信息重复
	 */
	public static int MESSAGE_REPEAT = R.raw.message_repeat;
	/**
	 * 请按手指 
	 */
	public static int PRESS_FINGER  = R.raw.press_finger;
	/**
	 * 请再按一次
	 */
	public static int PRESS_FINGER_AGAIN  = R.raw.press_finger_again;
	/**
	 * 请重按手指
	 */
	public static int PRESS_FINGER_REPEAT  = R.raw.press_finger_repeat;
	/**
	 * 已打卡
	 */
	public static int SIGNED  = R.raw.signed;
	/**
	 * 谢谢
	 */
	public static int THANK_YOU = R.raw.thank_you;
	
	private PlayUtils(Context context){
		this.mContext = context;
		spb.setMaxStreams(10);
		//spb.setAudioAttributes(null); 
		soundPool = spb.build();
	}
	
	public static PlayUtils getInstance(Context context){
		if(instance == null){
			instance = new PlayUtils(context);
		}
		return instance;
	}
	
	public void playRing(int resId){
		soundPool.load(mContext, resId, 1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				// TODO Auto-generated method stub
				soundPool.play(sampleId, 1, 1, 0, 0, 1);
			}
		});
	}
	
	public void release(){
		soundPool.release();
	}
}
