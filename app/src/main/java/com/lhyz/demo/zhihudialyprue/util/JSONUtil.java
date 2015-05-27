package com.lhyz.demo.zhihudialyprue.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {

    private String jsonInput;
    private static JSONUtil instance;

    public void with(String jsonInput){
        instance.jsonInput = jsonInput;
    }

    public static JSONUtil getInstance(){
        if(instance == null){
            instance = new JSONUtil();
        }
        return instance;
    }

    public String getAuthor(){
        try {
            JSONObject object = new JSONObject(jsonInput);
            return object.getString("text");
        }catch (JSONException e){
            return null;
        }
    }

    public String getBitmapURL(){
        try{
            JSONObject object = new JSONObject(jsonInput);
            return object.getString("img");
        }catch (JSONException e){
            return null;
        }
    }
}
