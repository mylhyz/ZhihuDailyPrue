package com.lhyz.demo.zhihudialyprue.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhyz.demo.zhihudialyprue.R;
import com.lhyz.demo.zhihudialyprue.cache.StartPagerCache;
import com.lhyz.demo.zhihudialyprue.util.DateUtil;
import com.lhyz.demo.zhihudialyprue.util.JSONUtil;
import com.lhyz.demo.zhihudialyprue.util.URLUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StartActivity extends AppCompatActivity {

    private TextView mAuthorText;
    private ImageView mStartImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthorText = (TextView)findViewById(R.id.author);
        mStartImage = (ImageView)findViewById(R.id.image);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_start_page);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAuthorText.setVisibility(View.INVISIBLE);
                finish();
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mStartImage.setAnimation(animation);

        new StartPageDownload().execute();
    }

    public static class StartPager{
        private String author;
        private Bitmap bitmap;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }

    private class StartPageDownload extends AsyncTask<Void,Void,StartPager>{

        StartPagerCache cache = new StartPagerCache(getApplicationContext().getCacheDir());

        protected StartPager doInBackground(Void... params){
            StartPager pager = new StartPager();

            String author;
            Bitmap bitmap;

            if((author = cache.getAuthor(DateUtil.getToady()))!=null && (bitmap = cache.getBitmap(DateUtil.getToady()))!= null ){
                pager.setAuthor(author);
                pager.setBitmap(bitmap);
                return pager;
            }

            try{
                URL url = new URL(URLUtil.getInstance(getApplicationContext()).getStartImageURL());
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while((line = reader.readLine())!=null){
                        builder.append(line);
                    }
                    reader.close();
                    JSONUtil.getInstance().with(builder.toString());
                    cache.put(DateUtil.getToady(),JSONUtil.getInstance().getAuthor());
                    pager.setAuthor(JSONUtil.getInstance().getAuthor());
                }else{
                    throw new IOException("Network Error - response code: " + connection.getResponseCode());
                }
                connection.disconnect();

                url = new URL(JSONUtil.getInstance().getBitmapURL());
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                    cache.put(DateUtil.getToady(),bitmap);
                    pager.setBitmap(bitmap);
                }else{
                    throw new IOException("Network Error - response code: " + connection.getResponseCode());
                }
                connection.disconnect();
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }

            return pager;
        }

        @Override
        protected void onPostExecute(StartPager startPager) {
            mAuthorText.setText(startPager.getAuthor());
            mStartImage.setImageBitmap(startPager.getBitmap());
        }
    }
}
