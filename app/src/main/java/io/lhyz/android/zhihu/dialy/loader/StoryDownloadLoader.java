package io.lhyz.android.zhihu.dialy.loader;

import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import io.lhyz.android.zhihu.dialy.bean.StoryDetail;
import io.lhyz.android.zhihu.dialy.network.BaseHttp;
import io.lhyz.android.zhihu.dialy.provider.SingleStoryProvider;

public class StoryDownloadLoader extends DataLoader<StoryDetail> {

    private String url;
    private Gson mGson = new Gson();

    public StoryDownloadLoader(Context context, String url) {
        super(context);
        mContext = context;
        this.url = url;
    }

    @Override
    protected StoryDetail loadData() {
        String raw = null;
        StoryDetail detail;
        try {
            raw = BaseHttp.get(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (raw == null) {
            return null;
        }

        detail = mGson.fromJson(raw, new TypeToken<StoryDetail>() {
        }.getType());

        ContentValues cv = new ContentValues();
        cv.put(SingleStoryProvider.COLUMN_IMAGE_SOURCE, detail.getImage_source());
        cv.put(SingleStoryProvider.COLUMN_TITLE, detail.getTitle());
        cv.put(SingleStoryProvider.COLUMN_IMAGE, detail.getImage());
        cv.put(SingleStoryProvider.COLUMN_ID, detail.getId());
        cv.put(SingleStoryProvider.COLUMN_CSS, detail.getCss()[0]);
        cv.put(SingleStoryProvider.COLUMN_BODY, detail.getBody());
        cv.put(SingleStoryProvider.COLUMN_SHARE_URL, detail.getShare_url());
        getContext().getContentResolver().insert(SingleStoryProvider.CONTENT_URI, cv);
        return detail;
    }

    @Override
    public StoryDetail loadInBackground() {
        return loadData();
    }
}
