package com.royalnext.base.util.network;

import android.content.Context;

import com.royalnext.base.app.BaseApp;
import com.royalnext.base.util.Common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ApiCache {
    public static int FLAG_NO_CACHE = 0;
    public static int FLAG_RELOAD = 1;
    public static int FLAG_CACHE = 2;

    public static String filePrefix = "API_CACHE_";

    public Date time;
    public String apiUniKey;
    public String content;

    public ApiCache(String apiUniKey, String content) {
        super();
        this.time = new Date();
        this.apiUniKey = apiUniKey;
        this.content = content;
    }

    private static final int CACHE_SIZE = 30;
    private static ConcurrentLinkedQueue<ApiCache> cacheList = new ConcurrentLinkedQueue<ApiCache>();

    public static void addCache(String apiUniKey, String content) {

		/*
         * for (ApiCache cache : cacheList) { if
		 * (cache.apiUniKey.equals(apiUniKey)) { cacheList.remove(cache); } }
		 * ApiCache newCache = new ApiCache(apiUniKey, content);
		 * Common.d("addCache: " + apiUniKey); cacheList.add(newCache); if
		 * (cacheList.size() > CACHE_SIZE) { cacheList.poll(); }
		 */

        FileOutputStream fos;
        try {
            fos = BaseApp.me.openFileOutput(filePrefix + apiUniKey, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
            Common.d("Add Cache: " + filePrefix + apiUniKey);
        } catch (Exception e) {
        } finally {
        }
    }

    public static String getCache(String apiUniKey) {
        try {
            String content = "";
            FileInputStream fs = BaseApp.me.openFileInput(filePrefix + apiUniKey);
            byte[] input = new byte[fs.available()];
            while (fs.read(input) != -1) {
            }
            content += new String(input);
            Common.d("Get Cache: " + filePrefix + apiUniKey);
            return content;
        } catch (Exception e) {
        }
        return null;

		/*
		 * for (ApiCache cache : cacheList) { if
		 * (cache.apiUniKey.equals(apiUniKey)) { Common.d("getCache: " +
		 * apiUniKey); return cache.content; } } return null;
		 */
    }

    public static void cleanUpCacheDir() {
        String[] files = BaseApp.me.fileList();
        Common.d("Cache File Size: " + files.length);
        if (files.length > CACHE_SIZE) {
            for (int i = CACHE_SIZE; i < files.length; i++) {
                Common.d("Del Cache: " + files[i]);
                BaseApp.me.deleteFile(files[i]);
            }
        }

    }
}
