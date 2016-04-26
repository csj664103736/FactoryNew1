package com.chenshujun.factorynew.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.activity.MainActivity;
import com.chenshujun.factorynew.adapter.ListNewsAdapter;
import com.chenshujun.factorynew.bean.ChannelBean;
import com.chenshujun.factorynew.bean.PageBean;
import com.chenshujun.factorynew.utils.CommonUtil;
import com.chenshujun.factorynew.utils.GsonUtil;
import com.chenshujun.factorynew.utils.UrlUtil;
import com.chenshujun.factorynew.tools.VolleyUtil;
import com.chenshujun.factorynew.view.PullToRefreshLayout;
import com.chenshujun.factorynew.view.PullableListView;
import com.chenshujun.factorynew.view.RollViewPager;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPage页面
 */
public class ItemFragment extends Fragment implements RollViewPager.OnPagerClickCallback, VolleyUtil.VolleyFinishListener {
    private String TAG = "ItemFragment";
    Context context;
    int requestType = -1;
    int topSize = 3;

    @ViewInject(R.id.top_news_viewpager)
    private LinearLayout mViewPagerLay;
    //布局
    @ViewInject(R.id.dots_ll)
    private LinearLayout dotLl;
    @ViewInject(R.id.top_news_title)
    private TextView topNewsTitle;
    RollViewPager rpImgNews;
    RelativeLayout rlImgNews;
    PullableListView lvNews;
    PullToRefreshLayout ptrl;

    //数据源
    ArrayList dotList;
    private ArrayList<String> titleList, urlList;
//    ImageView imageView;
    ListNewsAdapter listNewsAdapter;
    String name,channelId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        Log.e("create new",""+this);
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        //获取组件
        //->滚动新闻
        rpImgNews = new RollViewPager(context,dotList,R.drawable.dot_focus,R.drawable.dot_normal);
        rpImgNews.setOnPagerClickCallback(this);
        mViewPagerLay = (LinearLayout) view.findViewById(R.id.top_news_viewpager);
        rlImgNews = (RelativeLayout) view.findViewById(R.id.rl_img_news);
        topNewsTitle = (TextView) view.findViewById(R.id.top_news_title);
        lvNews = (PullableListView) view.findViewById(R.id.content_view);
        ptrl = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);

        //接口回调
        VolleyUtil.getVolleyUtil().setOnVolleyFinishListener(this);

        //获取数据
        //获取当前Item的频道名和频道Id
        this.name= getArguments().getString("name");
        this.channelId= getArguments().getString("channelId");
        urlList = new ArrayList<>();
        titleList = new ArrayList<>();
        showingList = new ArrayList<PageBean.Content>();
        requestType = VolleyUtil.REQUEST_NEWS;
        showLoadingView();
        //网络请求：获取对应频道的新闻数据
        VolleyUtil.getVolleyUtil().requestNews(getActivity(), UrlUtil.getNewAddress(channelId, 1), requestType, VolleyUtil.METHOD_GET);


        //配置监听
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

                // 刷新完毕,通知下拉头消失
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                loadMoreNews();
                // 加载完毕,通知下拉头消失
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });

//        }

        //界面配置
        //->配置自定义ViewPager的参数
        rpImgNews.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
//        ((MainActivity)context).unLock();
        return view;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestry"+this);
        super.onDestroy();
    }

    //ListView初始化方法
    private void initListView() {
        listNewsAdapter = new ListNewsAdapter(getActivity(), showingList);
        lvNews.setAdapter(listNewsAdapter);
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                disposeOnNewsListItemClick(contentList,position);
            }
        });
    }

    //列表新闻点击
    private void disposeOnNewsListItemClick(List<PageBean.Content> list,int position){
        Toast.makeText(getActivity(),list.get(position).getTitle(),Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getActivity(), DetailNewsActivitySocialNews.class);
//        intent.putExtra("url",list.get(position).getLink());
//        intent.putExtra("title",list.get(position).getTitle());
//        startActivity(intent);
    }
    //处理获取的新闻内容
    List<PageBean.Content> contentList;
    private void disposeVolleyNews(JSONObject resultJson){
        PageBean tmpPage;
        try {
            JSONObject bodyJson = resultJson.getJSONObject("showapi_res_body");
            JSONObject pageJson = bodyJson.getJSONObject("pagebean");
            tmpPage = GsonUtil.changeGsonToBean(pageJson.toString(), PageBean.class);
            contentList = tmpPage.getContentlist();
            //图片新闻
            disposeImgNews();
            //添加列表新闻第一页,一定要在图片新闻之后，否则新闻会重复
            loadMoreNews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //处理图片新闻
    List<PageBean.Content> imgNews;
    private void disposeImgNews(){
        List<PageBean.Content.ImageUrl> tmpImg;
        PageBean.Content tmpContent;
        //遍历所有新闻内容
        for(int i =0; i < contentList.size(); i++){
            Log.e(TAG,"contentList:"+contentList.get(i).getTitle());
            tmpContent = contentList.get(i);
            //获取imageurl字段内容，如果有图片，就加入到titleList和urlList中
            tmpImg = tmpContent.getImageurls();
            if(tmpImg!= null && tmpImg.size()>0){
                titleList.add(tmpContent.getTitle());
                urlList.add(tmpImg.get(0).getUrl());
                //如果imgNews为null，则新建imgNews
                if(imgNews == null){
                    imgNews = new ArrayList<PageBean.Content>();
                }
                //imgNews添加子元素，从原有list中删除已经展示的新闻项
                imgNews.add(tmpContent);
                contentList.remove(i);
            }
            //当达到展示图片的数量后，跳出循环
            if(urlList.size() >= topSize){
                break;
            }
        }
        Log.e(TAG,"topNews size:"+urlList.size());
        for(int i =0; i < urlList.size(); i++){
            Log.e(TAG,"topNews:"+urlList.get(i).toString());
        }
        //当有图片新闻时，加载图片新闻显示区域
        if(urlList.size() > 0){
//                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//                ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
//                    @Override
//                    public Bitmap getBitmap(String s) {
//                        return null;
//                    }
//
//                    @Override
//                    public void putBitmap(String s, Bitmap bitmap) {
//
//                    }
//                });
//
//                ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,R.drawable.load_default,R.drawable.load_error);
//
////                Log.e("VOLLEYURL","http://img.my.csdn.net/uploads/201403/03/1393854083_2323.jpg");
//                imageLoader.get("http://img.my.csdn.net/uploads/201403/03/1393854083_2323.jpg",listener);
            Log.e(TAG,"rlImgNews visibility show");
            //图片新闻不可见
            rlImgNews.setVisibility(View.VISIBLE);
            // top新闻的图片地址
            rpImgNews.setUriList(urlList);
            rpImgNews.setTitle(topNewsTitle, titleList);
            //开启线程  并实例化
            rpImgNews.startRoll();
            mViewPagerLay.removeAllViews();
            //添加View
            mViewPagerLay.addView(rpImgNews);
        }
        else {
            Log.e(TAG,"rlImgNews visibility gone");
            // 图片新闻不可见
            rlImgNews.setVisibility(View.GONE);
        }
    }

    //加载更多新闻数据
    int curPage = 1;
    int REGION = 7;
    List<PageBean.Content> showingList;
    private void loadMoreNews(){
        Log.e(TAG,"currentPage:"+curPage);
        if(contentList == null){
            Log.e(TAG,"contentlist is null");
        }
        else{
            Log.e(TAG,"contentlist not null");
        }
        for(int i =(curPage-1)*REGION; i < curPage*REGION && i<contentList.size(); i++){
            showingList.add(contentList.get(i));
        }
        curPage++;

        //List
        initListView();
//        listNewsAdapter.notifyDataSetChanged();
        for(int i =0; i < showingList.size(); i++){
            Log.e(TAG, "showingList:" + showingList.get(i).getTitle());
        }
    }

    /*=============================新闻滚动页面========================================*/
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

    //滚动新闻图片点击事件
    @Override
    public void onPagerClick(int position) {

    }

    /*=============================公用方法===========================================*/
    @Override
    public void onVolleyFinish(int isSuccess, Object result) {
        Log.e(TAG, "VolleyFinish:" + result.toString());
        switch (isSuccess){
            case VolleyUtil.SUCCESS_SUCCESS:
                disposeVolleySuccess(result);
                break;
            case VolleyUtil.FAILED_FAILED:
                break;
        }
        ((MainActivity)getActivity()).loadingView.dismiss();
    }
    //Volley响应成功
    private void disposeVolleySuccess(Object result){
        switch (requestType){
            //请求新闻
            case VolleyUtil.REQUEST_NEWS:
                disposeVolleyNews((JSONObject) result);
                break;
        }
    }
    private void showLoadingView(){
        if(!((MainActivity)getActivity()).loadingView.isShow()){
            ((MainActivity)getActivity()).loadingView.show();
        }
    }

}
