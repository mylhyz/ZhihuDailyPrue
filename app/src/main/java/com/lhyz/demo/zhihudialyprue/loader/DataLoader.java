package com.lhyz.demo.zhihudialyprue.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.lhyz.demo.zhihudialyprue.bean.StorySimple;

import java.util.List;

public abstract class DataLoader extends AsyncTaskLoader<List<StorySimple>> {

    protected List<StorySimple> mData;
    protected Context mContext;

    public DataLoader(Context context) {
        super(context);
        mContext= context;
    }

    protected abstract List<StorySimple> loadData();

    @Override
    protected void onStartLoading() {
        if(mData != null){
            deliverResult(mData);
        }else{
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<StorySimple> data) {
        mData = data;
        if(isStarted()){
            super.deliverResult(data);
        }
    }
}
