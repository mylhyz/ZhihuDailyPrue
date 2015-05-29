package com.lhyz.demo.zhihudialyprue.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lhyz.demo.zhihudialyprue.R;
import com.lhyz.demo.zhihudialyprue.adapter.NewsListAdapter;
import com.lhyz.demo.zhihudialyprue.task.DownloadThread;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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
                new DownloadThread(MainActivity.this).start();
                MainActivity.this.getLoaderManager().initLoader(0, null, new NewsListAdapter.StoryTodaysCallback());
                MainActivity.this.getLoaderManager().initLoader(1, null, new NewsListAdapter.StoryHotsCallback());
            }
        });

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NewsListAdapter(this, getSupportFragmentManager(),swipeRefreshLayout));

        getLoaderManager().initLoader(0, null, new NewsListAdapter.StoryTodaysCallback());
        getLoaderManager().initLoader(1,null,new NewsListAdapter.StoryHotsCallback());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
