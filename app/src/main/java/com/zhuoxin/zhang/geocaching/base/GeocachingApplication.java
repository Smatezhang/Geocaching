package com.zhuoxin.zhang.geocaching.base;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.zhuoxin.zhang.geocaching.entity.UserPrefs;

/**
 * Created by Administrator on 2017/8/29.
 */

public class GeocachingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UserPrefs.init(getApplicationContext());
        //百度地图初始化 SDK
        SDKInitializer.initialize(getApplicationContext());
    }
}
