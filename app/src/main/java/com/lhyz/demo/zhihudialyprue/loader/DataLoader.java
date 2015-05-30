package com.lhyz.demo.zhihudialyprue.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class DataLoader<T> extends AsyncTaskLoader<T> {
    protected T mData;

    public DataLoader(Context context) {
        super(context);
    }

    protected abstract T loadData();

    @Override
    protected void onStartLoading() {
        if(mData != null){
            deliverResult(mData);
        }else{
            forceLoad();
        }
    }

    @Override
    public void deliverResult(T data) {
        mData = data;
        if(isStarted()){
            super.deliverResult(data);
        }
    }
}
