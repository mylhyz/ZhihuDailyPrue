package com.lhyz.demo.zhihudialyprue.log;

import android.util.Log;

import com.lhyz.demo.zhihudialyprue.bean.StorySimple;

import java.util.List;

public class Debug {
    private static final boolean ON = true;
    public static void i(String TAG,String msg){
        if(ON){
            Log.i(TAG,msg);
        }
    }

    public static void e(String TAG,String msg,Exception e){
        if(ON){
            Log.e(TAG, msg,e);
        }
    }

    public static void print(List<StorySimple> simples){
        if(ON){
            for(StorySimple simple : simples){
                i("ALL LIST ",simple.getTitle());
            }
        }
    }
}
