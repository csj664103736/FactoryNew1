package com.chenshujun.factorynew.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chenshujun.factorynew.R;

import java.util.List;

/**
 * 作者：csj
 * 邮箱：￥￥￥￥
 */
public class PersonalNewsAdapter extends BaseAdapter {
    Context context;
    List itemList;

    public PersonalNewsAdapter(Context context, List itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_recycler_news_personal,null);
        return view;
    }
}
