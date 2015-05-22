package com.lhyz.demo.zhihudialyprue.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import com.lhyz.demo.zhihudialyprue.util.DateUtil;
import com.lhyz.demo.zhihudialyprue.util.JSONUtil;
import com.lhyz.demo.zhihudialyprue.util.URLUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GetStartPager {

    private Context mContext;

    public static Bitmap mBitmap;
    public static String mAuthor;

    static LruCache<String,StartPager> mStartImageCache = new LruCache<>(1);

    ExecutorService executor = Executors.newSingleThreadExecutor();

    static String mPagerURL;
    static String mBitmapURL;

    public StartPager pager;

    private static GetStartPager instance;

    private GetStartPager(Context context){
        mContext = context;
    }

    public static GetStartPager getInstance(Context context){
        if(instance != null){
            return instance;
        }else{
            return new GetStartPager(context);
        }
    }

    public void getPager() throws InterruptedException,ExecutionException{
        //如果缓存中有当天数据，那么直接返回
        if((pager = mStartImageCache.get(DateUtil.getToady()))!= null){
            mAuthor = pager.author;
            mBitmap = pager.bitmap;
            return;
        }

        mPagerURL = URLUtil.getInstance(mContext).getStartImageURL();

        Future<String> startPagerFuture =  executor.submit(new JsonDataDownload());
        mBitmapURL = startPagerFuture.get();

        executor.submit(new BitmapDownload());
    }

    private static class StartPager{
        public String author;
        public Bitmap bitmap;

        public StartPager(String author, Bitmap bitmap) {
            this.author = author;
            this.bitmap = bitmap;
        }
    }

    /**
     * 从构造的URL获取JSON数据
     */
    private static class JsonDataDownload implements Callable<String>{
        @Override
        public String call() throws Exception {
            URL url = new URL(mPagerURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            try{
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                    reader.close();
                    JSONUtil.getInstance().with(builder.toString());
                    mAuthor = JSONUtil.getInstance().getAuthor();
                    Log.i("TAG",mAuthor);
                    return JSONUtil.getInstance().getBitmapURL();
                }else{
                    throw new IOException("Network Error - response code: " + connection.getResponseCode());
                }
            }finally {
                connection.disconnect();
            }
        }
    }

    /**
     * 从获取的JSON数据得到图像数据之后去下载图像，并加入缓存中
     */
    private static class BitmapDownload implements Callable<Void>{
        @Override
        public Void call() throws Exception {
            URL url = new URL(mBitmapURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            try{
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    mBitmap = BitmapFactory.decodeStream(connection.getInputStream());
                    //下载完图像之后更新缓存
                    mStartImageCache.put(DateUtil.getToady(),new StartPager(mAuthor,mBitmap));
                    Log.i("TAG","Done");
                }else{
                    throw new IOException("Network Error - response code: " + connection.getResponseCode());
                }
            }finally {
                connection.disconnect();
            }
            return null;
        }
    }
}
