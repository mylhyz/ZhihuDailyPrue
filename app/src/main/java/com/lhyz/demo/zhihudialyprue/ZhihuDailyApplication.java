package com.lhyz.demo.zhihudialyprue;

import android.app.Application;
import android.content.Intent;

import com.lhyz.demo.zhihudialyprue.provider.DBHelper;
import com.lhyz.demo.zhihudialyprue.service.UpdateService;

public class ZhihuDailyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.getInstance(this);
        startService(new Intent(this, UpdateService.class));
    }
}
