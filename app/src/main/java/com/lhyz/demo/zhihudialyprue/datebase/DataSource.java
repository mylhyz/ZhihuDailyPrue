package com.lhyz.demo.zhihudialyprue.datebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lhyz.demo.zhihudialyprue.bean.StoryDetail;
import com.lhyz.demo.zhihudialyprue.bean.StoryHot;
import com.lhyz.demo.zhihudialyprue.bean.StorySimple;
import com.lhyz.demo.zhihudialyprue.bean.StoryToday;
import com.lhyz.demo.zhihudialyprue.util.DateUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private static DataSource instance;

    private String[] allColumns1={
            DBHelper.COLUMN_IMAGE,
            DBHelper.COLUMN_TITLE,
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_GA_PREFIX
    };

    private String[] allColumns2={
            DBHelper.COLUMN_IMAGES,
            DBHelper.COLUMN_TITLE,
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_GA_PREFIX
    };

    private String[] column_id={
            DBHelper.COLUMN_ID
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
     * 因此在执行插入数据期间其他线程不得访问此对象其他方法
     * 使用Application作为Context实例化的单例作为全局都是唯一的单例
     * @param storyTodays 从外部线程传入的数据
     */
    public void insertNewFromToday(List<StoryToday> storyTodays) {
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

    /**
     * 插入今日热点数据
     * @param storyHots 从其他线程获取的数据
     */
    public void insertNewFromHot(List<StoryHot> storyHots) {
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

    /**
     * 插入每条新闻的详细界面
     * @param detail 点击一个Item之后进入细节页，会缓存得到的数据，然后下次每次进入都会检查是否存在
     */
    public void insertNewDetail(StoryDetail detail) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_IMAGE, detail.getImage());
        cv.put(DBHelper.COLUMN_SOURCE, detail.getImage_source());
        cv.put(DBHelper.COLUMN_TITLE, detail.getTitle());
        cv.put(DBHelper.COLUMN_ID,detail.getId());
        cv.put(DBHelper.COLUMN_SHARE_URL,detail.getShare_url());
        cv.put(DBHelper.COLUMN_BODY, detail.getBody());
        database.insert(DBHelper.TABLE_STORY_NAME, null, cv);
    }

    public void moveData(){
        List<StoryToday> todays = new ArrayList<>();
        StoryToday today;

        Cursor cursor = database.query(DBHelper.TABLE_STORY_TODAY_NAME, null, null, null, null, null, null, null);

        int index1 = cursor.getColumnIndex(DBHelper.COLUMN_IMAGES);
        int index2 = cursor.getColumnIndex(DBHelper.COLUMN_MULTIPIC);
        int index3 = cursor.getColumnIndex(DBHelper.COLUMN_TYPE);
        int index4 = cursor.getColumnIndex(DBHelper.COLUMN_ID);
        int index5 = cursor.getColumnIndex(DBHelper.COLUMN_GA_PREFIX);
        int index6 = cursor.getColumnIndex(DBHelper.COLUMN_TITLE);

        cursor.moveToFirst();
        today = new StoryToday();
        today.setImages(new String[]{cursor.getString(index1)});
        today.setMultipic(cursor.getString(index2));
        today.setType(cursor.getString(index3));
        today.setId(cursor.getString(index4));
        today.setGa_prefix(cursor.getString(index5));
        today.setTitle(cursor.getString(index6));
        todays.add(today);

        while (cursor.moveToNext()){
            today = new StoryToday();
            today.setImages(new String[]{cursor.getString(index1)});
            today.setMultipic(cursor.getString(index2));
            today.setType(cursor.getString(index3));
            today.setId(cursor.getString(index4));
            today.setGa_prefix(cursor.getString(index5));
            today.setTitle(cursor.getString(index6));
            todays.add(today);
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        for(StoryToday item : todays){
            cv.put(DBHelper.COLUMN_DATE, DateUtil.getYesToday());
            cv.put(DBHelper.COLUMN_IMAGES,item.getImages()[0]);
            cv.put(DBHelper.COLUMN_TYPE, item.getType());
            cv.put(DBHelper.COLUMN_ID, item.getId());
            cv.put(DBHelper.COLUMN_GA_PREFIX, item.getGa_prefix());
            cv.put(DBHelper.COLUMN_TITLE, item.getTitle());
            database.insert(DBHelper.TABLE_STORY_LAST_NAME,null,cv);
            cv.clear();
        }

        deleteForm();
    }

    private void deleteForm(){
        database.execSQL("DROP TABLE IF EXISTS "+ DBHelper.TABLE_STORY_TODAY_NAME);
        database.execSQL(DBHelper.RECREATE_TABLE_STORY_TODAY);
    }

    public List<StorySimple> queryTodays(){
        List<StorySimple> simples = new LinkedList<>();
        Cursor cursor = database.query(DBHelper.TABLE_STORY_TODAY_NAME,allColumns2,null,null,null,null,DBHelper.COLUMN_GA_PREFIX+" DESC");
        int index1 = cursor.getColumnIndex("images");
        int index2 = cursor.getColumnIndex("title");
        int index3 = cursor.getColumnIndex("id");
        StorySimple storySimple = new StorySimple();
        cursor.moveToFirst();
        storySimple.setImage(cursor.getString(index1));
        storySimple.setTitle(cursor.getString(index2));
        storySimple.setId(cursor.getString(index3));
        simples.add(storySimple);
        while(cursor.moveToNext()){
            storySimple = new StorySimple();
            storySimple.setImage(cursor.getString(index1));
            storySimple.setTitle(cursor.getString(index2));
            storySimple.setId(cursor.getString(index3));
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
        int index3 = cursor.getColumnIndex("id");
        StorySimple storySimple = new StorySimple();
        cursor.moveToFirst();
        storySimple.setImage(cursor.getString(index1));
        storySimple.setTitle(cursor.getString(index2));
        storySimple.setId(cursor.getString(index3));
        simples.add(storySimple);
        while(cursor.moveToNext()){
            storySimple = new StorySimple();
            storySimple.setImage(cursor.getString(index1));
            storySimple.setTitle(cursor.getString(index2));
            storySimple.setId(cursor.getString(index3));
            simples.add(storySimple);
        }
        cursor.close();
        return simples;
    }

    public boolean hasStory(String id){
        Cursor cursor = database.query(DBHelper.TABLE_STORY_NAME, column_id, DBHelper.COLUMN_ID + " = ? ", new String[]{id}, null, null, null, "1");
        if(cursor.getCount() == 1){
            cursor.close();
            return true;
        }
        return false;
    }

    public StoryDetail getStory(String id){
        StoryDetail detail = new StoryDetail();
        Cursor cursor = database.query(DBHelper.TABLE_STORY_NAME, null, DBHelper.COLUMN_ID + " = ? ", new String[]{id}, null, null, null,null);

        int index1 = cursor.getColumnIndex(DBHelper.COLUMN_IMAGE);
        int index2 = cursor.getColumnIndex(DBHelper.COLUMN_SOURCE);
        int index3 = cursor.getColumnIndex(DBHelper.COLUMN_TITLE);
        int index4 = cursor.getColumnIndex(DBHelper.COLUMN_ID);
        int index5 = cursor.getColumnIndex(DBHelper.COLUMN_SHARE_URL);
        int index6 = cursor.getColumnIndex(DBHelper.COLUMN_BODY);

        cursor.moveToFirst();
        detail.setImage(cursor.getString(index1));
        detail.setImage_source(cursor.getString(index2));
        detail.setTitle(cursor.getString(index3));
        detail.setId(cursor.getString(index4));
        detail.setShare_url(cursor.getString(index5));
        detail.setBody(cursor.getString(index6));

        cursor.close();
        return detail;
    }
}
