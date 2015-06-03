package com.lhyz.demo.zhihudialyprue.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import com.lhyz.demo.zhihudialyprue.bean.StorySimple;
import com.lhyz.demo.zhihudialyprue.bean.StoryToday;
import com.lhyz.demo.zhihudialyprue.datebase.DataSource;
import com.lhyz.demo.zhihudialyprue.loader.StoryHotLoader;
import com.lhyz.demo.zhihudialyprue.loader.StoryTodayLoader;
import com.lhyz.demo.zhihudialyprue.network.BaseHttp;
import com.lhyz.demo.zhihudialyprue.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PRE_DATE = "date";

    private LoaderManager mLoaderManager;
    private FragmentManager mFragmentManager;
    private Context mContext = this;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NewsListAdapter mAdapter;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private List<StorySimple> mDataTodays;
    private List<StorySimple> mDataHots;

    private static boolean mark1 = false;
    private static boolean mark2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        String date = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(PRE_DATE, DateUtil.getToady());

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(PRE_DATE, DateUtil.getToady())
                .commit();
        if (!DateUtil.getToady().equals(date)) {
            DataSource.getInstance(this).moveData();
        }

        mLoaderManager = getSupportLoaderManager();
        mFragmentManager = getSupportFragmentManager();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.blue1,
                R.color.blue2,
                R.color.blue3,
                R.color.blue4
        );
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DownloadTask().execute();
            }
        });

        mark1 = false;
        mark2 = false;

        mRecyclerView = (RecyclerView) findViewById(R.id.listview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(null);

        new DownloadTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class DownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            mark1 = false;
            mark2 = false;
            Gson gson = new Gson();
            String jsonData = null;

            try {
                jsonData = BaseHttp.get(Constants.TODAY_STORIES_URL);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (jsonData == null) {
                return null;
            }

            List<StoryToday> list1 = null;
            try {
                JSONObject object = new JSONObject(jsonData);
                JSONArray array = object.getJSONArray("stories");
                list1 = gson.fromJson(array.toString(), new TypeToken<List<StoryToday>>() {
                }.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (list1 != null) {
                DataSource.getInstance(MainActivity.this).insertNewFromToday(list1);
            }

            List<StoryHot> list2 = null;
            try {
                JSONObject object = new JSONObject(jsonData);
                JSONArray array = object.getJSONArray("top_stories");
                list2 = gson.fromJson(array.toString(), new TypeToken<List<StoryHot>>() {
                }.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (list2 != null) {
                DataSource.getInstance(MainActivity.this).insertNewFromHot(list2);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mLoaderManager.initLoader(0, null, new UpdateTodays());
            mLoaderManager.initLoader(1, null, new UpdateHots());
        }
    }

    private class UpdateTodays implements LoaderManager.LoaderCallbacks<List<StorySimple>> {
        @Override
        public Loader<List<StorySimple>> onCreateLoader(int id, Bundle args) {
            return new StoryTodayLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<List<StorySimple>> loader, List<StorySimple> data) {
            mDataTodays = data;
            mark1 = true;
            if (mark2) {
                mAdapter = new NewsListAdapter(mContext, mFragmentManager, mDataTodays, mDataHots);
                mRecyclerView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<StorySimple>> loader) {

        }
    }

    private class UpdateHots implements LoaderManager.LoaderCallbacks<List<StorySimple>> {
        @Override
        public Loader<List<StorySimple>> onCreateLoader(int id, Bundle args) {
            return new StoryHotLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<List<StorySimple>> loader, List<StorySimple> data) {
            mDataHots = data;
            mark2 = true;
            if (mark1) {
                mAdapter = new NewsListAdapter(mContext, mFragmentManager, mDataTodays, mDataHots);
                mRecyclerView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<StorySimple>> loader) {

        }
    }
}
