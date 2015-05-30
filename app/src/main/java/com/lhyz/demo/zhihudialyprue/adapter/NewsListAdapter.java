package com.lhyz.demo.zhihudialyprue.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhyz.demo.zhihudialyprue.R;
import com.lhyz.demo.zhihudialyprue.bean.StorySimple;
import com.lhyz.demo.zhihudialyprue.datebase.DataSource;
import com.lhyz.demo.zhihudialyprue.loader.DataLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_VIEW_PAGER = 0;
    private static final int TYPE_VIEW_TITLE = 1;
    private static final int TYPE_VIEW_ITEM = 2;

    private static List<StorySimple> sTodays = new ArrayList<>();
    private static List<StorySimple> sHots = new ArrayList<>();

    private static Context sContext;
    private static FragmentManager sFragmentManager;
    private static LabsPagerAdapter sAdapter;
    private static SwipeRefreshLayout sSwipeRefreshLayout;
    private static NewsListAdapter instance;

    private static boolean mark1 = false;
    private static boolean mark2 = false;

    public NewsListAdapter(final Context context,FragmentManager fm,SwipeRefreshLayout swipeRefreshLayout) {
        sFragmentManager = fm;
        sContext = context;
        sSwipeRefreshLayout = swipeRefreshLayout;
        sSwipeRefreshLayout.setRefreshing(true);
        mark1 = false;
        mark2 = false;
        instance = this;
    }

    /**
     * 第一个是ViewPager
     */
    private static class ViewHolder1 extends RecyclerView.ViewHolder{
        private final ViewPager mViewPager;

        public ViewHolder1(View itemView) {
            super(itemView);
            mViewPager = (ViewPager)itemView.findViewById(R.id.labs);
        }

        public ViewPager getViewPager() {
            return mViewPager;
        }
    }

    /**
     * 第二个是正常的CardView
     */
    private static class ViewHolder2 extends RecyclerView.ViewHolder{
        private final TextView mTextView;
        private final ImageView mImageView;

        public ViewHolder2(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.title);
            mImageView = (ImageView)itemView.findViewById(R.id.images);
        }

        public TextView getTextView() {
            return mTextView;
        }

        public ImageView getImageView() {
            return mImageView;
        }
    }

    private static class ViewHolder3 extends RecyclerView.ViewHolder{

        public ViewHolder3(View itemView) {
            super(itemView);
        }
    }

    /**
     * 根据position返回Item的视图类型
     * @param position 需要实例化Item的位置
     * @return 类型标识符
     */
    @Override
    public int getItemViewType(int position) {
        int type;
        switch (position){
            case 0:
                type = TYPE_VIEW_PAGER;
                break;
            case 1:
                type = TYPE_VIEW_TITLE;
                break;
            default:
                type = TYPE_VIEW_ITEM;
        }
        return type;
    }

    /**
     * 这个方法只是实例化View
     * @param parent 父视图
     * @param viewType View类型
     * @return 返回ViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType){
            case TYPE_VIEW_PAGER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.labs_view_item,parent,false);
                return new ViewHolder1(v);
            case TYPE_VIEW_TITLE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title,parent,false);
                return new ViewHolder3(v);
            case TYPE_VIEW_ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item,parent,false);
                return new ViewHolder2(v);
            default:
                return null;
        }
    }

    /**
     * 将视图绑定数据
     * @param holder 实例化的ViewHolder
     * @param position 当前需要实例化的View Item
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0) {
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            sAdapter = new LabsPagerAdapter(sFragmentManager);
            viewHolder1.getViewPager().setAdapter(sAdapter);
        }else if(position != 1){
            ViewHolder2 viewHolder2 = (ViewHolder2)holder;
            viewHolder2.getTextView().setText(sTodays.get(position - 2).getTitle());
            Picasso.with(sContext)
                    .load(sTodays.get(position - 2).getImage())
                    .fit()
                    .centerCrop()
                    .into(viewHolder2.getImageView());
        }
    }

    /**
     * 获取所有数据的大小
     * @return 返回一个标识大小的数值
     */
    @Override
    public int getItemCount() {
        return sTodays.size()+2;
    }

    private class LabsPagerAdapter extends FragmentPagerAdapter{

        public LabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return LabsFragment.getInstance(sHots.get(position));
        }

        @Override
        public int getCount() {
            return sHots.size();
        }
    }

    

    public static class LabsFragment extends Fragment{
        private static final String EXTRA_DATA = "data";

        private StorySimple mStoryHot;

        public static LabsFragment getInstance(StorySimple storyHot){
            Bundle args = new Bundle();
            args.putSerializable(EXTRA_DATA, storyHot);
            LabsFragment fragment = new LabsFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mStoryHot = (StorySimple)getArguments().getSerializable(EXTRA_DATA);
            return inflater.inflate(R.layout.view_pager_item, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            ImageView imageView = (ImageView)view.findViewById(R.id.image);
            Picasso.with(sContext)
                    .load(mStoryHot.getImage())
                    .fit()
                    .centerCrop()
                    .into(imageView);
            TextView textView = (TextView)view.findViewById(R.id.title);
            textView.setText(mStoryHot.getTitle());
        }
    }

    private static class StoryTodayLoader extends DataLoader<List<StorySimple>> {
        public StoryTodayLoader(Context context) {
            super(context);
        }

        @Override
        protected List<StorySimple> loadData() {
            mData = DataSource.getInstance(sContext).queryTodays();
            return mData;
        }

        @Override
        public List<StorySimple> loadInBackground() {
            return loadData();
        }
    }

    private static class StoryHotLoader extends DataLoader<List<StorySimple>> {
        public StoryHotLoader(Context context) {
            super(context);
        }

        @Override
        protected List<StorySimple> loadData() {
            mData = DataSource.getInstance(sContext).queryHots();
            return mData;
        }

        @Override
        public List<StorySimple> loadInBackground() {
            return loadData();
        }
    }

    public static class StoryTodaysCallback implements LoaderManager.LoaderCallbacks<List<StorySimple>>{
        @Override
        public Loader<List<StorySimple>> onCreateLoader(int id, Bundle args) {
            return new StoryTodayLoader(sContext);
        }

        @Override
        public void onLoadFinished(Loader<List<StorySimple>> loader, List<StorySimple> data) {
            sTodays = data;
            mark1 = true;
            instance.notifyItemRangeChanged(2,sTodays.size());
            if(mark2){
                sSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<StorySimple>> loader) {
            //            sTodays = null;
        }
    }

    public static class StoryHotsCallback implements LoaderManager.LoaderCallbacks<List<StorySimple>>{
        @Override
        public Loader<List<StorySimple>> onCreateLoader(int id, Bundle args) {
            return new StoryHotLoader(sContext);
        }

        @Override
        public void onLoadFinished(Loader<List<StorySimple>> loader, List<StorySimple> data) {
            sHots = data;
            mark2 = true;
            if(mark1){
                sSwipeRefreshLayout.setRefreshing(false);
            }
            if(sAdapter != null){
                sAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<StorySimple>> loader) {
            sAdapter = null;
        }
    }
}
