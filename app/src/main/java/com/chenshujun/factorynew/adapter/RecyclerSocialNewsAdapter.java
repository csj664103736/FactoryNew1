package com.chenshujun.factorynew.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.bean.PageBean;
import com.chenshujun.factorynew.tools.DataSource;
import com.chenshujun.factorynew.utils.CommonUtil;
import com.chenshujun.factorynew.view.RollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @note:社会新闻Adapter
 */
public class RecyclerSocialNewsAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    //头条新闻ItemView
    private static final int TYPE_TOP = 3;
    //普通ItemView
    private static final int TYPE_ITEM = 0;
    //图片新闻ItemView
    private static final int TYPE_IMAGE = 2;
    //FooterView
    private static final int TYPE_FOOTER = 1;

    //滑动状态：上拉状态
    public static final int PULL_LOAD_MORE = 0;
    //滑动状态：正在刷新
    public static final int LOADING_MORE = 1;

    private int load_more_status = PULL_LOAD_MORE;

    private LayoutInflater inflater;

    private List<PageBean.Content> newsList,topNewsList;
    private ArrayList<String> titleList = new ArrayList<>(), urlList = new ArrayList<>();

    private static Context context;
    int hasTopNews = 1;
    /**
     * @note:社会新闻Adapter
     * @param: imgNewsList 图片新闻Bean集合
     * @param：newsList 头条新闻集合*/
    public RecyclerSocialNewsAdapter(Context context,List<PageBean.Content> newsList, List<PageBean.Content> topNewsList) {
        //获取布局解析器
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.newsList = newsList;
        this.topNewsList = topNewsList;

        //提取出top新闻的标题和图片地址集合
        getTopList();
    }

    private void getTopList(){
        titleList.removeAll(titleList);
        urlList.removeAll(urlList);
        PageBean.Content tmpContent;
        List<PageBean.Content.ImageUrl> tmpImg;
        for(int i = 0; i < topNewsList.size(); i++){
            tmpContent = topNewsList.get(i);
            tmpImg = tmpContent.getImageurls();
            //图片标题
            titleList.add(tmpContent.getTitle());
            //图片地址
            urlList.add(tmpImg.get(0).getUrl());
        }
        Log.e(TAG,"topNewsList："+topNewsList.size());
        Log.e(TAG,"urllist："+urlList.size());

        for(int i = 0; i < urlList.size(); i++)
        {
            Log.e(TAG,"urllist："+urlList.get(i));
        }
        if(urlList.size() <= 0){
            hasTopNews = 0;
        }
    }

    /**
     * @note:item的View显示
     * */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果是普通item，实例化item的viewHolder
        if(TYPE_ITEM == viewType){
            View view = inflater.inflate(R.layout.item_list_news_noimg,parent,false);
            TextViewHolder itemViewHolder = new TextViewHolder(view);
            //配置监听，ViewHolder是静态类，占据内存太多
            itemViewHolder.itemView.setOnClickListener(this);
            return itemViewHolder;
        }
        //如果是图片item
        else if(TYPE_IMAGE == viewType){
            View view = inflater.inflate(R.layout.item_list_news,parent,false);
            ImgViewHolder imgViewHolder = new ImgViewHolder(view);
            imgViewHolder.itemView.setOnClickListener(this);
            return imgViewHolder;
        }
        //如果是页脚item，实例化Footer的ViewHoler
        else if(TYPE_FOOTER == viewType){
            View view = inflater.inflate(R.layout.recycler_load_more_layout,parent,false);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;
        }
        //如果是头条item
        else if(TYPE_TOP == viewType){
            View view = inflater.inflate(R.layout.layout_scrollnews,parent,false);
            TopViewHolder topViewHolder = new TopViewHolder(view);
            return topViewHolder;
        }
        return null;
    }

    /**
     * @note:item的数据绑定显示*/
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //如果是普通ItemHolder
        if(holder instanceof TextViewHolder){
            final TextViewHolder textViewHolder = (TextViewHolder) holder;
            position = (position - 1)<0?0:position-1;
            //界面数据
            PageBean.Content newsBean = newsList.get(position);
            textViewHolder.tvTitle.setText(newsBean.getTitle());
            textViewHolder.tvContent.setText(newsBean.getDesc());
        }
        //如果是图片新闻
        else if(holder instanceof ImgViewHolder){
            position = (position - 1)<0?0:position-1;
            PageBean.Content newsBean = newsList.get(position);
            ImgViewHolder imgViewHolder = (ImgViewHolder) holder;
            imgViewHolder.tvTitle.setText(newsBean.getTitle());
            imgViewHolder.tvContent.setText(newsBean.getDesc());

            //填充数据
            Glide.with(context)
                    .load(newsBean.getImageurls().get(0).getUrl())
                    .into(imgViewHolder.imageView);
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
        //如果是头条新闻
        else if(holder instanceof TopViewHolder){
            TopViewHolder topViewHolder = (TopViewHolder) holder;
            if(urlList.size() <= 0){
                topViewHolder.rlImgNews.setVisibility(View.GONE);
            }
            else{
                topViewHolder.rlImgNews.setVisibility(View.VISIBLE);
                topViewHolder.rpImgNews.setUriList(urlList);
                topViewHolder.rpImgNews.setTitle(topViewHolder.topNewsTitle,titleList);
                //开启线程实例化
                topViewHolder.rpImgNews.startRoll();
                topViewHolder.rpImgNews.notifyDataChange();
                topViewHolder.mViewPagerLay.removeAllViews();
                //添加View
                topViewHolder.mViewPagerLay.addView(topViewHolder.rpImgNews);
            }
//            // top新闻的图片地址
//            rpImgNews.setUriList(urlList);
//            rpImgNews.setTitle(topNewsTitle, titleList);
//            //开启线程  并实例化
//            rpImgNews.startRoll();
//            mViewPagerLay.removeAllViews();
//            //添加View
//            mViewPagerLay.addView(rpImgNews);
        }
        //为View设置Tag，保存当前View在List中的position
        holder.itemView.setTag(position);
    }


    private String TAG = "RecyclerSocialAdapter";
    /**
     * @note:判断是普通View还是FooterView*/
    @Override
    public int getItemViewType(int position) {
//        Log.e(TAG,"position:"+position+"  getItemCount:"+getItemCount());
        //当position == 0
        if(position == 0){
            //如果当前频道有头条新闻
            if(topNewsList.size()>0){
//                Log.e(TAG,"position"+position+"is TYPE_TOP");
                return TYPE_TOP;
            }
            //如果当前频道没有头条新闻，第一个是图片新闻
            else if(newsList != null&& newsList.size()>0 && newsList.get(position).getImageurls().size() > 0) {
//                Log.e(TAG,"position"+position+"is TYPE_IMAGE");
                return TYPE_IMAGE;
            }
            //如果当前频道没有头条新闻，第一个是普通新闻
            else if( newsList != null&& newsList.size()>0){
//                Log.e(TAG,"position"+position+"is TYPE_ITEM");
                return TYPE_ITEM;
            }
        }
        //position不是0
        else{
            //返回FooterView
            if(position + 1== getItemCount()){
            //      if(position +hasTopNews+ 1== getItemCount() && position!=0){
//                Log.e(TAG,"position"+position+"is TYPE_FOOTER");
                return TYPE_FOOTER;
            }
            //返回图片新闻View
            if(newsList != null&& newsList.size()>0&& position<=newsList.size() && newsList.get(position-1).getImageurls().size() > 0){
//                Log.e(TAG,"position"+position+"is TYPE_IMAGE");
                return TYPE_IMAGE;
            }
            //返回普通新闻View
            if( newsList != null&& newsList.size()>0 && position <= newsList.size()){
//                Log.e(TAG,"position"+position+"is TYPE_ITEM");
                return TYPE_ITEM;
            }
        }
//        //返回FooterView
//        if(position + 1== getItemCount() && position!=0){
////      if(position +hasTopNews+ 1== getItemCount() && position!=0){
//            Log.e(TAG,"position"+position+"is TYPE_FOOTER");
//            return TYPE_FOOTER;
//        }
//        //返回头条新闻View 如果是第一个位置且当前频道有图片新闻
//        if(position == 0 && (topNewsList.size()>0)){
//            Log.e(TAG,"position"+position+"is TYPE_TOP");
//            return TYPE_TOP;
//        }
//        //返回图片新闻View，当有top新闻时
//        if(position!= 0 && newsList != null&& newsList.size()>0&& position<=newsList.size() && newsList.get(position-1).getImageurls().size() > 0){
//            Log.e(TAG,"position"+position+"is TYPE_IMAGE");
//            return TYPE_IMAGE;
//        }
//        //返回图片新闻view，当没有top新闻时
//        if(topNewsList.size()==0 && position == 0&& newsList != null&& newsList.size()>0&& position<=newsList.size() && newsList.get(position-1).getImageurls().size() > 0){
//            Log.e(TAG,"position"+position+"is TYPE_IMAGE");
//            return TYPE_IMAGE;
//        }
//        //返回普通新闻View
//        if(position!= 0&& newsList != null&& newsList.size()>0 && position <= newsList.size()){
//            Log.e(TAG,"position"+position+"is TYPE_ITEM");
//            return TYPE_ITEM;
//        }
//        //返回普通新闻View，当没有top新闻时
//        if(topNewsList.size()==0 && position == 0&& newsList != null&& newsList.size()>0 && position <= newsList.size()){
//            Log.e(TAG,"position"+position+"is TYPE_ITEM");
//            return TYPE_ITEM;
//        }
        return -1;
    }

    //第一个1是topNews，第二个1是footerView
    @Override
    public int getItemCount() {
        //如果有新闻
        if(newsList.size()>0){
            return newsList.size()+hasTopNews+1;
        }
        //如果没有新闻
        else{
            return newsList.size()+hasTopNews;
        }
    }

    public void addItem(List newDatas){
        newDatas.addAll(newsList);
        newsList.removeAll(newsList);
        newsList.addAll(newDatas);
        notifyDataSetChanged();
    }

    //添加数据
    public void addMoreItem(List newDatas){
        newsList.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void refreshItem(List newDatas){
        newsList.removeAll(newsList);
        newsList.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addTopNewsItem(List newDatas){
        topNewsList.removeAll(topNewsList);
        topNewsList.addAll(newDatas);
        getTopList();
        hasTopNews = 1;
        notifyDataSetChanged();
    }

    //修改RecyclerView的状态：load状态还是普通状态
    public void changeMoreStatus(int status){
        load_more_status = status;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if(onItemClickListener != null){
            onItemClickListener.onItemClick(v, position);
        }
    }

    /*===================================Item点击事件=====================================*/
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        //Item点击事件
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    /*===========================ViewHoler==========================================*/
    //TopView的ViewHolder
    public static class TopViewHolder extends RecyclerView.ViewHolder{
        RollViewPager rpImgNews;
        RelativeLayout rlImgNews;
        LinearLayout mViewPagerLay;
        TextView topNewsTitle;
        ArrayList dotList;
        LinearLayout dotLl;

        public TopViewHolder(View itemView) {
            super(itemView);
            dotLl = (LinearLayout) itemView.findViewById(R.id.dots_ll);
            //初始化图片点
            initDot(DataSource.TOPSIZE);
            //获取组件
            rpImgNews = new RollViewPager(context,dotList,R.drawable.dot_focus,R.drawable.dot_normal);
            mViewPagerLay = (LinearLayout) itemView.findViewById(R.id.top_news_viewpager);
            rlImgNews = (RelativeLayout) itemView.findViewById(R.id.rl_img_news);
            topNewsTitle = (TextView) itemView.findViewById(R.id.top_news_title);
            //->配置自定义ViewPager的参数
            rpImgNews.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        /**
         * @note：加载图片新闻的索引点
         * @param size 图片新闻的数量
         */
        private void initDot(int size) {
            dotList = new ArrayList<View>();
            dotLl.removeAllViews();
            for (int i = 0; i < size; i++) {
                //第一个参数是高度，第二个参数是宽度
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        CommonUtil.changeDipToPx(context, 6), CommonUtil.changeDipToPx(context, 6));
                //设置布局边界
                params.setMargins(5, 0, 5, 0);
                //新建点的布局
                View dotView = new View(context);
                if (i == 0) {
                    dotView.setBackgroundResource(R.drawable.dot_focus);
                } else {
                    dotView.setBackgroundResource(R.drawable.dot_normal);
                }
                //给布局添加参数
                dotView.setLayoutParams(params);
                //给Layout添加布局View
                dotLl.addView(dotView);
                //向布局集合（ArrayList）中添加itemView
                dotList.add(dotView);
            }
        }
    }
    //带图片的ViewHolder
    public static class ImgViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tvTitle;
        TextView tvContent;

        public ImgViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvContent = (TextView) itemView.findViewById(R.id.content);
        }
    }
    //不带图片的ViewHolder
    public static class TextViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvContent;

        public TextViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvContent = (TextView) itemView.findViewById(R.id.content);
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
    /*=============================新闻滚动页面========================================*/

}
