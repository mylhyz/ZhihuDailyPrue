package com.lhyz.demo.zhihudialyprue.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME="dailies.db";
    private static final int DB_VERSION = 1;

    private static DBHelper instance;

    public static DBHelper getInstance(Context context){
        if(instance==null){
            instance = new DBHelper(context);
        }
        return instance;
    }

    private static final String CREATE_TABLE_STORY =
            "CREATE TABLE "+ StoryProvider.TABLE_STORY
            +"("+StoryProvider.COLUMN_IMAGE+" VARCHAR(100) NOT NULL,"
            +StoryProvider.COLUMN_ID+" INTEGER PRIMARY KEY,"
            +StoryProvider.COLUMN_GA_PREFIX+" VARCHAR(10) NOT NULL,"
            +StoryProvider.COLUMN_MULTIPIC+" VARCHAR(10),"
            +StoryProvider.COLUMN_TITLE+" TEXT NOT NULL"+")";

    private static final String CREATE_TABLE_TOP_STORY =
            "CREATE TABLE "+ TopStoryProvider.TABLE_TOP_STORY
                    +"("+TopStoryProvider.COLUMN_IMAGE+" VARCHAR(100) NOT NULL,"
                    +TopStoryProvider.COLUMN_ID+" INTEGER PRIMARY KEY,"
                    +TopStoryProvider.COLUMN_GA_PREFIX+" VARCHAR(10) NOT NULL,"
                    +TopStoryProvider.COLUMN_TITLE+" TEXT NOT NULL"+")";

    private static final String DELETE_TABLE_STORY =
            "DROP TABLE IF EXISTS "+StoryProvider.TABLE_STORY;

    private static final String DELETE_TABLE_TOP_STORY =
            "DROP TABLE IF EXISTS "+TopStoryProvider.TABLE_TOP_STORY;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STORY);
        db.execSQL(CREATE_TABLE_TOP_STORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_STORY);
        db.execSQL(DELETE_TABLE_TOP_STORY);
        onCreate(db);
    }
}
