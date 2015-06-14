package com.lhyz.demo.zhihudialyprue.loader;

import android.content.Context;
import android.database.Cursor;

import com.lhyz.demo.zhihudialyprue.adapter.LabsPageStateAdapter;
import com.lhyz.demo.zhihudialyprue.log.Debug;
import com.lhyz.demo.zhihudialyprue.provider.TopStoryProvider;

public class TopStoryLoader extends DataLoader<Cursor> {
    private static final String TAG = TopStoryLoader.class.getCanonicalName();

    public TopStoryLoader(Context context) {
        super(context);
    }

    @Override
    protected Cursor loadData() {
        Cursor cursor = mContext.getContentResolver().query(TopStoryProvider.CONTENT_URI, LabsPageStateAdapter.projection,null,null,null);
        if(cursor == null){
            Debug.i(TAG,"Could not query data from TopStoryProvider");
        }
        return cursor;
    }

    @Override
    public Cursor loadInBackground() {
        return loadData();
    }
}