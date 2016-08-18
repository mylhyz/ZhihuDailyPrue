package io.lhyz.android.zhihu.dialy.util;

import android.content.Context;

import io.lhyz.android.zhihu.dialy.Constants;


public class URLUtil {

    private static Context mContext;

    private URLUtil(Context context) {
        mContext = context;
    }

    public static URLUtil getInstance(Context context) {
        return new URLUtil(context);
    }

    /**
     * 根据设备尺寸构造启动页的图片URL请求
     *
     * @return 成功构造的URL
     */
    public String getStartImageURL() {
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        String size;

        if (width < 320) {
            size = Constants.START_IMG_SIZE_1;
        } else if (width < 480) {
            size = Constants.START_IMG_SIZE_2;
        } else if (width < 720) {
            size = Constants.START_IMG_SIZE_3;
        } else {
            size = Constants.START_IMG_SIZE_4;
        }

        return Constants.START_IMG_URL + size;
    }
}
