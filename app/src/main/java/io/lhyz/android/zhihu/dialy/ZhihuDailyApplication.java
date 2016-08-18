package io.lhyz.android.zhihu.dialy;

import android.app.Application;
import android.content.Intent;

import io.lhyz.android.zhihu.dialy.provider.DBHelper;
import io.lhyz.android.zhihu.dialy.service.UpdateService;

public class ZhihuDailyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.getInstance(this);
        startService(new Intent(this, UpdateService.class));
    }
}
