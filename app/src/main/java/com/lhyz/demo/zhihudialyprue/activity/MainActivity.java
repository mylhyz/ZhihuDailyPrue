package com.lhyz.demo.zhihudialyprue.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lhyz.demo.zhihudialyprue.Constants;
import com.lhyz.demo.zhihudialyprue.R;
import com.lhyz.demo.zhihudialyprue.adapter.NewsListAdapter;
import com.lhyz.demo.zhihudialyprue.bean.StoryHot;
import com.lhyz.demo.zhihudialyprue.bean.StoryToday;
import com.lhyz.demo.zhihudialyprue.datebase.DataSource;
import com.lhyz.demo.zhihudialyprue.network.BaseHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private LoaderManager mLoaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mLoaderManager = getSupportLoaderManager();


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.blue1,
                R.color.blue2,
                R.color.blue3,
                R.color.blue4
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DownloadTask().execute();
            }
        });
        NewsListAdapter adapter = new NewsListAdapter(this, getSupportFragmentManager(), swipeRefreshLayout);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

        mLoaderManager.initLoader(0, null, new NewsListAdapter.StoryTodaysCallback());
        mLoaderManager.initLoader(1, null, new NewsListAdapter.StoryHotsCallback());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class DownloadTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            Gson gson = new Gson();
            String jsonData = null;

            try{
                jsonData = BaseHttp.get(Constants.TODAY_STORIES_URL);
            }catch (IOException e){
                e.printStackTrace();
            }

            if(jsonData == null){
                return null;
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
                DataSource.getInstance(MainActivity.this).insertNewFromToday(list1);
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
                DataSource.getInstance(MainActivity.this).insertNewFromHot(list2);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mLoaderManager.initLoader(0, null, new NewsListAdapter.StoryTodaysCallback());
            mLoaderManager.initLoader(1, null, new NewsListAdapter.StoryHotsCallback());
        }
    }
}
