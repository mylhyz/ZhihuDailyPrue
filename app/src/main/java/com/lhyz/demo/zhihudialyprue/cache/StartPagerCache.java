package com.lhyz.demo.zhihudialyprue.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lhyz.demo.zhihudialyprue.log.Debug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StartPagerCache {

    private File mRootCacheDir;

    public StartPagerCache(File mRootCacheDir) {
        this.mRootCacheDir = mRootCacheDir;
        Debug.i(mRootCacheDir.getAbsolutePath());
    }

    public synchronized void clear(){
        int i=0;
        for(File file : mRootCacheDir.listFiles()){
            boolean delete = file.delete();
            if(delete){
                Debug.i("Clear "+i++);
            }
        }
    }

    public synchronized void put(String key,Bitmap bitmap){
        File cache = new File(mRootCacheDir.getAbsolutePath()+"/"+key+".jpg");
        try {
            OutputStream out = new FileOutputStream(cache);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG,80,out)){
                out.flush();
                out.close();
            }
            Debug.i("Bitmap Cache");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public synchronized void put(String key,String author){
        clear();
        File cache = new File(mRootCacheDir.getAbsolutePath()+"/"+key+".txt");
        try {
            OutputStream out = new FileOutputStream(cache);
            out.write(author.getBytes());
            out.close();
            Debug.i("Author Cache");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public synchronized Bitmap getBitmap(String key){
        File cache = new File(mRootCacheDir.getAbsolutePath()+"/"+key+".jpg");
        if(cache.exists()){
            Debug.i("Has Bitmap Cache");
            try {
                InputStream in = new FileInputStream(cache);
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                in.close();
                return bitmap;
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }else{
            Debug.i("Has no Bitmap Cache");
            return null;
        }
    }

    public synchronized String getAuthor(String key){
        File cache = new File(mRootCacheDir.getAbsolutePath()+"/"+key+".txt");
        if(!cache.exists()){
            Debug.i("Has no Author Cache");
            return null;
        }
        Debug.i("Has Author Cache");
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cache)));
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                builder.append(line);
            }
            reader.close();
            return builder.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
