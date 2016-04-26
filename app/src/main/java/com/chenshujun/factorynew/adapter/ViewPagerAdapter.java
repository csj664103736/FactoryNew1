package com.chenshujun.factorynew.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.chenshujun.factorynew.page.FunctionItemPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenshujun on 2016/4/15.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<FunctionItemPage> itemList;
    private List<String> titleList;

    public ViewPagerAdapter(Context context, List itemList, List titleList) {
        this.itemList = itemList;
        this.titleList = titleList;
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(position>itemList.size()) return;
        ((ViewPager)container).removeView(itemList.get(position).getContentView());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int size = titleList.size();
        return titleList.get(position%size);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(itemList.get(position).getContentView());
        return itemList.get(position).getContentView();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
