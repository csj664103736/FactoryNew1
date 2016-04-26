package com.chenshujun.factorynew.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.activity.MainActivity;
import com.chenshujun.factorynew.adapter.NewsFragmentAdapter;
import com.chenshujun.factorynew.bean.ChannelBean;
import com.chenshujun.factorynew.tools.DataSource;

import java.util.List;

/**
 * @note:社会化新闻
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SocialFragment extends Fragment {
    private String TAG = "SocialFragment";

    List<ChannelBean.Channel> channelList;
    int requestType = -1;
    int curPage = 1;


    ViewPager pager;
    TabLayout tableLayout;
    Toolbar toolbar;
//    TabPageIndicator indicator;


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social,container,false);
        //获取组件
        //->ViewPage的Adapter
//        adapter = new TabPageIndicatorAdapter(getFragmentManager());
        pager = (ViewPager) view.findViewById(R.id.viewpage);
        //->Indivator
//        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        tableLayout = (TabLayout) view.findViewById(R.id.indicator);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.social));
        ((MainActivity)getContext()).setSupportActionBar(toolbar);
        ((MainActivity)getContext()).getSupportActionBar().setTitle(getString(R.string.news_social));
        ((MainActivity)getContext()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity)getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NewsFragmentAdapter adapter = new NewsFragmentAdapter(getChildFragmentManager());
        //获取数据
        channelList = DataSource.getDataSource().getChannelList();
//        tableLayout.setTabMode(TabLayout.MODE_FIXED);
        if(!channelList.isEmpty()){
            for(int i=0;i<channelList.size();i++){
//                ItemFragment newsFragment=new ItemFragment();
                ItemChannelFragment newsFragment = new ItemChannelFragment();
//                ItemChannelFragment newsFragment = ItemChannelFragment_.builder().build();
                Bundle ags=new Bundle(2);
                ags.putString("name",channelList.get(i).getName());
                ags.putString("channelId",channelList.get(i).getChannelId());
                newsFragment.setArguments(ags);
                adapter.addFragment(newsFragment, channelList.get(i).getName());
            }
        }
//        tableLayout.setOnClickListener();
//        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tableLayout);
//        pager.addOnPageChangeListener(listener);
        pager.setAdapter(adapter);
        tableLayout.setupWithViewPager(pager);
        tableLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tableLayout.setTabsFromPagerAdapter(adapter);
//        pager.setOffscreenPageLimit(channelList.size());//预加载
        if(tableLayout.getTabAt(0) == null){
            Log.e(TAG,"tabLayout0 is null");
        }
        else{
            Log.e(TAG,"tabLayout0 is not null");
        }
        if(tableLayout.getTabAt(0).getCustomView() == null){
            Log.e(TAG,"tabLayout.getCustomView() is null");
        }
        else{
            Log.e(TAG,"tabLayout.getCustomView() is not null");
        }
//        tableLayout.getTabAt(0).getCustomView().setSelected(true);

        //Adapter
//        pager.setAdapter(adapter);
//        indicator.setViewPager(pager);
//        tableLayout.setupWithViewPager(pager);

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    //选择Indicator中的具体页面
//    private void disposeOnPageSeleced(int position){
//        //请求网络新闻数据
//        requestType = VolleyUtil.REQUEST_NEWS;
//        VolleyUtil.getVolleyUtil().requestNews(getActivity(), UrlUtil.getNewAddress(channelList.get(position).getChannelId(),1),requestType,VolleyUtil.METHOD_GET);
//    }
//
//     Fragment fragment;
//    /**
//     * @note:ViewPager适配器*/
//    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
//        public TabPageIndicatorAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            //新建一个Fragment来展示ViewPager item的内容，并传递参数
//            Log.e("TESTITEM","getItem:"+position+ channelList.get(position).getName());
//            fragment = new ItemFragment();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("channelItem", channelList.get(position));
//            fragment.setArguments(bundle);
//            return fragment;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            String title = channelList.get(position % channelList.size()).getName();
//            return title.substring(0,title.length()-2);
//        }
//
//        @Override
//        public int getCount() {
//            return channelList.size();
//        }
//    }
}
