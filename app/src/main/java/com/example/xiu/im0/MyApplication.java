package com.example.xiu.im0;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by xiu on 16/3/25.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // leanCloud初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "V3o5V2M27mCoAwRx0P5xLulA-gzGzoHsz", "lkDiLfRgYh1B15YdYJEz0p2P");

    }
}
