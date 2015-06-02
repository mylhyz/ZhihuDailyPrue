package com.lhyz.demo.zhihudialyprue.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lhyz.demo.zhihudialyprue.bean.StorySimple;
import com.lhyz.demo.zhihudialyprue.fragment.LabsFragment;

import java.util.List;

public class LabsPageStateAdapter extends FragmentStatePagerAdapter {
    private List<StorySimple> mHots;
    private Context mContext;

    public LabsPageStateAdapter(Context context,FragmentManager fm,List<StorySimple> hots) {
        super(fm);
        mHots = hots;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return LabsFragment.getInstance(mContext, mHots.get(position));
    }

    @Override
    public int getCount() {
        return mHots.size();
    }
}
