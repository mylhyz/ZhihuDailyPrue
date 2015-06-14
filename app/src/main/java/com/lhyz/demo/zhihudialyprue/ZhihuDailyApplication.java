package com.lhyz.demo.zhihudialyprue;

import android.app.Application;

import com.lhyz.demo.zhihudialyprue.provider.DBHelper;

public class ZhihuDailyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.getInstance(this);
    }
}
