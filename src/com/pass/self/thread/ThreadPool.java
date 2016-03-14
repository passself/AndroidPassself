package com.pass.self.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
	
	private static volatile ThreadPool instance;
	ThreadPoolExecutor executor;
	int normalNum = 5;
	int maxNum = 8;
	int keepAliveTime = 200;
	
	private ThreadPool() {
		// TODO Auto-generated constructor stub
		executor = getExecutorInstance();
	}
	
	private ThreadPoolExecutor getExecutorInstance(){
		return new ThreadPoolExecutor(normalNum, maxNum, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
	}
	
	public static ThreadPool getInstance(){
		if(instance == null){
			instance = new ThreadPool();
		}
		return instance;
	}
	
	public void submit(Runnable runnable){
		if(executor != null){
			executor.submit(runnable);
		}
	}
	
	public ThreadPoolExecutor getExecutor(){
		if(executor == null){
			executor = getExecutorInstance();
		}
		return executor;
	}
}
