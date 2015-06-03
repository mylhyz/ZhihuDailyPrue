package com.lhyz.demo.zhihudialyprue.util;


import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String getToady(){
        return DateFormat.format("yyyyMMdd",new Date()).toString();
    }

    public static String getYesToday(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,-1);
        return DateFormat.format("yyyyMMdd",calendar.getTime()).toString();
    }
}
