package com.chenshujun.factorynew.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.activity.MainActivity;
import com.chenshujun.factorynew.activity.PersonalNewsDetailActivity;
import com.chenshujun.factorynew.activity.PersonalNewsDetailActivity_;
import com.chenshujun.factorynew.adapter.RefreshFootAdapter;
import com.chenshujun.factorynew.bean.PersonalNewsBean;
import com.chenshujun.factorynew.tools.DataSource;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * @note:个人新闻页面
 */
@EFragment(R.layout.fragment_personal)
public class PersonalNewsFragment extends BaseFragment implements RefreshFootAdapter.OnItemClickListener, PersonalNewsDetailActivity.OnDataChangeListener, View.OnClickListener {
    public String TAG = "PersonalNewsFragment";

    @ViewById(R.id.recycler_content)
    RecyclerView recyclerView;
    @ViewById(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.fab)
    FloatingActionButton fab;

    private LinearLayoutManager linearLayoutManager;
    //    private RefreshRecyclerAdapter refreshAdapter;
    private RefreshFootAdapter loadAdapter;

    private List<PersonalNewsBean> newsList;

    @Override
    protected void initView() {
        super.initView();
        Log.e(TAG, "initView1");
        //侧滑界面
        configSlidingMenu();
        //获取数据：首先获取本地数据，再加载网络数据
        newsList = DataSource.getDataSource().getPersonalNews(getActivity());
        for (int i = 0; i < newsList.size(); i++) {
            Log.e(TAG, newsList.get(i).toString());
        }
    }

    @Override
    protected void destroy() {
        //解除SlidingMenu绑定
        disableSliding();
    }
    @AfterViews
    void afterView() {
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.social));
        ((MainActivity)getContext()).setSupportActionBar(toolbar);
        ((MainActivity)getContext()).getSupportActionBar().setTitle(getString(R.string.news_personal));
        ((MainActivity)getContext()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity)getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        //添加分隔线
//        recyclerView.addItemDecoration(new AdvanceDecoration(this, OrientationHelper.VERTICAL));
        //refreshAdapter
        for (int i = 0; i < newsList.size(); i++) {
            Log.e(TAG, newsList.get(i).toString());
        }
//        recyclerView.setAdapter(refreshAdapter = new RefreshRecyclerAdapter(getActivity(), newsList));
//        loadAdapter = new RefreshFootAdapter(getActivity(),newsList);
        recyclerView.setAdapter(loadAdapter = new RefreshFootAdapter(getActivity(), newsList));
        loadAdapter.setOnItemClickListener(this);
        //添加监听
        //refreshListener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "onRefresh", Toast.LENGTH_SHORT).show();
                //刷新结束
                swipeRefreshLayout.setRefreshing(false);
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
                    List newData = DataSource.getDataSource().getMorePersonalNews(getActivity());
                    loadAdapter.addMoreItem(newData);
                    loadAdapter.changeMoreStatus(RefreshFootAdapter.PULL_LOAD_MORE);
                    Toast.makeText(getActivity(), "加载更多。。。", Toast.LENGTH_SHORT).show();
                }
            }
            //            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                //如果是滑动状态，且可见的最后一个元素与adapter中list的count数相等，则说明滑动到达了底部
//                if(newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1 == refreshAdapter.getItemCount()){
//                    List newData = new ArrayList();
//                    for(int i = 0; i < 10; i++){
//                        newData.add("newData"+i);
//                    }
//                    refreshAdapter.addMoreItem(newData);
//                    Toast.makeText(getActivity(),"加载更多。。。",Toast.LENGTH_SHORT).show();
//                }
//            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        fab.setOnClickListener(this);

        //datachangeListener
//        PersonalNewsDetailActivity_.setOnDataChangeListener(this);
        PersonalNewsDetailActivity.setOnDataChangeListener(this);
    }

    /*============================CardViewClick===========================================*/
    //记录打开item的position
    int curPosition = -1;
    @Override
    public void onItemClick(View view, int position) {
        TextView tvUser = (TextView) view.findViewById(R.id.tv_user);
        Toast.makeText(getActivity(),"position"+position,Toast.LENGTH_SHORT).show();
        tvUser.setText("position"+position);
        Intent intent = new Intent(getActivity(), PersonalNewsDetailActivity_.class);
//        Intent intent = new Intent(getActivity(), PersonalNewsDetailActivity.class);
        intent.putExtra("data",newsList.get(position));
        curPosition = position;
//        Intent intent = new Intent(getActivity(), PersonalNewsDetailActivity.class);
//        BaseFragment fragment = PersonalNewsDetailFragment_.builder().build();
        startActivity(intent);
    }

    @Override
    public void onZanButtonClick(int position, List<PersonalNewsBean> itemList) {
        itemList.get(position).isZan = !itemList.get(position).isZan;
        if(itemList.get(position).isZan){
            itemList.get(position).userName = "true";
        }
        else{
            itemList.get(position).userName = "false";
        }
        //notify调用后，会重新调用onBindViewHolder函数
        loadAdapter.notifyItemChanged(position);
    }

    @Override
    public void onSocializeClick(int position, List<PersonalNewsBean> itemList) {
        itemList.get(position).isSocialize = !itemList.get(position).isSocialize;
        if(itemList.get(position).isSocialize){
            itemList.get(position).pubDate = "true";
        }
        else{
            itemList.get(position).pubDate = "false";
        }
        loadAdapter.notifyItemChanged(position);
    }

    @Override
    public void onShareButtonClick(int position, List<PersonalNewsBean> itemList) {
        Toast.makeText(getActivity(),"share"+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentButtonClick(int position, List<PersonalNewsBean> itemList) {
        Toast.makeText(getActivity(),"commont"+position,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void notifyDataListChange(PersonalNewsBean personalNewsBean) {
        newsList.set(curPosition,personalNewsBean);
        loadAdapter.notifyItemChanged(curPosition);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getActivity(),"Float",Toast.LENGTH_SHORT).show();
    }
}
