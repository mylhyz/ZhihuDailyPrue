package com.lhyz.demo.zhihudialyprue.datebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lhyz.demo.zhihudialyprue.bean.StoryHot;
import com.lhyz.demo.zhihudialyprue.bean.StoryToday;

import java.sql.SQLException;
import java.util.List;

public class DataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private static DataSource instance;

    public static DataSource getInstance(Context context) {
        if(instance==null){
            instance = new DataSource(context);
        }
        return instance;
    }

    /**
     * 实例化数据库管理器
     * @param context 外部context
     */
    private DataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    /***
     * 打开数据库
     * @throws SQLException 数据库打开异常
     */
    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    /**
     * 插入今日新闻数据
     * @param storyTodays 从外部线程传入的数据
     */
    public void insertNewFromToday(List<StoryToday> storyTodays){
        ContentValues cv = new ContentValues();
        for(StoryToday story : storyTodays){
            cv.put(DBHelper.COLUMN_IMAGES,story.getImages()[0]);
            cv.put(DBHelper.COLUMN_MULTIPIC,story.getMultipic());
            cv.put(DBHelper.COLUMN_TYPE,story.getType());
            cv.put(DBHelper.COLUMN_ID,story.getId());
            cv.put(DBHelper.COLUMN_GA_PREFIX,story.getGa_prefix());
            cv.put(DBHelper.COLUMN_TITLE, story.getTitle());
            database.insert(DBHelper.TABLE_STORY_TODAY_NAME, null, cv);
            cv.clear();
        }
    }

    /**
     * 插入今日热点数据
     * @param storyHots 从其他线程获取的数据
     */
    public void insertNewFromHot(List<StoryHot> storyHots){
        ContentValues cv = new ContentValues();
        for(StoryHot hot : storyHots){
            cv.put(DBHelper.COLUMN_IMAGE,hot.getImage());
            cv.put(DBHelper.COLUMN_TYPE,hot.getType());
            cv.put(DBHelper.COLUMN_ID,hot.getId());
            cv.put(DBHelper.COLUMN_GA_PREFIX,hot.getGa_prefix());
            cv.put(DBHelper.COLUMN_TITLE,hot.getTitle());
            database.insert(DBHelper.TABLE_STORY_HOT_NAME, null, cv);
            cv.clear();
        }
    }
}
