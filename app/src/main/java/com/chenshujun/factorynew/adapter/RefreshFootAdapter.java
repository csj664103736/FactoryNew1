package com.chenshujun.factorynew.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.bean.PersonalNewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenshujun on 2016/4/11.
 */
public class RefreshFootAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    //普通ItemView
    private static final int TYPE_ITEM = 0;
    //FooterView
    private static final int TYPE_FOOTER = 1;
    //滑动状态：上拉状态
    public static final int PULL_LOAD_MORE = 0;
    //滑动状态：正在刷新
    public static final int LOADING_MORE = 1;

    private int load_more_status = PULL_LOAD_MORE;

    private LayoutInflater inflater;

    private List<PersonalNewsBean> itemList;

    public RefreshFootAdapter(Context context, List<PersonalNewsBean> mlist) {
        //获取布局解析器
        this.inflater = LayoutInflater.from(context);
        this.itemList = mlist;
    }

    /**
     * @note:item的View显示
     * */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果是普通item，实例化item的viewHolder
        if(TYPE_ITEM == viewType){
            View view = inflater.inflate(R.layout.item_recycler_news_personal,parent,false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            //配置监听，ViewHolder是静态类，占据内存太多
            itemViewHolder.itemView.setOnClickListener(this);
            itemViewHolder.ibZan.setOnClickListener(this);
            itemViewHolder.ibSocialize.setOnClickListener(this);
            itemViewHolder.ibComment.setOnClickListener(this);
            itemViewHolder.ibDispacher.setOnClickListener(this);
            return itemViewHolder;
        }
        //如果是页脚item，实例化Footer的ViewHoler
        else if(TYPE_FOOTER == viewType){
            View view = inflater.inflate(R.layout.recycler_load_more_layout,parent,false);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;
        }
        return null;
    }

    /**
     * @note:item的数据绑定显示*/
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //如果是ItemHolder
        if(holder instanceof ItemViewHolder){
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            //界面数据
            PersonalNewsBean personalNewsBean = itemList.get(position);
            itemViewHolder.tvUser.setText(personalNewsBean.userName+position);
            itemViewHolder.tvDate.setText(personalNewsBean.pubDate);
            itemViewHolder.tvTitle.setText(personalNewsBean.title);
            itemViewHolder.tvDesc.setText(personalNewsBean.desc);
            //如果已经赞/社会化，则图标变化
            if(itemList.get(position).isZan){
                itemViewHolder.ibZan.setImageResource(R.drawable.zan_press);
            }
            else {
                itemViewHolder.ibZan.setImageResource(R.drawable.zan);
            }
            if(itemList.get(position).isSocialize){
                itemViewHolder.ibSocialize.setImageResource(R.drawable.social_press);
            }
            else {
                itemViewHolder.ibSocialize.setImageResource(R.drawable.social);
            }
            //为View设置Tag，保存当前View在List中的position
            itemViewHolder.ibZan.setTag(position);
            itemViewHolder.ibSocialize.setTag(position);
            itemViewHolder.itemView.setTag(position);
            itemViewHolder.ibDispacher.setTag(position);
            itemViewHolder.ibComment.setTag(position);
        }
        //如果是FooterHolder
        else if(holder instanceof FootViewHolder){
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status){
                case PULL_LOAD_MORE:
                    footViewHolder.tvFooter.setText("上拉加载更多");
                    break;
                case LOADING_MORE:
                    footViewHolder.tvFooter.setText("正在加载...");
                    break;
            }
        }
    }


    /**
     * @note:判断是普通View还是FooterView*/
    @Override
    public int getItemViewType(int position) {
        if(position + 1== getItemCount()){
            return TYPE_FOOTER;
        }
        else{
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size()+1;
    }

    public void addItem(List newDatas){
        newDatas.addAll(itemList);
        itemList.removeAll(itemList);
        itemList.addAll(newDatas);
        notifyDataSetChanged();
    }

    //添加数据
    public void addMoreItem(List newDatas){
        itemList.addAll(newDatas);
        notifyDataSetChanged();
    }

    //修改RecyclerView的状态：load状态还是普通状态
    public void changeMoreStatus(int status){
        load_more_status = status;
        notifyDataSetChanged();
    }
    //赞按钮点击
//    private void disposeZanOnClick(int curPosition){
////        curPosition = (int) itemViewHolder.itemView.getTag();
//        Log.e("RefreshFootAdapter","Before OnClick:"+curPosition+itemList.get(curPosition).isZan);
//        itemList.get(curPosition).isZan = !itemList.get(curPosition).isZan;
//        if(itemList.get(curPosition).isZan){
//            itemList.get(curPosition).userName = "true";
//        }
//        else{
//            itemList.get(curPosition).userName = "false";
//        }
//        Log.e("RefreshFootAdapter","After OnClick:"+curPosition+itemList.get(curPosition).isZan);
//        //notify调用后，会重新调用onBindViewHolder函数
//        notifyItemChanged(curPosition);
//    }

//    //社会化按钮点击
//    private void disposeSocializeOnClick(int curPosition){
////        curPosition = (int) itemViewHolder.itemView.getTag();
//        Log.e("RefreshFootAdapter","Before OnClick:"+curPosition+itemList.get(curPosition).isZan);
//        itemList.get(curPosition).isSocialize = !itemList.get(curPosition).isSocialize;
//        if(itemList.get(curPosition).isSocialize){
//            itemList.get(curPosition).pubDate = "true";
//        }
//        else{
//            itemList.get(curPosition).pubDate = "false";
//        }
//        Log.e("RefreshFootAdapter","After OnClick:"+curPosition+itemList.get(curPosition).isSocialize);
//        notifyItemChanged(curPosition);
//    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (v.getId()){
            //赞
            case R.id.ib_zan:
                if(onItemClickListener != null)
                    onItemClickListener.onZanButtonClick(position,itemList);
                break;
            //社会化
            case R.id.ib_socialize:
                if(onItemClickListener != null)
                    onItemClickListener.onSocializeClick(position,itemList);
                break;
            //转发
            case R.id.ib_dispacher:
                if(onItemClickListener != null)
                    onItemClickListener.onShareButtonClick(position,itemList);
                break;
            //评论
            case R.id.ib_comment:
                if(onItemClickListener != null)
                    onItemClickListener.onCommentButtonClick(position,itemList);
                break;
            //默认为CardView的点击事件
            default:
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v, position);
                }
        }
    }

    /*===================================Item点击事件=====================================*/
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        //Item点击事件
        void onItemClick(View view,int position);
        //赞按钮点击事件
        void onZanButtonClick(int position, List<PersonalNewsBean> itemList);
        //社会化按钮点击事件
        void onSocializeClick(int position,List<PersonalNewsBean> itemList);
        //转发按钮点击事件
        void onShareButtonClick(int position, List<PersonalNewsBean> itemList);
        //评论按钮点击事件
        void onCommentButtonClick(int position, List<PersonalNewsBean> itemList);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    /*===========================ViewHoler==========================================*/
    //普通CardItem的ViewHolder
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivUser;
        public TextView tvUser,tvDate,tvTitle, tvDesc;
        public ImageButton ibDispacher,ibComment,ibSocialize,ibZan;


        public ItemViewHolder(View view) {
            super(view);
            //获取组件
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

    //FootItem的ViewHolder
    public static class FootViewHolder extends RecyclerView.ViewHolder{
        public TextView tvFooter;

        public FootViewHolder(View itemView) {
            super(itemView);
            tvFooter = (TextView) itemView.findViewById(R.id.foot_view_item_tv);
        }
    }

}
