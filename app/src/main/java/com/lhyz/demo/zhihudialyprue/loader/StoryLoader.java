package com.lhyz.demo.zhihudialyprue.loader;

import android.content.Context;

import com.lhyz.demo.zhihudialyprue.bean.StoryDetail;
import com.lhyz.demo.zhihudialyprue.datebase.DataSource;

public class StoryLoader extends DataLoader<StoryDetail> {

    private String id;

    public StoryLoader(Context context,String id) {
        super(context);
        this.id = id;
    }

    @Override
    protected StoryDetail loadData() {
        return DataSource.getInstance(mContext).getStory(id);
    }

    @Override
    public StoryDetail loadInBackground() {
        return loadData();
    }
}
