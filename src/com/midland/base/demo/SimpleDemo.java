package com.midland.base.demo;

import android.widget.ImageView;

import com.midland.base.R;
import com.midland.base.app.BaseApp;
import com.midland.base.util.ImageLoader;

/**
 * Created by royng on 17/4/14.
 */
public class SimpleDemo {


    public void demoLoadImages() {
        String url = "http://image.com/1234";
        ImageView imageView = new ImageView(BaseApp.me);
        ImageLoader.getLoader().loadImgOtherThread(url,imageView, ImageLoader.ImgSizeType.THUMB, R.drawable.ic_image);

    }
}
