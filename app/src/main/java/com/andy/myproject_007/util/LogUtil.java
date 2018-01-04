package com.andy.myproject_007.util;

import android.content.Context;
import android.util.Log;

import com.andy.myproject_007.R;
import com.andy.myproject_007.common.MainApplication;
import com.andy.myproject_007.ui.activity.sub.MainActivity;


public class LogUtil {

	private static String TAG = "";

	private static LogUtil mLogUtil;

	private static Context context;

	public static LogUtil getInstance(){
		if(mLogUtil == null){
			synchronized (LogUtil.class){
				if(mLogUtil == null){
					mLogUtil = new LogUtil();
					context = MainApplication.getContext();
				}
			}
		}
		return mLogUtil;
	}

	public void Debug(String msg) {
		TAG = context.getClass().getName();
		boolean isDebug = context.getResources().getBoolean(R.bool.isDebug);
		if(isDebug){
			Log.d(TAG, msg);
		}
	}

	public void Info(String msg) {
		TAG = context.getClass().getName();
		boolean isDebug = context.getResources().getBoolean(R.bool.isDebug);
		if(isDebug){
			Log.i(TAG, msg);
		}
	}

	public void Error(String msg) {
		TAG = context.getClass().getName();
		boolean isDebug = context.getResources().getBoolean(R.bool.isDebug);
		if(isDebug){
			Log.e(TAG, msg);
		}
	}

}
