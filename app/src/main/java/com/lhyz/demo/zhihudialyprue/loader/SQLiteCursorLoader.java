package com.lhyz.demo.zhihudialyprue.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

/**
 * 暂时没有用到此类，之后将用于本地数据库数据异步加载
 */
public abstract class SQLiteCursorLoader extends AsyncTaskLoader<Cursor>{

    private Cursor mCursor;

    public SQLiteCursorLoader(Context context) {
        super(context);
    }

    /**
     * 通过第三方代码加载Cursor
     * @return 查询到的Cursor
     */
    protected abstract Cursor loadCursor();

    /**
     * 获取Cursor
     * @return 返回获取到的Cursor
     */
    @Override
    public Cursor loadInBackground() {
        Cursor cursor = loadCursor();
        if(cursor!=null){
            cursor.getCount();
        }
        return cursor;
    }

    /**
     * 判断是否loader已经启动，确保old的cursor可以被关闭
     * @param data 新获取到的数据
     */
    @Override
    public void deliverResult(Cursor data) {
        Cursor oldCursor = mCursor;
        mCursor = data;

        if(isStarted()){
            super.deliverResult(data);
        }
        if(oldCursor != null && oldCursor != data && !oldCursor.isClosed()){
            oldCursor.close();
        }

    }

    @Override
    protected void onStartLoading() {
        if(mCursor != null){
            deliverResult(mCursor);
        }
        if(takeContentChanged() || mCursor == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        if(data != null && !data.isClosed()){
            data.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if(mCursor != null && !mCursor.isClosed()){
            mCursor.close();
        }
        mCursor = null;
    }
}
