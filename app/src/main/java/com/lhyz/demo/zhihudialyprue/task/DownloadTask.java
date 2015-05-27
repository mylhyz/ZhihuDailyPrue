package com.lhyz.demo.zhihudialyprue.task;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lhyz.demo.zhihudialyprue.bean.StoryHot;
import com.lhyz.demo.zhihudialyprue.bean.StoryToday;
import com.lhyz.demo.zhihudialyprue.datebase.DataSource;
import com.lhyz.demo.zhihudialyprue.network.BaseHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DownloadTask {

    private String jsonData;
    private String urlAddr;
    private static Context sContext;

    Future<List<StoryToday>> stories;
    Future<List<StoryHot>> top_stories;

    private Gson gson = new Gson();

    private static DownloadTask instance;

    ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    public static DownloadTask getInstance(Context context,String urlAddr) {
        if(instance==null){
            instance = new DownloadTask(urlAddr);
        }
        sContext = context;
        return instance;
    }

    private DownloadTask(String urlAddr) {
        this.urlAddr = urlAddr;
    }

    public void loadingBackground() throws InterruptedException,ExecutionException{
        mExecutorService.submit(new DataDownloadCall());
        stories = mExecutorService.submit(new TodayStoryCall());
        top_stories = mExecutorService.submit(new HotStoryCall());
        mExecutorService.submit(new SaveDataCall());
    }

    private class SaveDataCall implements Callable<Void>{
        @Override
        public Void call() throws Exception {
            DataSource.getInstance(sContext).insertNewFromToday(stories.get());
            DataSource.getInstance(sContext).insertNewFromHot(top_stories.get());
            return null;
        }
    }

    private class DataDownloadCall implements Callable<Void>{
        @Override
        public Void call() throws Exception {
            jsonData = BaseHttp.get(urlAddr);
            return null;
        }
    }

    private class TodayStoryCall implements Callable<List<StoryToday>>{
        @Override
        public List<StoryToday> call() throws Exception {
            List<StoryToday> list = null;
            try{
                JSONObject object =new JSONObject(jsonData);
                JSONArray array = object.getJSONArray("stories");
                list = gson.fromJson(array.toString(),new TypeToken<List<StoryToday>>(){}.getType());
            }catch (JSONException e){
                e.printStackTrace();
            }
            return list;
        }
    }

    private class HotStoryCall implements Callable<List<StoryHot>>{
        @Override
        public List<StoryHot> call() throws Exception {
            List<StoryHot> list;
            try {
                JSONObject object = new JSONObject(jsonData);
                JSONArray array = object.getJSONArray("top_stories");
                list = gson.fromJson(array.toString(),new TypeToken<List<StoryHot>>(){}.getType());
            }catch (JSONException e){
                return null;
            }
            return list;
        }
    }
}
