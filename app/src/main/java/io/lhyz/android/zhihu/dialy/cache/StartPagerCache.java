package io.lhyz.android.zhihu.dialy.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    }

    public synchronized void clear(){
        for(File file : mRootCacheDir.listFiles()){
            boolean delete = file.delete();
            if(delete){
                //未测试结果的语句，可能有错误
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
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public synchronized Bitmap getBitmap(String key){
        File cache = new File(mRootCacheDir.getAbsolutePath()+"/"+key+".jpg");
        if(cache.exists()){
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
            return null;
        }
    }

    public synchronized String getAuthor(String key){
        File cache = new File(mRootCacheDir.getAbsolutePath()+"/"+key+".txt");
        if(!cache.exists()){
            return null;
        }
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
