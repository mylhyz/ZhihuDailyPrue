package com.lhyz.demo.zhihudialyprue.log;

import android.util.Log;

import com.lhyz.demo.zhihudialyprue.bean.StorySimple;

import java.util.List;

public class Debug {
    private static final boolean ON = true;
    public static void i(String msg){
        if(ON){
            Log.i("lhyz Debug",msg);
        }
    }

    public static void e(String msg){
        if(ON){
            Log.e("lhyz Debug Error ", msg);
        }
    }

    public static void print(List<StorySimple> simples){
        if(ON){
            for(StorySimple simple : simples){
                i(""+simple.getTitle());
            }
        }
    }
}
