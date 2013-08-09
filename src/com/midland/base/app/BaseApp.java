package com.midland.base.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.midland.base.util.Common;
import com.midland.base.util.Ran;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

public class BaseApp extends Application {
    public static String APP_NAME = "";
    public static String IMAGE_FOLDER_NAME = File.separator + "images" + File.separator;
    public static BaseApp me;

    @Override
    public void onCreate() {
        super.onCreate();
        Common.i("App onCreate: " + getDeviceId());
        me = this;
        Thread.setDefaultUncaughtExceptionHandler(ueh);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Common.i("App onTerminate");
        me = null;
    }

    public void savePref(String pref, String value) {
        SharedPreferences settings = getSharedPreferences(APP_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(pref, value);
        editor.commit();
    }

    public String getPref(String pref) {
        SharedPreferences settings = getSharedPreferences(APP_NAME, 0);
        return settings.getString(pref, "");
    }

    public String getDeviceId() {
        String id = getPref("DEVICE_ID");
        if (id.length() > 0) {
            return id;
        } else {
            String newId = Ran.genDeviceID();
            savePref("DEVICE_ID", newId);
            return newId;
        }
    }

    public String getVersionName() {
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (NameNotFoundException e) {
            return "1.0";
        }
    }

    public String getVersionCode() {
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionCode + "";
        } catch (NameNotFoundException e) {
            return "1";
        }
    }

    public boolean hasNewApp(String versionInSever) {
        String[] a1 = versionInSever.trim().split("\\.");
        int majorServer = Integer.parseInt(a1[0]);
        int minorServer = a1.length >= 2 ? Integer.parseInt(a1[1]) : 0;

        String versionDevice = getVersionName();
        String[] a2 = versionDevice.split("\\.");
        int majorDevice = Integer.parseInt(a2[0]);
        int minorDevice = a2.length >= 2 ? Integer.parseInt(a2[1]) : 0;

        if (majorServer > majorDevice && minorServer > minorDevice) {
            return true;
        } else {
            return false;
        }
    }

    private static UncaughtExceptionHandler defualtHandler = Thread.getDefaultUncaughtExceptionHandler();

    private static UncaughtExceptionHandler ueh = new UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            Common.e(ex);
            defualtHandler.uncaughtException(thread, ex);
        }

    };

    public static int dpToPx(float dp) {
        final float scale = me.getResources().getDisplayMetrics().density;
        int px = (int) (dp * scale + 0.5f);
        return px;
    }

    public static int dpToPx(int dp) {
        final float scale = me.getResources().getDisplayMetrics().density;
        int px = (int) (dp * scale + 0.5f);
        return px;
    }

    public static int longSide() {
        int w = me.getResources().getDisplayMetrics().widthPixels;
        int h = me.getResources().getDisplayMetrics().heightPixels;

        if (w >= h) {
            return w;
        } else {
            return h;
        }
    }

}
