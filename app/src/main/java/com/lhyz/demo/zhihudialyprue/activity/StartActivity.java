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
import com.lhyz.demo.zhihudialyprue.network.BaseHttp;
import com.lhyz.demo.zhihudialyprue.util.DateUtil;
import com.lhyz.demo.zhihudialyprue.util.JSONUtil;
import com.lhyz.demo.zhihudialyprue.util.URLUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class StartActivity extends AppCompatActivity {

    private TextView mAuthorText;
    private ImageView mStartImage;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        mAuthorText = (TextView)findViewById(R.id.author);
        mStartImage = (ImageView)findViewById(R.id.image);

        animation = AnimationUtils.loadAnimation(this,R.anim.anim_start_page);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mStartImage.setVisibility(View.INVISIBLE);
                mAuthorText.setVisibility(View.INVISIBLE);
                finish();
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        new StartPageLoading().execute();
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

    /**
     * 单纯用来下载图片并缓存的Task
     */
    private class StartPageDownload extends AsyncTask<Void,Void,Void>{

        StartPagerCache cache = new StartPagerCache(getApplicationContext().getCacheDir());

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap bitmap;
            try{
                String data = BaseHttp.get(URLUtil.getInstance(getApplicationContext()).getStartImageURL());
                JSONUtil.getInstance().with(data);
                cache.put(DateUtil.getToady(), JSONUtil.getInstance().getAuthor());

                URL url = new URL(JSONUtil.getInstance().getBitmapURL());
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                    cache.put(DateUtil.getToady(), bitmap);
                }else{
                    throw new IOException("Network Error - response code: " + connection.getResponseCode());
                }
                connection.disconnect();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

    }

    /**
     * 寻找是否启动页图片可用的Task
     */
    private class StartPageLoading extends AsyncTask<Void,Void,StartPager>{

        StartPagerCache cache = new StartPagerCache(getApplicationContext().getCacheDir());

        protected StartPager doInBackground(Void... params){
            StartPager pager = new StartPager();

            String author;
            Bitmap bitmap;

            if((author = cache.getAuthor(DateUtil.getToady()))!=null && (bitmap = cache.getBitmap(DateUtil.getToady()))!= null ){
                pager.setAuthor(author);
                pager.setBitmap(bitmap);
            }else{
                pager.setAuthor(getResources().getString(R.string.init_author));
                BitmapFactory.Options options = new BitmapFactory.Options();
                //设置此参数可以使得图像不会被分配内存
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getResources(), R.drawable.init_image,options);
                int scale = (int)(options.outHeight / (float)getResources().getDisplayMetrics().heightPixels);
                if(scale<0){
                    scale = 1;
                }
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.init_image,options);
                pager.setBitmap(bitmap);
                new StartPageDownload().execute();
            }
            return  pager;
        }

        @Override
        protected void onPostExecute(StartPager startPager) {
            mAuthorText.setText(startPager.getAuthor());
            mStartImage.setImageBitmap(startPager.getBitmap());
            mStartImage.setAnimation(animation);
        }
    }
}
