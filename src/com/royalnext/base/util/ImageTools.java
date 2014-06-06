package com.royalnext.base.util;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.royalnext.base.app.BaseApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImageTools {

    public static Bitmap getImageFromUri(Uri uri, int maxSize) {
        BitmapFactory.Options o = null;
        InputStream stream = null;
        try {
            //Common.d("uri path: " + uri.getPath());
            o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            stream = BaseApp.me.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(stream, null, o);
            stream.close();

            int heightRatio = (int) Math.ceil((float) o.outHeight / (float) maxSize);
            int widthRatio = (int) Math.ceil((float) o.outWidth / (float) maxSize);
            int scale = 1;
            if ((heightRatio > 1) && (widthRatio > 1)) {
                if (heightRatio > widthRatio) {
                    scale = heightRatio;
                } else {
                    scale = widthRatio;
                }
            }
            // Common.d("Before resize: " + o.outWidth + ":" + o.outHeight);
            // Common.d("Scale: " + scale);
            o = new BitmapFactory.Options();
            o.inSampleSize = scale;
            o.inDither = false;
            stream = BaseApp.me.getContentResolver().openInputStream(uri);
            Bitmap bScale = BitmapFactory.decodeStream(stream, null, o);
            stream.close();

            int longSide = bScale.getWidth() > bScale.getHeight() ? bScale.getWidth() : bScale.getHeight();
            float ratio = (float) longSide / (float) maxSize;

            Bitmap bResize = null;
            if (longSide > maxSize) {
                bResize = Bitmap.createScaledBitmap(bScale, (int) (bScale.getWidth() / ratio), (int) (bScale.getHeight() / ratio), true);
            } else {
                bResize = Bitmap.createBitmap(bScale);
            }

            int degrees = 0;
            String realPath = getRealPathFromURI(uri);
            if (realPath != null) {
                ExifInterface exif = new ExifInterface(realPath);
                int orientation1 = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                if (orientation1 != 0) {
                    switch (orientation1) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            degrees = 270;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            degrees = 180;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            degrees = 90;
                    }
                }
            }

            int orientation2 = getOrientation(uri);
            if (orientation2 != 0) {
                degrees = orientation2;
            }
            // Common.d("degree : " + degrees);

            o = new BitmapFactory.Options();
            o.inPurgeable = true;
            o.inInputShareable = true;
            Matrix mtx = new Matrix();
            mtx.postRotate(degrees);
            Bitmap bRotate = Bitmap.createBitmap(bResize, 0, 0, bResize.getWidth(), bResize.getHeight(), mtx, true);
            // Common.d("After resize: " + bRotate.getWidth() + ":" +
            // bRotate.getHeight());
            return bRotate;
        } catch (Exception e) {
            Common.e(e);
        } finally {
        }
        return null;
    }

    public static Uri saveToFile(Bitmap bitmap, File dir, String fileName, int quality) {
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File targetFile = new File(dir, fileName + ".jpg");
            FileOutputStream out = new FileOutputStream(targetFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
            out.close();
            return Uri.fromFile(targetFile);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRealPathFromURI(Uri uri) {
        try {
            Cursor cursor = BaseApp.me.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (cursor.moveToFirst()) {
                Common.d("Real Path: " + cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                String result = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                cursor.close();
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static int getOrientation(Uri uri) {
        try {
            Cursor cursor = BaseApp.me.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);
            if (cursor.moveToFirst()) {
                int result = cursor.getInt(0);
                cursor.close();
                return result;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static Bitmap getBitmap(int res, int size) {
        Bitmap BitmapOrg = BitmapFactory.decodeResource(BaseApp.me.getResources(), res);
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();

        int longSide = width > height ? width : height;
        float ratio = (float) longSide / (float) size;
        Bitmap bResize = null;
        if (longSide > size) {
            bResize = Bitmap.createScaledBitmap(BitmapOrg, (int) (BitmapOrg.getWidth() / ratio), (int) (BitmapOrg.getHeight() / ratio), true);
        } else {
            bResize = Bitmap.createBitmap(BitmapOrg);
        }
        return bResize;
    }

    public static Drawable getDrawable(int res, int size) {
        Bitmap bitmapOrg = BitmapFactory.decodeResource(BaseApp.me.getResources(), res);
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();

        int longSide = width > height ? width : height;
        float ratio = (float) longSide / (float) size;
        Bitmap bResize = null;
        if (longSide > size) {
            bResize = Bitmap.createScaledBitmap(bitmapOrg, (int) (bitmapOrg.getWidth() / ratio), (int) (bitmapOrg.getHeight() / ratio), true);
        } else {
            bResize = Bitmap.createBitmap(bitmapOrg);
        }
        return new BitmapDrawable(bResize);
    }

    public static Drawable getDrawableWithBounds(int res, int size) {
        Drawable drawableOrg = BaseApp.me.getResources().getDrawable(res);
        int width = drawableOrg.getIntrinsicWidth();
        int height = drawableOrg.getIntrinsicHeight();

        int longSide = width > height ? width : height;
        float ratio = (float) longSide / (float) size;
        drawableOrg.setBounds(0, 0, (int) (width / ratio), (int) (height / ratio));
        Common.i("getDrawableWithBounds: " + drawableOrg.getBounds().width() + ":" + drawableOrg.getBounds().height());
        return drawableOrg;
    }

    public static Bitmap rotateLeft(Bitmap bitmap) {
        Matrix mtx = new Matrix();
        mtx.postRotate(-90);
        Bitmap bRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
        return bRotate;
    }

    public static Bitmap rotateRight(Bitmap bitmap) {
        Matrix mtx = new Matrix();
        mtx.postRotate(90);
        Bitmap bRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
        return bRotate;
    }

}
