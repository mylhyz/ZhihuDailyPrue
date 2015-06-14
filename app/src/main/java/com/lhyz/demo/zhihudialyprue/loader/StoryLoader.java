package com.lhyz.demo.zhihudialyprue.loader;

import android.content.Context;

import com.lhyz.demo.zhihudialyprue.bean.StoryDetail;

public class StoryLoader extends DataLoader<StoryDetail> {

    private String id;

    public StoryLoader(Context context,String id) {
        super(context);
        this.id = id;
    }

    @Override
    protected StoryDetail loadData() {
        return null;
    }

    @Override
    public StoryDetail loadInBackground() {
        return loadData();
    }
}
