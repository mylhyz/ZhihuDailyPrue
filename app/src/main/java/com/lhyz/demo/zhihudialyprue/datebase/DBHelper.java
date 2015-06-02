package com.lhyz.demo.zhihudialyprue.datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME="meta.db";
    private static final int DB_VERSION = 2;

    public static final String TABLE_STORY_TODAY_NAME = "story_today";
    public static final String TABLE_STORY_HOT_NAME ="story_hot";
    public static final String TABLE_STORY_NAME = "story";

    public static final String COLUMN_IMAGES="images";
    public static final String COLUMN_IMAGE="image";
    public static final String COLUMN_MULTIPIC="multipic";
    public static final String COLUMN_TYPE="type";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_GA_PREFIX="ga_prefix";
    public static final String COLUMN_TITLE="title";
    public static final String COLUMN_SOURCE = "image_source";
    public static final String COLUMN_BODY = "body";



    private static final String CREATE_TABLE_STORY_TODAY=
            "CREATE TABLE "+ TABLE_STORY_TODAY_NAME +
                    "("+
                    COLUMN_IMAGES+" VARCHAR(100) NOT NULL,"+
                    COLUMN_MULTIPIC+" VARCHAR(10),"+
                    COLUMN_TYPE+" INTEGER NOT NULL,"+
                    COLUMN_ID+" INTEGER NOT NULL,"+
                    COLUMN_GA_PREFIX+" INTEGER UNIQUE NOT NULL,"+
                    COLUMN_TITLE+" VARCHAR(100) NOT NULL" +
                    ")";

    private static final String CREATE_TABLE_STORY_HOT =
            "CREATE TABLE "+ TABLE_STORY_HOT_NAME +
                    "("+
                    COLUMN_IMAGE+" VARCHAR(100) NOT NULL,"+
                    COLUMN_TYPE+" INTEGER NOT NULL,"+
                    COLUMN_ID+" INTEGER UNIQUE NOT NULL,"+
                    COLUMN_GA_PREFIX+" INTEGER UNIQUE NOT NULL,"+
                    COLUMN_TITLE+" VARCHAR(100) NOT NULL" +
                    ")";

    private static final String CREATE_TABLE_STORY =
            "CREATE TABLE "+ TABLE_STORY_NAME +
                    "("+
                    COLUMN_IMAGE+" VARCHAR(100) NOT NULL,"+
                    COLUMN_SOURCE+" VARCHAR(100) NOT NULL,"+
                    COLUMN_TITLE+" VARCHAR(100) NOT NULL,"+
                    COLUMN_ID+" VARCHAR(20) UNIQUE NOT NULL,"+
                    COLUMN_BODY+" TEXT NOT NULL" +
                    ")";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STORY_TODAY);
        db.execSQL(CREATE_TABLE_STORY_HOT);
        db.execSQL(CREATE_TABLE_STORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_STORY_HOT_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_STORY_TODAY_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_STORY_NAME);
        onCreate(db);
    }
}
