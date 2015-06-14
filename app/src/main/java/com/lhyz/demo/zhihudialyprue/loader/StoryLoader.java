package com.lhyz.demo.zhihudialyprue.loader;

import android.content.Context;
import android.database.Cursor;

import com.lhyz.demo.zhihudialyprue.adapter.NewsListAdapter;
import com.lhyz.demo.zhihudialyprue.log.Debug;
import com.lhyz.demo.zhihudialyprue.provider.StoryProvider;

public class StoryLoader extends DataLoader<Cursor> {
    private static final String TAG  = StoryLoader.class.getCanonicalName();

    public StoryLoader(Context context) {
        super(context);
    }

    @Override
    protected Cursor loadData() {
        Cursor cursor = mContext.getContentResolver().query(StoryProvider.CONTENT_URI, NewsListAdapter.projection,null,null,null);
        if(cursor==null){
            Debug.i(TAG,"Could not load data from StoryProvider . ");
        }
        return cursor;
    }

    @Override
    public Cursor loadInBackground() {
        return loadData();
    }
}