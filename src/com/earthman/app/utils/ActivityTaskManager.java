package com.earthman.app.utils;

import java.util.Iterator;
import java.util.Stack;

import android.app.Activity;

/**
 * 
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-17 上午10:35:35
 * @Decription 
 */
public class ActivityTaskManager {

	private static ActivityTaskManager instance;
	private static Stack<Activity> activityStack;

	public static ActivityTaskManager getInstance() {
		if (instance == null) {
			instance = new ActivityTaskManager();
		}
		return instance;
	}
	
	private ActivityTaskManager(){
		activityStack = new Stack<Activity>();
	}

	public void popActivity() {
		Activity activity = activityStack.lastElement();
		if (activity != null) {
			activity.finish();
			activity = null;
		}
	}

	public void popActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	public void pushActivity(Activity activity) {
		activityStack.add(activity);
	}

	public void popAllActivityExceptOne(Class cls) {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				break;
			}
			popActivity(activity);
		}
	}
	
	public void popActivity(Class cls) {
		for (Iterator<Activity> iterator = activityStack.iterator(); iterator.hasNext();) {
			Activity activity = iterator.next();
			if(activity != null && activity.getClass().equals(cls)){
				activity.finish();
				activityStack.remove(activity);
				activity = null;
			}
			
		}
	}
}
