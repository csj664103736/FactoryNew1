package com.chenshujun.factorynew.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.bean.ZanBean;

import java.util.List;

/**
 * @note：新闻评论
 * @type:Adapter
 */
public class ListZanAdapter extends BaseAdapter{
    private List<ZanBean> itemList;
    Context context;

    public ListZanAdapter(List<ZanBean> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
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
        ViewHolder holder;
        //获取布局
        //如果contentView为null
        if(convertView == null){
            convertView = View.inflate(context,R.layout.item_list_zan,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        //填充数据
        Glide.with(context).load(itemList.get(position).imgUrl).into(holder.ivUser);
        holder.tvUser.setText(itemList.get(position).userName);
        return convertView;
    }

    static class ViewHolder{
        ImageView ivUser;
        TextView tvUser;

        public ViewHolder(View view) {
            ivUser = (ImageView) view.findViewById(R.id.iv_user);
            tvUser = (TextView) view.findViewById(R.id.tv_user);
        }
    }
}
