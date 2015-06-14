package com.lhyz.demo.zhihudialyprue.network;

import com.lhyz.demo.zhihudialyprue.log.Debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaseHttp {
    /**
     * 从一个URL获取JSON数据
     * @param urlString URL地址字符串
     * @return 获取到的字符串数据
     */
    public static String get(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        StringBuilder builder = new StringBuilder();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        } else {
            throw new IOException("Network Error - response code: " + connection.getResponseCode());
        }
        connection.disconnect();

        return builder.toString();
    }
}
