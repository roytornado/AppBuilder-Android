package com.midland.base.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.midland.base.R;
import com.midland.base.app.BaseApp;
import com.midland.base.util.TaskManager.ThreadTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageLoader {
    // ***** public ****** //
    public static ImageLoader getLoader() {
        if (uniLoader == null) {
            uniLoader = new ImageLoader();
        }
        return uniLoader;
    }

    public static void free() {
        hardCache.evictAll();
    }

    public static long getCacheSizeInSD() {
        long size = 0;
        try {
            File dir = new File(BaseApp.me.getCacheDir(), BaseApp.IMAGE_FOLDER_NAME);
            if (dir.exists()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    size += file.length();
                }
            }
        } catch (Exception e) {
            Common.e(e);
        }
        return size;
    }

    public static void cleanCacheInSD() {
        try {
            File dir = new File(BaseApp.me.getCacheDir(), BaseApp.IMAGE_FOLDER_NAME);
            if (dir.exists()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            Common.e(e);
        }
    }

    public void loadImgOtherThread(final String url, final ImageView imageView) {
        this.loadImgOtherThread(url, imageView, ImgSizeType.THUMB);
    }

    public void loadImgOtherThread(final String url, final ImageView imageView, final ImgSizeType type) {
        imageView.setImageResource(R.drawable.ic_image);
        final ViewHandler handler = new ViewHandler(imageView);
        ThreadTask thread = new ThreadTask() {
            public void doInBackground() {
                Drawable drawable = fetchDrawable(url, type);
                Message message = handler.obtainMessage(1, drawable);
                handler.sendMessage(message);
            }
        };
        thread.viewOwner = imageView.hashCode() + "";
        taskManager.cleanTaskByViewOwner(imageView.hashCode() + "");
        taskManager.addTask(thread);
    }

    public enum ImgSizeType {
        THUMB, ORG
    }

    // ***** private ****** //

    private TaskManager taskManager = new TaskManager(5);
    private final static int hardCachedSize = 2 * 1024 * 1024;
    private final static LruCache<String, Bitmap> hardCache = new LruCache<String, Bitmap>(hardCachedSize) {
        @Override
        public int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }
    };

    static ImageLoader uniLoader;

    private static Drawable fetchDrawable(String urlString, ImgSizeType type) {
        try {
            String key = MD5.getMD5(urlString);
            if (type == ImgSizeType.THUMB) {
                synchronized (hardCache) {
                    final Bitmap bitmap = hardCache.get(key);
                    if (bitmap != null) {
                        // Common.d("[IMG] Hard Cache: " + urlString + ":" +
                        // key);
                        return new BitmapDrawable(bitmap);
                    }
                }
            }

            Bitmap imgFromSD = getImageFromSD(urlString, key, type);
            if (imgFromSD != null) {
                // Common.d("[IMG] SD Cache: " + urlString + ":" + key);
                if (type == ImgSizeType.THUMB) {
                    synchronized (hardCache) {
                        hardCache.put(key, imgFromSD);
                    }
                }
                return new BitmapDrawable(imgFromSD);
            }
            downloadImageToSD(urlString, key);
            imgFromSD = getImageFromSD(urlString, key, type);
            return new BitmapDrawable(imgFromSD);
        } catch (Exception e) {
            Common.e(e);
            return null;
        }
    }

    private static class ViewHandler extends Handler {
        private ImageView view;

        public ViewHandler(ImageView view) {
            super(Looper.getMainLooper());
            this.view = view;
        }

        @Override
        public void handleMessage(Message message) {
            view.setImageDrawable((Drawable) message.obj);
            view = null;
            message.obj = null;
        }
    }

    private static Bitmap getImageFromSD(String urlPath, String key, ImgSizeType type) {
        if (key == null || key.length() == 0) {
            return null;
        }

        try {
            File dir = new File(BaseApp.me.getCacheDir(), BaseApp.IMAGE_FOLDER_NAME);
            String fileFullPath = dir + File.separator + key + "_" + Common.urlToFilename(urlPath);
            File targetFile = new File(fileFullPath);
            if (targetFile.exists() && targetFile.canRead()) {
                int size = BaseApp.dpToPx(100);
                if (type == ImgSizeType.ORG) {
                    size = BaseApp.longSide();
                }
                Uri uri = Uri.fromFile(targetFile);
                Bitmap bitmap = ImageTools.getImageFromUri(uri, size);
                Common.d("getImageFromSD: " + bitmap.getWidth() + ":" + bitmap.getHeight() + ":" + size + ":" + urlPath);
                return bitmap;
            } else {
                return null;
            }
        } catch (Exception e) {
            Common.e(e);
        }
        return null;
    }

    private static void downloadImageToSD(String urlPath, String key) {
        try {
            File dir = new File(BaseApp.me.getCacheDir(), BaseApp.IMAGE_FOLDER_NAME);
            if (!dir.exists())
                dir.mkdirs();
            String fileFullPath = dir + File.separator + key + "_" + Common.urlToFilename(urlPath);
            download(urlPath, fileFullPath);
            Common.d("[IMG] Download: " + fileFullPath + ":" + urlPath + ":" + key);
        } catch (Exception e) {
            Common.e(e);
        }
    }

    private static void download(String urlPath, String filePath) throws Exception {
        URL url = new URL(urlPath);
        URLConnection connection = url.openConnection();
        connection.connect();
        int fileLength = connection.getContentLength();
        InputStream input = new BufferedInputStream(url.openStream());
        OutputStream output = new FileOutputStream(filePath);

        byte data[] = new byte[1024];
        long total = 0;
        int count;
        while ((count = input.read(data)) != -1) {
            total += count;
            output.write(data, 0, count);
        }

        output.flush();
        output.close();
        input.close();
    }

}
