package com.lhyz.demo.zhihudialyprue.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhyz.demo.zhihudialyprue.Constants;
import com.lhyz.demo.zhihudialyprue.R;
import com.lhyz.demo.zhihudialyprue.bean.StoryDetail;
import com.lhyz.demo.zhihudialyprue.loader.StoryLoader;
import com.squareup.picasso.Picasso;

public class ContentActivity extends AppCompatActivity{

    public static final String EXTRA_ID = "id";

    private String mURL;
    private Context mContext = this;

    private ImageView mImageView;
    private TextView mTitleTextView;
    private TextView mSourceTextView;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mImageView = (ImageView)findViewById(R.id.image);
        mTitleTextView = (TextView)findViewById(R.id.title);
        mSourceTextView = (TextView)findViewById(R.id.source);
        mWebView = (WebView)findViewById(R.id.content);
        WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mURL = Constants.STORY_DETAILS_URL + getIntent().getStringExtra(EXTRA_ID);

        getSupportLoaderManager().initLoader(3,null,new Callbacks());
    }

    private class Callbacks implements LoaderManager.LoaderCallbacks<StoryDetail>{

        @Override
        public Loader<StoryDetail> onCreateLoader(int id, Bundle args) {
            return new StoryLoader(mContext,mURL);
        }

        @Override
        public void onLoadFinished(Loader<StoryDetail> loader, StoryDetail data) {
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
