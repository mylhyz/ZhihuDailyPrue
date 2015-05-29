package com.lhyz.demo.zhihudialyprue.log;

import android.util.Log;

public class Debug {
    private static final boolean ON = true;
    public static void i(String msg){
        if(ON){
            Log.i("Debug",msg);
        }
    }

    public static void e(String msg){
        if(ON){
            Log.e("Debug Error ",msg);
        }
    }
}
