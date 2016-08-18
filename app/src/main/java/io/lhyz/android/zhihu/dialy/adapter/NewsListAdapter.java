package io.lhyz.android.zhihu.dialy.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.lhyz.android.zhihu.dialy.R;
import io.lhyz.android.zhihu.dialy.activity.ContentActivity;
import io.lhyz.android.zhihu.dialy.bean.StorySimple;
import io.lhyz.android.zhihu.dialy.provider.StoryProvider;

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_VIEW_PAGER = 0;
    private static final int TYPE_VIEW_TITLE = 1;
    private static final int TYPE_VIEW_ITEM = 2;

    public static final String[] projection = new String[]{
            StoryProvider.COLUMN_ID,
            StoryProvider.COLUMN_IMAGE,
            StoryProvider.COLUMN_TITLE
    };

    private static List<StorySimple> mDataTodays;
    private static Context mContext;
    private LabsPageStateAdapter mAdapter;

    public NewsListAdapter(Context context,FragmentManager fm,Cursor c1,Cursor c2) {
        mContext = context;
        mDataTodays = new ArrayList<>();
        int idIdx = c1.getColumnIndex(StoryProvider.COLUMN_ID);
        int imgIdx = c1.getColumnIndex(StoryProvider.COLUMN_IMAGE);
        int titleIdx = c1.getColumnIndex(StoryProvider.COLUMN_TITLE);
        StorySimple simple;
        while(c1.moveToNext()){
            simple = new StorySimple();
            simple.setId(Integer.toString(c1.getInt(idIdx)));
            simple.setImage(c1.getString(imgIdx));
            simple.setTitle(c1.getString(titleIdx));
            mDataTodays.add(simple);
        }
        c1.close();

        mAdapter = new LabsPageStateAdapter(context,fm,c2);
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
        private final CardView mCardView;
        private final TextView mTextView;
        private final ImageView mImageView;

        public ViewHolder2(View itemView) {
            super(itemView);
            mCardView = (CardView)itemView.findViewById(R.id.card_item);
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StorySimple item = mDataTodays.get(getAdapterPosition() - 2);
                    Intent intent = new Intent(mContext, ContentActivity.class);
                    intent.putExtra(ContentActivity.EXTRA_ID, item.getId());
                    mContext.startActivity(intent);
                }
            });
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

    /**
     * 第三个是标题的TextView
     */
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
            viewHolder1.getViewPager().setAdapter(mAdapter);
        }else if(position != 1){
            ViewHolder2 viewHolder2 = (ViewHolder2)holder;
            viewHolder2.getTextView().setText(mDataTodays.get(position - 2).getTitle());
            Picasso.with(mContext)
                    .load(mDataTodays.get(position - 2).getImage())
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
        if(mDataTodays == null){
            return 0;
        }
        return mDataTodays.size()+2;
    }
}
