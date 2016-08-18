package io.lhyz.android.zhihu.dialy.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.lhyz.android.zhihu.dialy.bean.StorySimple;
import io.lhyz.android.zhihu.dialy.fragment.LabsFragment;
import io.lhyz.android.zhihu.dialy.provider.TopStoryProvider;

public class LabsPageStateAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private List<StorySimple> mSimpleList;

    public static final String[] projection = new String[]{
            TopStoryProvider.COLUMN_ID,
            TopStoryProvider.COLUMN_IMAGE,
            TopStoryProvider.COLUMN_TITLE
    };

    public LabsPageStateAdapter(Context context, FragmentManager fm, Cursor cursor) {
        super(fm);
        mContext = context;
        mSimpleList = new ArrayList<>();
        int idIdx = cursor.getColumnIndex(TopStoryProvider.COLUMN_ID);
        int imgIdx = cursor.getColumnIndex(TopStoryProvider.COLUMN_IMAGE);
        int titleIdx = cursor.getColumnIndex(TopStoryProvider.COLUMN_TITLE);
        StorySimple simple;
        while (cursor.moveToNext()) {
            simple = new StorySimple();
            simple.setId(Integer.toString(cursor.getInt(idIdx)));
            simple.setImage(cursor.getString(imgIdx));
            simple.setTitle(cursor.getString(titleIdx));
            mSimpleList.add(simple);
        }
        cursor.close();
    }

    @Override
    public Fragment getItem(int position) {
        return LabsFragment.getInstance(mContext, mSimpleList.get(position));
    }

    @Override
    public int getCount() {
        return mSimpleList.size();
    }
}
