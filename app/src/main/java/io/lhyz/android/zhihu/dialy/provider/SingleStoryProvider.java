package io.lhyz.android.zhihu.dialy.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import io.lhyz.android.zhihu.dialy.log.Debug;

/**
 * Created by lhyz on 6/14/2015.
 * Copyright (c)
 */
public class SingleStoryProvider extends ContentProvider {
    private static final String TAG = SingleStoryProvider.class.getCanonicalName();
    private static final String TABLE_STORY = "story";

    public static final String AUTHORITY = TAG;
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_STORY).build();

    public static final String COLUMN_IMAGE_SOURCE = "image_source";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SHARE_URL = "share_url";
    public static final String COLUMN_CSS = "css";
    public static final String COLUMN_BODY = "body";

    private static final String TYPE_SINGLE_STORY = ContentResolver.CURSOR_ITEM_BASE_TYPE + "vnd.provider.singles";

    private static final int ALL_STORY = 1;
    private static final int SINGLE_STORY = 2;
    private static final UriMatcher sUriMatcher;
    private static final Map<String, String> projectionMap;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE_STORY, ALL_STORY);
        sUriMatcher.addURI(AUTHORITY, TABLE_STORY + "/*", SINGLE_STORY);

        projectionMap = new HashMap<>();
        projectionMap.put(COLUMN_IMAGE_SOURCE, COLUMN_IMAGE_SOURCE);
        projectionMap.put(COLUMN_TITLE, COLUMN_TITLE);
        projectionMap.put(COLUMN_IMAGE, COLUMN_IMAGE);
        projectionMap.put(COLUMN_ID, COLUMN_ID);
        projectionMap.put(COLUMN_CSS, COLUMN_CSS);
        projectionMap.put(COLUMN_SHARE_URL, COLUMN_SHARE_URL);
        projectionMap.put(COLUMN_BODY, COLUMN_BODY);
    }

    private DBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDBHelper.getReadableDatabase();
        assert db != null;
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        final int match = sUriMatcher.match(uri);
        if (match == SINGLE_STORY) {
            builder.setTables(TABLE_STORY);
            builder.setProjectionMap(projectionMap);
            builder.appendWhere(COLUMN_ID + "=" + uri.getLastPathSegment());
        } else {
            throw new IllegalArgumentException("Unsupported URI : " + uri);
        }
        return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        if (sUriMatcher.match(uri) == SINGLE_STORY) {
            return TYPE_SINGLE_STORY;
        } else {
            throw new IllegalArgumentException("Unknown URI : " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        long id;

        if (match == ALL_STORY) {
            id = db.insert(TABLE_STORY, null, values);
        } else {
            throw new IllegalArgumentException("Unsupported URI : " + uri);
        }

        if (id > 0) {
            Uri result = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(result, null);
            return result;
        } else {
            Debug.i(TAG, "" + id);
            return null;
        }
//        throw new SQLiteException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private class DBHelper extends SQLiteOpenHelper {

        private static final String CREATE_TABLE_STORY =
                "CREATE TABLE " + TABLE_STORY +
                        "(" + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY,"
                        + COLUMN_IMAGE_SOURCE + " VARCHAR(50) NOT NULL,"
                        + COLUMN_TITLE + " VARCHAR(100) NOT NULL,"
                        + COLUMN_IMAGE + " VARCHAR(100) NOT NULL,"
                        + COLUMN_SHARE_URL + " VARCHAR(100) NOT NULL,"
                        + COLUMN_CSS + " VARCHAR(100) NOT NULL,"
                        + COLUMN_BODY + " TEXT NOT NULL" + ")";

        private static final String DELETE_TABLE_STORY =
                "DROP TABLE IF EXISTS " + TABLE_STORY;

        public DBHelper(Context context) {
            super(context, "stories.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_STORY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DELETE_TABLE_STORY);
            onCreate(db);
        }
    }
}
