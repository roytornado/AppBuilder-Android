package com.royalnext.base.demo;

import android.widget.ImageView;

import com.midland.base.R;
import com.royalnext.base.app.BaseApp;
import com.royalnext.base.util.ImageLoader;
import com.royalnext.base.util.network.ServerTaskListener;
import com.royalnext.base.util.network.ServerTaskManager;

import java.util.HashMap;

/**
 * Created by royng on 17/4/14.
 */
public class SimpleDemo {


    ServerTaskManager stm;
    public void demoLoadImages() {
        String url = "http://image.com/1234";
        ImageView imageView = new ImageView(BaseApp.me);
        ImageLoader.getLoader().loadImgOtherThread(url,imageView, ImageLoader.ImgSizeType.THUMB, R.drawable.ic_image);


        stm.startTask("Host",new HashMap<String, String>(),new ServerTaskListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(String result, String code, String msg) {

            }
        });

    }
}
