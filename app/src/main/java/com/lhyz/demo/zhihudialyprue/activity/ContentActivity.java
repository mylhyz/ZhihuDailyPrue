package com.lhyz.demo.zhihudialyprue.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhyz.demo.zhihudialyprue.Constants;
import com.lhyz.demo.zhihudialyprue.R;
import com.lhyz.demo.zhihudialyprue.bean.StoryDetail;
import com.lhyz.demo.zhihudialyprue.datebase.DataSource;
import com.lhyz.demo.zhihudialyprue.loader.StoryDownloadLoader;
import com.lhyz.demo.zhihudialyprue.loader.StoryLoader;
import com.lhyz.demo.zhihudialyprue.log.Debug;
import com.squareup.picasso.Picasso;

public class ContentActivity extends AppCompatActivity{

    public static final String EXTRA_ID = "id";

    private String mURL;
    private String mId;
    private Context mContext = this;
    private StoryDetail mDetail;

    private ImageView mImageView;
    private TextView mTitleTextView;
    private TextView mSourceTextView;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mImageView = (ImageView)findViewById(R.id.image);
        mTitleTextView = (TextView)findViewById(R.id.title);
        mSourceTextView = (TextView)findViewById(R.id.source);
        mWebView = (WebView)findViewById(R.id.content);
        WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mId = getIntent().getStringExtra(EXTRA_ID);
        mURL = Constants.STORY_DETAILS_URL + mId;
        if(DataSource.getInstance(this).hasStory(mId)){
            Debug.i("Has It");
            getSupportLoaderManager().initLoader(3,null,new DBCallbacks());
        }else{
            Debug.i("Has NOT Cache It");
            getSupportLoaderManager().initLoader(4,null,new DownloadCallbacks());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shared_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_share) {
            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            if (shareActionProvider != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mDetail.getShare_url());
                shareActionProvider.setShareIntent(shareIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadCallbacks implements LoaderManager.LoaderCallbacks<StoryDetail>{

        @Override
        public Loader<StoryDetail> onCreateLoader(int id, Bundle args) {
            return new StoryDownloadLoader(mContext,mURL);
        }

        @Override
        public void onLoadFinished(Loader<StoryDetail> loader, StoryDetail data) {
            mDetail = data;
            Picasso.with(mContext)
                    .load(data.getImage())
                    .fit()
                    .centerCrop()
                    .into(mImageView);
            mTitleTextView.setText(data.getTitle());
            mSourceTextView.setText(data.getImage_source());
            mWebView.loadData(data.getBody(), "text/html; charset=UTF-8", null);
        }

        @Override
        public void onLoaderReset(Loader<StoryDetail> loader) {

        }
    }

    private class DBCallbacks implements LoaderManager.LoaderCallbacks<StoryDetail>{
        @Override
        public Loader<StoryDetail> onCreateLoader(int id, Bundle args) {
            return new StoryLoader(mContext,mId);
        }

        @Override
        public void onLoadFinished(Loader<StoryDetail> loader, StoryDetail data) {
            mDetail = data;
            Picasso.with(mContext)
                    .load(data.getImage())
                    .fit()
                    .centerCrop()
                    .into(mImageView);
            mTitleTextView.setText(data.getTitle());
            mSourceTextView.setText(data.getImage_source());
            mWebView.loadData(data.getBody(), "text/html; charset=UTF-8", null);
        }

        @Override
        public void onLoaderReset(Loader<StoryDetail> loader) {

        }
    }
}