package com.lhyz.demo.zhihudialyprue.loader;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.lhyz.demo.zhihudialyprue.bean.StoryDetail;
import com.lhyz.demo.zhihudialyprue.provider.SingleStoryProvider;

public class SingleStoryLoader extends DataLoader<StoryDetail> {

    private String id;

    public SingleStoryLoader(Context context, String id) {
        super(context);
        this.id = id;
    }

    @Override
    protected StoryDetail loadData() {
        Cursor cursor = getContext().getContentResolver().query(ContentUris.withAppendedId(SingleStoryProvider.CONTENT_URI,Integer.parseInt(id)),null,null,null,null);
        if(cursor==null){
            throw new SQLiteException("Failed to query story : has no data with id:"+id);
        }
        cursor.moveToFirst();
        StoryDetail detail = new StoryDetail();
        detail.setBody(cursor.getString(cursor.getColumnIndex(SingleStoryProvider.COLUMN_BODY)));
        detail.setImage_source(cursor.getString(cursor.getColumnIndex(SingleStoryProvider.COLUMN_IMAGE_SOURCE)));
        detail.setTitle(cursor.getString(cursor.getColumnIndex(SingleStoryProvider.COLUMN_TITLE)));
        detail.setImage(cursor.getString(cursor.getColumnIndex(SingleStoryProvider.COLUMN_IMAGE)));
        detail.setId(Integer.toString(cursor.getInt(cursor.getColumnIndex(SingleStoryProvider.COLUMN_ID))));
        detail.setShare_url(cursor.getString(cursor.getColumnIndex(SingleStoryProvider.COLUMN_SHARE_URL)));
        detail.setCss(new String[]{cursor.getString(cursor.getColumnIndex(SingleStoryProvider.COLUMN_CSS))});
        cursor.close();
        return detail;
    }

    @Override
    public StoryDetail loadInBackground() {
        return loadData();
    }
}
