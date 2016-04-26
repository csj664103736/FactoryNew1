package com.chenshujun.factorynew.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.bean.PersonalNewsBean;

import java.util.List;

/**
 * @note:RecyclerView Adapter
 */
public class RefreshRecyclerAdapter extends RecyclerView.Adapter<RefreshRecyclerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<PersonalNewsBean> itemList;

    public RefreshRecyclerAdapter(Context context, List<PersonalNewsBean> list) {
        this.mInflater = LayoutInflater.from(context);
        this.itemList = list;
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.item_recycler_news_personal, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        //view.setBackgroundColor(Color.RED);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PersonalNewsBean personalNewsBean = itemList.get(position);
        holder.tvUser.setText(personalNewsBean.userName);
        holder.tvDate.setText(personalNewsBean.pubDate);
        holder.tvTitle.setText(personalNewsBean.title);
        holder.tvDesc.setText(personalNewsBean.desc);
        holder.itemView.setTag(position);
        //如果已经赞/社会化，则图标变化
        if(itemList.get(position).isZan){
            holder.ibZan.setImageResource(R.drawable.zan_press);
        }
        if(itemList.get(position).isSocialize){
            holder.ibSocialize.setImageResource(R.drawable.social_press);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivUser;
        public TextView tvUser,tvDate,tvTitle, tvDesc;
        public ImageButton ibDispacher,ibComment,ibSocialize,ibZan;


        public ViewHolder(View view) {
            super(view);
            ivUser = (ImageView) view.findViewById(R.id.iv_user);
            tvUser = (TextView) view.findViewById(R.id.tv_user);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            ibDispacher = (ImageButton) view.findViewById(R.id.ib_dispacher);
            ibComment = (ImageButton) view.findViewById(R.id.ib_comment);
            ibSocialize = (ImageButton) view.findViewById(R.id.ib_socialize);
            ibZan = (ImageButton) view.findViewById(R.id.ib_zan);
        }
    }

    //添加数据
    public void addItem(List<PersonalNewsBean> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(itemList);
        itemList.removeAll(itemList);
        itemList.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<PersonalNewsBean> newDatas) {
        itemList.addAll(newDatas);
        notifyDataSetChanged();
    }
}
