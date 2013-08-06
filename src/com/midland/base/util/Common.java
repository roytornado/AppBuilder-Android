package com.midland.base.util;

import android.util.Log;

import com.midland.base.app.BaseApp;

public class Common {
	
	public static boolean isDebug = true;
	
	public synchronized static void v(String s) {
		if (isDebug) {
			Log.v(BaseApp.APP_NAME, s);
		}
	}
	
	public synchronized static void d(String s) {
		if (isDebug) {
			Log.d(BaseApp.APP_NAME, s);
		}
	}
	
	public synchronized static void i(String s) {
		if (isDebug) {
			Log.i(BaseApp.APP_NAME, s);
		}
	}
	
	public synchronized static void e(String s) {
		if (isDebug) {
			Log.e(BaseApp.APP_NAME, s);
		}
	}
	
	public synchronized static void e(Throwable e) {
		if (isDebug) {
			Log.e(BaseApp.APP_NAME, "Exception!!", e);
		}
	}
	
	public static String urlToFilename(String url) {
		return url.substring(url.lastIndexOf("/") + 1, url.length());
	}
	
}
