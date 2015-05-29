package com.lhyz.demo.zhihudialyprue.datebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lhyz.demo.zhihudialyprue.bean.StoryHot;
import com.lhyz.demo.zhihudialyprue.bean.StorySimple;
import com.lhyz.demo.zhihudialyprue.bean.StoryToday;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private static DataSource instance;

    private String[] allColumns1={
            DBHelper.COLUMN_IMAGE,
            DBHelper.COLUMN_TITLE,
            DBHelper.COLUMN_GA_PREFIX
    };

    private String[] allColumns2={
            DBHelper.COLUMN_IMAGES,
            DBHelper.COLUMN_TITLE,
            DBHelper.COLUMN_GA_PREFIX
    };

    /**
     * 使用这个实例化的方法的好处是必须从Application中创建，
     * 因为此方法的context虽然在不同的Context下被传入的不一，但是因为这是一个单例
     * 所以整个过程中只有一个通过Application实例化的instance，因此只要程序不关闭，
     * 此instance的context就不会失效。
     * @param context 外部传入的内部辅助参数context
     * @return 返回单例
     */
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
     * 插入今日新闻数据,全部代码段都被对象锁锁住，
     * 因此在执行插入数据期间其他线程不得访问此对象其他方法，
     * 且由于本
     * @param storyTodays 从外部线程传入的数据
     */
    public void insertNewFromToday(List<StoryToday> storyTodays){
        synchronized (this) {
            ContentValues cv = new ContentValues();
            for (StoryToday story : storyTodays) {
                cv.put(DBHelper.COLUMN_IMAGES, story.getImages()[0]);
                cv.put(DBHelper.COLUMN_MULTIPIC, story.getMultipic());
                cv.put(DBHelper.COLUMN_TYPE, story.getType());
                cv.put(DBHelper.COLUMN_ID, story.getId());
                cv.put(DBHelper.COLUMN_GA_PREFIX, story.getGa_prefix());
                cv.put(DBHelper.COLUMN_TITLE, story.getTitle());
                database.insert(DBHelper.TABLE_STORY_TODAY_NAME, null, cv);
                cv.clear();
            }
        }
    }

    /**
     * 插入今日热点数据
     * @param storyHots 从其他线程获取的数据
     */
    public void insertNewFromHot(List<StoryHot> storyHots){
        synchronized (this) {
            ContentValues cv = new ContentValues();
            for (StoryHot hot : storyHots) {
                cv.put(DBHelper.COLUMN_IMAGE, hot.getImage());
                cv.put(DBHelper.COLUMN_TYPE, hot.getType());
                cv.put(DBHelper.COLUMN_ID, hot.getId());
                cv.put(DBHelper.COLUMN_GA_PREFIX, hot.getGa_prefix());
                cv.put(DBHelper.COLUMN_TITLE, hot.getTitle());
                database.insert(DBHelper.TABLE_STORY_HOT_NAME, null, cv);
                cv.clear();
            }
        }
    }

    public List<StorySimple> queryTodays(){
        List<StorySimple> simples = new LinkedList<>();
        Cursor cursor = database.query(DBHelper.TABLE_STORY_TODAY_NAME,allColumns2,null,null,null,null,DBHelper.COLUMN_GA_PREFIX+" DESC");
        int index1 = cursor.getColumnIndex("images");
        int index2 = cursor.getColumnIndex("title");
        StorySimple storySimple = new StorySimple();
        cursor.moveToFirst();
        storySimple.setImage(cursor.getString(index1));
        storySimple.setTitle(cursor.getString(index2));
        simples.add(storySimple);
        while(cursor.moveToNext()){
            storySimple = new StorySimple();
            storySimple.setImage(cursor.getString(index1));
            storySimple.setTitle(cursor.getString(index2));
            simples.add(storySimple);
        }
        cursor.close();
        return simples;
    }

    public List<StorySimple> queryHots(){
        List<StorySimple> simples = new LinkedList<>();
        Cursor cursor = database.query(DBHelper.TABLE_STORY_HOT_NAME, allColumns1, null, null, null, null, DBHelper.COLUMN_GA_PREFIX + " DESC");
        if(cursor == null){
            return null;
        }
        int index1 = cursor.getColumnIndex("image");
        int index2 = cursor.getColumnIndex("title");
        StorySimple storySimple = new StorySimple();
        cursor.moveToFirst();
        storySimple.setImage(cursor.getString(index1));
        storySimple.setTitle(cursor.getString(index2));
        simples.add(storySimple);
        while(cursor.moveToNext()){
            storySimple = new StorySimple();
            storySimple.setImage(cursor.getString(index1));
            storySimple.setTitle(cursor.getString(index2));
            simples.add(storySimple);
        }
        cursor.close();
        return simples;
    }
}
