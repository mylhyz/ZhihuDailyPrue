package io.lhyz.android.zhihu.dialy.loader;

import android.content.Context;
import android.database.Cursor;

import io.lhyz.android.zhihu.dialy.adapter.LabsPageStateAdapter;
import io.lhyz.android.zhihu.dialy.log.Debug;
import io.lhyz.android.zhihu.dialy.provider.TopStoryProvider;

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