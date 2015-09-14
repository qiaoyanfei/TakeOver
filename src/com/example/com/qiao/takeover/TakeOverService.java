package com.example.com.qiao.takeover;

import java.util.Timer;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;



public class TakeOverService extends Service {  
    private Timer mTimer;  
    public static final int FOREGROUND_ID = 0;  
  
    public static final String TAG="TakeOverService";
    private static int val=1;
    private void startTimer() {  
        if (mTimer == null) {  
            mTimer = new Timer();  
            TakeOverTask takeOverTask = new TakeOverTask(this);  
            mTimer.schedule(takeOverTask, 0L, 100L);  
        }  
    }  
  
    public IBinder onBind(Intent intent) {  
        return null;  
    }  
  
    public void onCreate() {  
        super.onCreate();  
        Log.e(TAG, "onCreate");
        startForeground(FOREGROUND_ID, new Notification());  
        val++;
    }  
  
    public int onStartCommand(Intent intent, int flags, int startId) { 
    	 new Thread(new Runnable() {  
 	        @Override  
 	        public void run() {  
 	        	  startTimer();  // 开始执行后台任务  
 	        }  
 	    }).start(); 
//    	 Log.e(TAG, "onStartCommand");
    	 Log.e(TAG, "onStartCommand"+val);
        return super.onStartCommand(intent, flags, startId);  
//        return START_NOT_STICKY;
    }  
  
    public void onDestroy() {  
        stopForeground(true);  
        mTimer.cancel();  
        mTimer.purge();  
        mTimer = null;  
   	 Log.e(TAG, "onDestroy");
//   	 android.os.Process.killProcess(android.os.Process.myPid()); 
        super.onDestroy();  
    }  
}  
