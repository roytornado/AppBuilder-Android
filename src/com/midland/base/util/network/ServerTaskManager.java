package com.midland.base.util.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import android.net.Uri;
import android.util.Base64;

import com.midland.base.util.Common;
import com.midland.base.util.Ran;

public class ServerTaskManager {
	public static final int POST_TYPE = 1;
	public static final int GET_TYPE = 2;
	public static final int UPLOAD_TYPE = 3;
	private HashMap<Integer, ApiThread> mThreads = new HashMap<Integer, ApiThread>();
	
	public void free() {
		killAll();
	}
	
	public int startUploadPhotoTask(String host, HashMap<String, String> params, Uri photo, ServerTaskListener listener) {
		return startTask(host, params, listener, ApiCache.FLAG_NO_CACHE, UPLOAD_TYPE, photo);
	}
	
	public int startTask(String host, HashMap<String, String> params, ServerTaskListener listener) {
		return startTask(host, params, listener, ApiCache.FLAG_NO_CACHE, GET_TYPE, null);
	}
	
	public int startTask(String host, HashMap<String, String> params, ServerTaskListener listener, int flag, int type, Uri photo) {
		listener.onStart();
		int taskID = Ran.ranInt();
		if (flag == ApiCache.FLAG_CACHE) {
			String cache = ApiCache.getCache(genApiKey(host, params));
			if (cache != null) {
				listener.onSuccess(cache);
				return taskID;
			}
		}
		ApiThread thread = new ApiThread(genApiKey(host, params), host, params, listener, flag, type);
		thread.photo = photo;
		mThreads.put(taskID, thread);
		listener.setManager(this, taskID);
		thread.start();
		return taskID;
	}
	
	public void killTask(int taskID) {
		mThreads.remove(taskID);
	}
	
	private void killAll() {
		mThreads.clear();
	}
	
	public void onSuccess(int taskID, String content) {
		ApiThread thread = mThreads.get(taskID);
		if (thread != null && thread.flag != ApiCache.FLAG_NO_CACHE) {
			ApiCache.addCache(thread.apiUniKey, content);
		}
	}
	
	private static class ApiThread extends Thread {
		String apiUniKey;
		String host;
		HashMap<String, String> params;
		Uri photo;
		ServerTaskListener listener;
		int flag;
		int type;
		
		public ApiThread(String apiUniKey, String host, HashMap<String, String> params, ServerTaskListener listener, int flag, int type) {
			super();
			this.apiUniKey = apiUniKey;
			this.host = host;
			this.params = params;
			this.listener = listener;
			this.flag = flag;
			this.type = type;
		}
		
		@Override
		public void run() {
			try {
				HTTPRequestHelper http_helper = new HTTPRequestHelper(listener);
				if (listener.isLog) {
					Common.d("URL: " + host);
					for (String key : params.keySet()) {
						Common.d("Params: " + key + " | " + params.get(key));
					}
				}
				if (type == POST_TYPE) {
					http_helper.performPost(host, params);
				}
				if (type == GET_TYPE) {
					http_helper.performGet(host, params);
				}
				if (type == UPLOAD_TYPE) {
					HttpFileUploadHelper uploadHelper = new HttpFileUploadHelper(listener);
					uploadHelper.performUpload(host, params, photo);
				}
			} catch (Exception e) {
				Common.e(e);
			}
		}
		
	}
	
	private static String genApiKey(final String host, final HashMap<String, String> params) {
		SortedMap<String, String> sortedParamMap = new TreeMap<String, String>(params);
		String canonicalQS = canonicalizeForSign(sortedParamMap);
		String key = host + "::" + canonicalQS;
		return Base64.encodeToString(key.getBytes(), Base64.DEFAULT);
	}
	
	private static String canonicalizeForSign(SortedMap<String, String> sortedParamMap) {
		if (sortedParamMap.isEmpty()) {
			return "";
		}
		
		StringBuffer buffer = new StringBuffer();
		Iterator<Map.Entry<String, String>> iter = sortedParamMap.entrySet().iterator();
		
		while (iter.hasNext()) {
			Map.Entry<String, String> kvpair = iter.next();
			buffer.append(kvpair.getValue());
		}
		String cannoical = buffer.toString();
		return cannoical;
	}
	
}
