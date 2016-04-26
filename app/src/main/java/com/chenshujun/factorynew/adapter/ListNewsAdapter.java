package com.chenshujun.factorynew.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.bean.PageBean;
import com.chenshujun.factorynew.tools.VolleyUtil;

import java.util.List;

/**
 * @note:新闻列表Adapter
 */
public class ListNewsAdapter extends BaseAdapter {
    private String TAG="NewsListAdapter";
    List<PageBean.Content> itemList;
    Context context;

    public ListNewsAdapter(Context context, List itemList) {
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
    public int getViewTypeCount() {
        return 2;
    }

    private final int TYPE_TEXT = 0;
    private final int TYPE_IMG = 1;

    @Override
    public int getItemViewType(int position) {
        //如果有图片url
        if(itemList.get(position).getImageurls()!= null && itemList.get(position).getImageurls().size() > 0){
            return TYPE_IMG;
        }
        return TYPE_TEXT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取View类型
        int viewType = getItemViewType(position);
        ImgViewHolder imgViewHolder = null;
        TextViewHolder textViewHolder = null;

        //如果convertView为null || convertView的类型和当前View的类型不一致
        if(convertView == null || ((Integer)convertView.getTag((R.id.lv))!=viewType)){
            switch (viewType){
                //文本类型
                case TYPE_TEXT:
                    convertView = View.inflate(context,R.layout.item_list_news_noimg, null);
                    textViewHolder = new TextViewHolder();
                    //获取组件
                    textViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
                    textViewHolder.tvContent = (TextView) convertView.findViewById(R.id.content);
                    convertView.setTag(R.id.lv_view,textViewHolder);
                    break;
                //图片类型
                case TYPE_IMG:
                    convertView = View.inflate(context, R.layout.item_list_news, null);
                    imgViewHolder = new ImgViewHolder();
                    //获取组件
                    imgViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
                    imgViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
                    imgViewHolder.tvContent = (TextView) convertView.findViewById(R.id.content);
                    convertView.setTag(R.id.lv_view,imgViewHolder);
                    break;
            }
            convertView.setTag(R.id.lv,viewType);
        }
        //convertview已经创建
        else{
            switch (viewType){
                case TYPE_TEXT:
                    textViewHolder = (TextViewHolder) convertView.getTag(R.id.lv_view);
                    break;
                case TYPE_IMG:
                    imgViewHolder = (ImgViewHolder) convertView.getTag(R.id.lv_view);
                    break;
            }
        }

        //填充数据
        switch (viewType){
            case TYPE_TEXT:
                textViewHolder.tvTitle.setText(itemList.get(position).getTitle());
                textViewHolder.tvContent.setText(itemList.get(position).getDesc().trim());
                break;
            case TYPE_IMG:
                String url = itemList.get(position).getImageurls().get(0).getUrl();
                VolleyUtil.getVolleyUtil().loadImage(context, imgViewHolder.imageView, url);
                imgViewHolder.tvTitle.setText(itemList.get(position).getTitle());
                imgViewHolder.tvContent.setText(itemList.get(position).getDesc().trim());
                break;
        }
        return convertView;
    }
    static class ImgViewHolder{
        ImageView imageView;
        TextView tvTitle;
        TextView tvContent;
    }
    static class TextViewHolder{
        TextView tvTitle;
        TextView tvContent;
    }
}
