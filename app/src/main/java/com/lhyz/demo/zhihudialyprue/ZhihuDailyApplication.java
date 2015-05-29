package com.lhyz.demo.zhihudialyprue;

import android.app.Application;

import com.lhyz.demo.zhihudialyprue.datebase.DataSource;

import java.sql.SQLException;

public class ZhihuDailyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            DataSource.getInstance(this).open();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
