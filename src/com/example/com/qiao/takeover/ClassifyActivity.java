package com.example.com.qiao.takeover;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.util.Log;

public class ClassifyActivity {

	private Context mContext = null;
	private ArrayList<String> contactNameList;
	private ArrayList<String> dialNameList;
	private ArrayList<String> mmsNameList;

	public boolean isAlias = false;

	public ClassifyActivity(Context context) {
		mContext = context;
		getSystemPackageList();
	}

	public boolean isDialActivity(String packageName, String className) {
		if (contactNameList.contains(packageName)) {
			if (isAlias) // className ----> recent className
			{
				if (className != null)
					return true;
			} else {
				if (className != null && className.contains("dial"))
					return true;
			}

		}

		if (dialNameList.contains(packageName)) {
			if (isAlias) // className ----> recent className
			{
				if (className != null && className.contains("dial"))
					return true;
			} else {
				if (className != null && className.contains("dial"))
					return true;
			}

		}
		return false;

	}

	public boolean isContactActivity(String packageName, String className) {
		if (contactNameList.contains(packageName)) {
			if (isAlias) // className ----> recent className
			{
				if (className == null)
					return true;
			} else {
				if (className != null && !className.contains("dial"))
					return true;
			}

		}
		return false;

	}

	public boolean isMmsActivity(String packageName, String className) {
		if (mmsNameList.contains(packageName)) {

			if (className != null && className.contains("ui"))
				return true;

		}
		return false;

	}

	public boolean isContactOrDialOrMms(String packageName, String className) {
		
		if(isMmsActivity(packageName,className))
			return true;
		
		if(isContactActivity(packageName,className))
			return true;
		if(isDialActivity(packageName,className))
			return true;
//		if (contactNameList.contains(packageName)
//				|| dialNameList.contains(packageName)||mmsNameList.contains(packageName)) {
//			return true;
//		}
		return false;

	}

	public class AppInfo {

		public String appName = "";
		public String packageName = "";
		public String versionName = "";
		public int versionCode = 0;

		public void print() {

			Log.v("app", "Name:" + appName + " Package:" + packageName);
			Log.v("app", "Name:" + appName + " versionName:" + versionName);
			Log.v("app", "Name:" + appName + " versionCode:" + versionCode);
		}

	}

	private void getSystemPackageList() {
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); // 用来存储获取的应用信息数据

		contactNameList = new ArrayList<String>();
		dialNameList = new ArrayList<String>();
		mmsNameList = new ArrayList<String>();
		List<PackageInfo> packages = mContext.getPackageManager()
				.getInstalledPackages(0);

		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			AppInfo tmpInfo = new AppInfo();
			tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
					mContext.getPackageManager()).toString();
			tmpInfo.packageName = packageInfo.packageName;
			tmpInfo.versionName = packageInfo.versionName;
			tmpInfo.versionCode = packageInfo.versionCode;

			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// 非系统应用
			} else {
				tmpInfo.print();
				if (tmpInfo.appName.equals("拨号")
						|| tmpInfo.packageName.contains("dialer")) {

					Log.e(tmpInfo.appName, tmpInfo.packageName);
					dialNameList.add(tmpInfo.packageName);
					appList.add(tmpInfo);
				} else if (tmpInfo.appName.equals("联系人")
						|| tmpInfo.packageName.contains("people")|| tmpInfo.packageName.contains("contact")) {
					Log.e(tmpInfo.appName, tmpInfo.packageName);
					contactNameList.add(tmpInfo.packageName);
					appList.add(tmpInfo);
				} else if (tmpInfo.appName.equals("信息")
						|| tmpInfo.appName.equals("短信")
						|| tmpInfo.packageName.contains("mms")) {
					Log.e(tmpInfo.appName, tmpInfo.packageName);

					// contactNameList.add(tmpInfo.packageName);
					mmsNameList.add(tmpInfo.packageName);
					appList.add(tmpInfo);
				}

				// 系统应用　　　　　　　　
			}

		}
	}
}
