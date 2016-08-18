package io.lhyz.android.zhihu.dialy.loader;

import android.content.Context;
import android.database.Cursor;

import io.lhyz.android.zhihu.dialy.adapter.NewsListAdapter;
import io.lhyz.android.zhihu.dialy.log.Debug;
import io.lhyz.android.zhihu.dialy.provider.StoryProvider;


public class StoryLoader extends DataLoader<Cursor> {
    private static final String TAG = StoryLoader.class.getCanonicalName();

    public StoryLoader(Context context) {
        super(context);
    }

    @Override
    protected Cursor loadData() {
        Cursor cursor = mContext.getContentResolver().query(StoryProvider.CONTENT_URI, NewsListAdapter.projection, null, null, null);
        if (cursor == null) {
            Debug.i(TAG, "Could not load data from StoryProvider . ");
        }
        return cursor;
    }

    @Override
    public Cursor loadInBackground() {
        return loadData();
    }
}