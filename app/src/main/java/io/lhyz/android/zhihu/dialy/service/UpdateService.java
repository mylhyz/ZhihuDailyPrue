package io.lhyz.android.zhihu.dialy.service;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.lhyz.android.zhihu.dialy.Constants;
import io.lhyz.android.zhihu.dialy.bean.StoryHot;
import io.lhyz.android.zhihu.dialy.bean.StoryToday;
import io.lhyz.android.zhihu.dialy.log.Debug;
import io.lhyz.android.zhihu.dialy.network.BaseHttp;
import io.lhyz.android.zhihu.dialy.provider.StoryProvider;
import io.lhyz.android.zhihu.dialy.provider.TopStoryProvider;

/**
 * Created by lhyz on 6/14/2015.
 * Copyright (c)
 * <p/>
 * 此Service可以从网上下载并保存数据到数据库中
 */
public class UpdateService extends IntentService {
    private static final String TAG = UpdateService.class.getCanonicalName();

    @SuppressWarnings("unused")
    public UpdateService() {
        super(TAG);
    }

    @SuppressWarnings("unused")
    public UpdateService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Gson gson = new Gson();
        String jsonData = null;
        Builder builder;
        ContentResolver contentResolver = getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        builder = ContentProviderOperation.newDelete(StoryProvider.CONTENT_URI);
        operations.add(builder.build());

        try {
            jsonData = BaseHttp.get(Constants.TODAY_STORIES_URL);
        } catch (IOException e) {
            Debug.e(TAG, "Unable to Get Data From remote network. ", e);
        }

        if (jsonData == null) {
            return;
        }

        List<StoryToday> list1 = null;
        try {
            JSONObject object = new JSONObject(jsonData);
            JSONArray array = object.getJSONArray("stories");
            list1 = gson.fromJson(array.toString(), new TypeToken<List<StoryToday>>() {
            }.getType());
        } catch (JSONException e) {
            Debug.e(TAG, "Unable to Parse item From json data. ", e);
        }

        if (list1 != null) {
            for (StoryToday today : list1) {
                ContentValues cv = new ContentValues();
                cv.put(StoryProvider.COLUMN_IMAGE, today.getImages()[0]);
                cv.put(StoryProvider.COLUMN_ID, today.getId());
                cv.put(StoryProvider.COLUMN_GA_PREFIX, today.getGa_prefix());
                cv.put(StoryProvider.COLUMN_MULTIPIC, today.getMultipic());
                cv.put(StoryProvider.COLUMN_TITLE, today.getTitle());
                builder = ContentProviderOperation.newInsert(StoryProvider.CONTENT_URI);
                builder.withValues(cv);
                operations.add(builder.build());
            }
        }

        try {
            contentResolver.applyBatch(StoryProvider.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            Debug.e(TAG, "Error to apply batch in StoryProvider. ", e);
        }

        operations.clear();
        builder = ContentProviderOperation.newDelete(TopStoryProvider.CONTENT_URI);
        operations.add(builder.build());

        List<StoryHot> list2 = null;
        try {
            JSONObject object = new JSONObject(jsonData);
            JSONArray array = object.getJSONArray("top_stories");
            list2 = gson.fromJson(array.toString(), new TypeToken<List<StoryHot>>() {
            }.getType());
        } catch (JSONException e) {
            Debug.e(TAG, "Unable to Parse item From json data. ", e);
        }

        if (list2 != null) {
            for (StoryHot hot : list2) {
                ContentValues cv = new ContentValues();
                cv.put(TopStoryProvider.COLUMN_IMAGE, hot.getImage());
                cv.put(TopStoryProvider.COLUMN_ID, hot.getId());
                cv.put(TopStoryProvider.COLUMN_GA_PREFIX, hot.getGa_prefix());
                cv.put(TopStoryProvider.COLUMN_TITLE, hot.getTitle());
                builder = ContentProviderOperation.newInsert(TopStoryProvider.CONTENT_URI);
                builder.withValues(cv);
                operations.add(builder.build());
            }
        }

        try {
            contentResolver.applyBatch(TopStoryProvider.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            Debug.e(TAG, "Error to apply batch in TopStoryProvider. ", e);
        }

        Debug.i(TAG, "Done");
    }
}
