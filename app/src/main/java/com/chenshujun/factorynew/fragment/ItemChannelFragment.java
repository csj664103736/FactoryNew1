package com.chenshujun.factorynew.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.activity.BaseWebActivity;
import com.chenshujun.factorynew.adapter.RecyclerSocialNewsAdapter;
import com.chenshujun.factorynew.adapter.RefreshFootAdapter;
import com.chenshujun.factorynew.bean.PageBean;
import com.chenshujun.factorynew.tools.DataSource;
import com.chenshujun.factorynew.tools.VolleyUtil;
import com.chenshujun.factorynew.utils.UrlUtil;
import com.chenshujun.factorynew.view.DividerLine;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenshujun on 2016/4/23.
 */
//@EFragment(R.layout.fragment_itemchannel)
public class ItemChannelFragment extends BaseFragment implements RecyclerSocialNewsAdapter.OnItemClickListener {
    private String TAG = "ItemFragment";
    int requestType = -1;
    int curPage = 1;

//    @ViewById(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
//    @ViewById(R.id.recycler_content)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itemchannel,container,false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_content);
        afterView();
        return view;
    }

    //数据源
    String name,channelId;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerSocialNewsAdapter loadAdapter;

    //加载数据
    @Override
    protected void initData() {
        Log.e("LOADVIEW",name+"initData");
        //获取数据
        //获取当前Item的频道名和频道Id
        this.name= getArguments().getString("name");
        this.channelId= getArguments().getString("channelId");
        Log.e(TAG,"channelName:"+name+"  channelId:"+channelId);
//        urlList = new ArrayList<>();
//        titleList = new ArrayList<>();
        showingList = new ArrayList<PageBean.Content>();
        topNews = new ArrayList<PageBean.Content>();
      }

//    @AfterViews
    void afterView(){
        Log.e("LOADVIEW",name+"afterview");
        //配置RecyclerView
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        //recyclerView
        linearLayoutManager = new LinearLayoutManager(getActivity());
        //recyclerView样式：横向、纵向、瀑布
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //adapter
//        recyclerView.setAdapter(loadAdapter = new RecyclerSocialNewsAdapter(getActivity(),showingList,topNews));
//        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
//        dividerLine.setSize(1);
//        dividerLine.setColor(getResources().getColor(R.color.gray_list));
//        dividerLine.setSpace(10);
//        recyclerView.addItemDecoration(dividerLine);
        //Item点击事件
//        loadAdapter.setOnItemClickListener(this);
        //添加监听
        //refreshListener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                requestVolleyNews();
//                Toast.makeText(getActivity(), "onRefresh", Toast.LENGTH_SHORT).show();
//                //刷新结束
//                swipeRefreshLayout.setRefreshing(false);
            }
        });
        //LoadmoreListener
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //如果可见最后一个item的position与adapter中list.size一样
                // adapter返回的数字值是list.size+1，最后的1是footerView
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == loadAdapter.getItemCount() - 1) {
                    loadAdapter.changeMoreStatus(RefreshFootAdapter.LOADING_MORE);
                    //加载数据
//                    List newData = DataSource.getDataSource().loadMoreNews(curPage);
                    List newData = null;
                    newData = loadMoreNewsData(newData);
                    loadAdapter.addMoreItem(newData);
                    loadAdapter.changeMoreStatus(RefreshFootAdapter.PULL_LOAD_MORE);
                    Toast.makeText(getActivity(), "加载更多。。。", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        //进度条显示
//        swipeRefreshLayout.setRefreshing(true);
        //网络请求：获取对应频道的新闻数据
//        requestVolleyNews();
    }

    //网络请求：获取对应频道的新闻数据
    RequestQueue requestQueue;
    private void requestVolleyNews(){
        if(swipeRefreshLayout!=null){
            swipeRefreshLayout.setRefreshing(true);
        }
        curPage = 1;
        Log.e(TAG,name+"requestVolleyNews");
        requestType = VolleyUtil.REQUEST_NEWS;
        requestQueue = Volley.newRequestQueue(getActivity());
        //发送网络请求
        JsonObjectRequest request = new JsonObjectRequest(UrlUtil.getNewAddress(channelId, 1),null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //接受数据，网络数据处理
                DataSource.getDataSource().setSocialNews(jsonObject);
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
        requestQueue.add(request);
//        VolleyUtil.getVolleyUtil().requestNews(getActivity(), UrlUtil.getNewAddress(channelId, 1), requestType, VolleyUtil.METHOD_GET);
    }

    /*=================================处理新闻===============================================*/

    //获取处理完的数据
    List<PageBean.Content> topNews;
    List<PageBean.Content> showingList;
    @Override
    protected void disposeDataSource(Object result) {
        Log.e("LOADVIEW",name+"disposeDataSource");
        List tmpList;
//        //获取头条新闻集合
//        tmpList = DataSource.getDataSource().getTopNews();
//        loadAdapter.addTopNewsItem(tmpList);
//        Log.e(TAG,name+"topNews size:"+tmpList.size());
//        //获取列表新闻集合
//        tmpList = loadMoreNewsData(tmpList);
//        Log.e(TAG,name+"tmpList size:"+tmpList.size());
//        loadAdapter.refreshItem(tmpList);
        //获取头条新闻集合

        topNews = DataSource.getDataSource().getTopNews();
//        loadAdapter.addTopNewsItem(topNews);
//        Log.e(TAG,"topNews size:"+tmpList.size());
        //获取列表新闻集合
        showingList = loadMoreNewsData(showingList);
        Log.e(TAG,"tmpList size:"+showingList.size());
//        loadAdapter.refreshItem(tmpList);
//        loadAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(loadAdapter = new RecyclerSocialNewsAdapter(getActivity(),showingList,topNews));
        loadAdapter.setOnItemClickListener(this);
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(getResources().getColor(R.color.gray_list));
        dividerLine.setSpace(10);
        recyclerView.addItemDecoration(dividerLine);
        swipeRefreshLayout.setRefreshing(false);
    }

    private boolean isFirstLoad = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("LOADVIEW","setUserVisibleHint"+name);
        if(isVisibleToUser){
            Log.e(TAG, name + ":setUserVisibleHint()");
            if(isFirstLoad){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFirstLoad = false;
                        requestVolleyNews();
                    }
                },100);
                isFirstLoad = false;
            }
        }
    }

    private List loadMoreNewsData(List tmpList){
        Log.e("LOADVIEW",name+"loadMoreNewsData");
        tmpList = DataSource.getDataSource().loadMoreNews(curPage);
        curPage++;
        return tmpList;
    }

    @Override
    protected void destroy() {}

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(),"position"+position,Toast.LENGTH_SHORT).show();
        PageBean.Content data= showingList.get(position);
        Intent intent =new Intent(getActivity(), BaseWebActivity.class);
        intent.putExtra("url",data.getLink());
        intent.putExtra("title",data.getTitle());
        startActivity(intent);
    }
}
