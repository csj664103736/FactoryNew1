package com.chenshujun.factorynew.page;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.activity.BaseActivity;
import com.chenshujun.factorynew.adapter.ListZanAdapter;
import com.chenshujun.factorynew.bean.ZanBean;
import com.chenshujun.factorynew.view.PullToRefreshLayout;
import com.chenshujun.factorynew.view.PullableListView;

import org.androidannotations.annotations.EView;

import java.util.ArrayList;
import java.util.List;

/**
 * @note:功能列表页
 * @param :PAGETYPE:0-赞 1-社会化 2-评论
 */
public class FunctionItemPage extends BasePage implements PullToRefreshLayout.OnRefreshListener {

    PullToRefreshLayout ptrl;
    PullableListView listView;
    View headView;

    int PAGETYPE = 0;
    String requestUrl = null;
    List dataList;


    public FunctionItemPage(Context ct, int i) {
        super(ct);
        this.PAGETYPE = i;
    }

    //父类构造函数时执行
    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.page_function,null);
        //获取组件
        ptrl = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        listView = (PullableListView) view.findViewById(R.id.content_view);
        headView = view.findViewById(R.id.head_view);

        //listView
        initListView();

        //监听
        ptrl.setOnRefreshListener(this);

        //界面配置
        headView.setVisibility(View.GONE);
        return view;
    }

    BaseAdapter adapter;
    //配置ListView
    private void initListView(){
        listView.setAdapter(adapter);
        //listItem监听
    }

    //初始化页面配置
    @Override
    public void initConfig(){
        if(dataList == null) dataList = new ArrayList();
        switch (PAGETYPE){
            //当前页面为赞
            case 0:
                requestUrl = "zan";
                adapter = new ListZanAdapter(dataList,context);
//                itemLayout = R.layout.item_list_zan;
                break;
            //当前页面为评论
            case 1:
                requestUrl = "comment";
//                itemLayout = R.layout.item_list_comment;
                break;
        }
    }

    //获取数据
    @Override
    public void initData(){
        if(dataList == null){
            dataList = new ArrayList();
        }
        //向服务器发送请求 url
        for(int i =0; i < 30; i++){
            dataList.add(new ZanBean("http://ww2.sinaimg.cn/crop.0.0.1080.1080.1024/7d5d4351jw8es526khhycj20u00u0q5b.jpg","user"+i));
        }
//        adapter.notifyDataSetChanged();
    }

    //-->ListView
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {}

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
