package com.lhyz.demo.zhihudialyprue.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lhyz.demo.zhihudialyprue.bean.StorySimple;
import com.lhyz.demo.zhihudialyprue.fragment.LabsFragment;
import com.lhyz.demo.zhihudialyprue.log.Debug;

import java.util.List;

/**
 * FragmentPagerAdapter使用的话可能出现问题，结果就是重新实例化此Adapter之后
 * 无法bind各个Item，就是getItem会不被调用（此时重新实例化提供的数据集合是没变的）
 * 主要问题稍后研究，只能暂时将Adapter基类换成{@link android.support.v4.app.FragmentStatePagerAdapter}
 */
@Deprecated
public class LabsPagerAdapter extends FragmentPagerAdapter{

    private List<StorySimple> mHots;
    private Context mContext;

    public LabsPagerAdapter(Context context,FragmentManager fm,List<StorySimple> hots) {
        super(fm);
        mHots = hots;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Debug.i(mHots.get(position).getTitle());
        return LabsFragment.getInstance(mContext, mHots.get(position));
    }

    @Override
    public int getCount() {
        Debug.i("DataSet : "+mHots.size());
        return mHots.size();
    }
}
