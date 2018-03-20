package com.example.demo.autolayout;

import android.app.Application;

import com.yolanda.nohttp.NoHttp;

/**
 * Created by 59246 on 2018/3/19.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
    }
}
