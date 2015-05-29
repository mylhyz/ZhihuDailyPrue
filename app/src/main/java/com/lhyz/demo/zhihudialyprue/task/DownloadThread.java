package com.lhyz.demo.zhihudialyprue.task;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lhyz.demo.zhihudialyprue.Constants;
import com.lhyz.demo.zhihudialyprue.bean.StoryHot;
import com.lhyz.demo.zhihudialyprue.bean.StoryToday;
import com.lhyz.demo.zhihudialyprue.datebase.DataSource;
import com.lhyz.demo.zhihudialyprue.network.BaseHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * 创建单个线程在后台按照顺序执行任务，根据我的思考，
 * 在下载和插入数据库的操作上，因为有严格的时间顺序限制，因此想办法顺序执行是很好的
 */
public class DownloadThread extends Thread {

    private Context mContext;

    public DownloadThread(Context context) {
        mContext = context;
    }

    @Override
    public void run(){
        Gson gson = new Gson();
        String jsonData = null;

        try{
            jsonData = BaseHttp.get(Constants.TODAY_STORIES_URL);
        }catch (IOException e){
            e.printStackTrace();
        }

        if(jsonData == null){
            return;
        }

        List<StoryToday> list1 = null;
        try{
            JSONObject object =new JSONObject(jsonData);
            JSONArray array = object.getJSONArray("stories");
            list1 = gson.fromJson(array.toString(), new TypeToken<List<StoryToday>>() {
            }.getType());
        }catch (JSONException e){
            e.printStackTrace();
        }

        if(list1 != null){
            DataSource.getInstance(mContext).insertNewFromToday(list1);
        }

        List<StoryHot> list2 = null;
        try {
            JSONObject object = new JSONObject(jsonData);
            JSONArray array = object.getJSONArray("top_stories");
            list2 = gson.fromJson(array.toString(),new TypeToken<List<StoryHot>>(){}.getType());
        }catch (JSONException e){
            e.printStackTrace();
        }

        if(list2 != null){
            DataSource.getInstance(mContext).insertNewFromHot(list2);
        }
    }
}
