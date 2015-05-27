package com.lhyz.demo.zhihudialyprue;

import android.app.Application;

import com.lhyz.demo.zhihudialyprue.datebase.DataSource;
import com.lhyz.demo.zhihudialyprue.task.DownloadTask;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class ZhihuDailyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            DataSource.getInstance(this).open();
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            DownloadTask.getInstance(this,Constants.TODAY_STORIES_URL).loadingBackground();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }
}
