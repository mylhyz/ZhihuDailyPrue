package com.lhyz.demo.zhihudialyprue.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *提供最新消息中的stories消息
 */
public class StoryProvider extends ContentProvider {
    private static final String TAG = StoryProvider.class.getCanonicalName();

    public static final String TABLE_STORY = "stories";

    public static final String COLUMN_IMAGE = "images";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_GA_PREFIX = "ga_prefix";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_MULTIPIC = "multipic";

    private static final String DEFAULT_ORDER = COLUMN_GA_PREFIX + " DESC";

    public static final String AUTHORITY = StoryProvider.class.getCanonicalName();
    //这个算作默认能够查询到的URI
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_STORY).build();

    private static final int ALL_STORY = 1;
    private static final int SINGLE_STORY = 2;

    private static final String TYPE_ALL_STORY = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.provider.stories";
    private static final String TYPE_SINGLE_STORY = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.provider.stories";

    private static final UriMatcher sUriMatcher;
    private static final Map<String, String> projectionMap;

    private DBHelper mDBHelper;

    static {
        //定义能够匹配到的URI，分别对应查询单个和查询所有STORY的URI，查询单个查询所有HOT的URI
        //，初步定义成只能使用唯一的id作为单个匹配的时候的的参数
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE_STORY, ALL_STORY);
        sUriMatcher.addURI(AUTHORITY, TABLE_STORY + "/*", SINGLE_STORY);

        projectionMap = new HashMap<>();
        projectionMap.put(COLUMN_IMAGE, COLUMN_IMAGE);
        projectionMap.put(COLUMN_ID, COLUMN_ID);
        projectionMap.put(COLUMN_GA_PREFIX, COLUMN_GA_PREFIX);
        projectionMap.put(COLUMN_TITLE, COLUMN_TITLE);
    }

    @Override
    public boolean onCreate() {
        mDBHelper = DBHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_STORY);
        qb.setProjectionMap(projectionMap);

        switch (match) {
            case ALL_STORY:
                break;
            case SINGLE_STORY:
                qb.appendWhere(COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }

        final SQLiteDatabase db = mDBHelper.getReadableDatabase();
        assert db != null;
        Cursor result = qb.query(db, projection, selection, selectionArgs, null, null, DEFAULT_ORDER);
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ALL_STORY:
                return TYPE_ALL_STORY;
            case SINGLE_STORY:
                return TYPE_SINGLE_STORY;
            default:
                throw new IllegalArgumentException("Unsupported URI : " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        long id;

        switch (match) {
            case ALL_STORY:
                id = db.insert(TABLE_STORY, null, values);
                break;
            case SINGLE_STORY:
                throw new IllegalArgumentException("Unsupported URI : " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }
        if (id > 0) {
            Uri result = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(result, null);
            return result;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        int result;

        switch (match) {
            case ALL_STORY:
                result = db.delete(TABLE_STORY, selection, selectionArgs);
                break;
            case SINGLE_STORY:
                String id = uri.getLastPathSegment();
                result = db.delete(TABLE_STORY,
                        COLUMN_ID
                                + "="
                                + id
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        int result;

        switch (match) {
            case ALL_STORY:
                result = db.update(TABLE_STORY, values, selection, selectionArgs);
                break;
            case SINGLE_STORY:
                result = db.update(TABLE_STORY, values,
                        COLUMN_ID
                                + "="
                                + uri.getLastPathSegment()
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }
}
