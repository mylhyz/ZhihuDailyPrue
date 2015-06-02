package com.lhyz.demo.zhihudialyprue.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class DataLoader<D> extends AsyncTaskLoader<D> {

    protected D mData;
    protected Context mContext;

    public DataLoader(Context context) {
        super(context);
        mContext= context;
    }

    protected abstract D loadData();

    @Override
    protected void onStartLoading() {
        if(mData != null){
            deliverResult(mData);
        }else{
            forceLoad();
        }
    }

    @Override
    public void deliverResult(D data) {
        mData = data;
        if(isStarted()){
            super.deliverResult(data);
        }
    }
}
