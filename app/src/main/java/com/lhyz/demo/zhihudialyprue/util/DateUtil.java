package com.lhyz.demo.zhihudialyprue.util;


import android.text.format.DateFormat;

import java.util.Date;

public class DateUtil {

    public static String getToady(){
        return DateFormat.format("yyyyMMdd",new Date()).toString();
    }
}
