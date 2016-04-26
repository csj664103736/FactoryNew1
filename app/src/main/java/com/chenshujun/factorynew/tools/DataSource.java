package com.chenshujun.factorynew.tools;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.bean.ChannelBean;
import com.chenshujun.factorynew.bean.PageBean;
import com.chenshujun.factorynew.bean.PersonalNewsBean;
import com.chenshujun.factorynew.page.FunctionItemPage;
import com.chenshujun.factorynew.utils.GsonUtil;
import com.chenshujun.factorynew.utils.SharedPreUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenshujun on 2016/3/18.
 */
public class DataSource {
    private String TAG = "DataSource";

    /*-------------------------------获取个人新闻-------------------------------------------*/
    //获取个人新闻页面评论区indicator的内容--赞、社会化、评论
    public List<String> getCommentTitles(Context context){
        List titles = new ArrayList();
        titles.add(context.getResources().getString(R.string.zan));
//        titles.add(context.getResources().getString(R.string.socialize));
        titles.add(context.getResources().getString(R.string.comment));
        return titles;
    }

    public List<FunctionItemPage> getPage(Context context){
        List page = new ArrayList();
        for(int i =0; i < 2; i++){
            page.add(new FunctionItemPage(context, i));
        }
        return page;
    }

    List<PersonalNewsBean> personalNewsList;
    public List<PersonalNewsBean> getMorePersonalNews(Context context){
        List<PersonalNewsBean> moreList = null;
        List imgList = new ArrayList();
        imgList.add("http://img1.imgtn.bdimg.com/it/u=2359670474,249090167&fm=21&gp=0.jpg");
        imgList.add("http://imgsrc.baidu.com/forum/w=580/sign=170de9952bf5e0feee1889096c6134e5/64de7bf40ad162d915c1849017dfa9ec8b13cd62.jpg");
        imgList.add("http://beauty.ilife.cn/images/upload/info/beauty/other_images/8_201216194133_1.jpg");
        if(moreList == null){
            moreList = new ArrayList();
        }
        //取缓存内容
        String strNews = SharedPreUtil.getString(context,SharedPreUtil.KEY_NEWS_PERSONAL);
        //如果有缓存内容
        if(!TextUtils.isEmpty(strNews)){
            moreList = GsonUtil.changeGsonToList(strNews, new TypeToken<List<PersonalNewsBean>>(){}.getType());
        }
        for(int i = 0; i < 3; i++){
            boolean flag = false;
            if(i%2 == 0){
                flag = true;
            }
            PersonalNewsBean personalNewsBean = new PersonalNewsBean(i,"http://img2.imgtn.bdimg.com/it/u=1751737817,3078213403&fm=21&gp=0.jpg",
                    "new"+i,""+i+"男朋友觉得你的化妆品多少钱？",
                    i+"也来个《男朋友觉得你的化妆品值多少钱》系列。不问不知道，一问吓一跳也来个《男朋友觉得你的化妆品值多少钱》系列。不问不知道，一问吓一跳也来个《男朋友觉得你的化妆品值多少钱》系列。不问不知道，一问吓一跳",
                    "2015-01_01",imgList,flag,!flag);
            moreList.add(personalNewsBean);
        }
        //从本地获取
        return moreList;
    }
    public List<PersonalNewsBean> getPersonalNews(Context context){
        List imgList = new ArrayList();
        imgList.add("http://img1.imgtn.bdimg.com/it/u=2359670474,249090167&fm=21&gp=0.jpg");
        imgList.add("http://imgsrc.baidu.com/forum/w=580/sign=170de9952bf5e0feee1889096c6134e5/64de7bf40ad162d915c1849017dfa9ec8b13cd62.jpg");
        imgList.add("http://beauty.ilife.cn/images/upload/info/beauty/other_images/8_201216194133_1.jpg");
        if(personalNewsList == null){
            personalNewsList = new ArrayList();
        }
        //取缓存内容
        String strNews = SharedPreUtil.getString(context,SharedPreUtil.KEY_NEWS_PERSONAL);
        //如果有缓存内容
        if(!TextUtils.isEmpty(strNews)){
            personalNewsList = GsonUtil.changeGsonToList(strNews, new TypeToken<List<PersonalNewsBean>>(){}.getType());
        }
        for(int i = 0; i < 10; i++){
            boolean flag = false;
            if(i%2 == 0){
                flag = true;
            }
            PersonalNewsBean personalNewsBean = new PersonalNewsBean(i,"http://img2.imgtn.bdimg.com/it/u=1751737817,3078213403&fm=21&gp=0.jpg",
                    "chenshujun"+i,""+i+"男朋友觉得你的化妆品多少钱？",
                    i+"也来个《男朋友觉得你的化妆品值多少钱》系列。不问不知道，一问吓一跳也来个《男朋友觉得你的化妆品值多少钱》系列。不问不知道，一问吓一跳也来个《男朋友觉得你的化妆品值多少钱》系列。不问不知道，一问吓一跳",
                    "2015-01_01",imgList,flag,!flag);
            personalNewsList.add(personalNewsBean);
        }
        //从本地获取
        return personalNewsList;
    }

    /*-------------------------------社会新闻-------------------------------------------*/
    //处理服务器返回的新闻
    List<PageBean.Content> contentList;
    public void setSocialNews(JSONObject resultJson){
        Log.e("DATASOURCESET","setSocialNews:"+resultJson.toString());
//        curPage = 1;
        if (topNews == null){
            topNews = new ArrayList<>();
        }
        if(showingList == null){
            showingList = new ArrayList<>();
        }
        //清空原来的数据
        topNews.removeAll(topNews);
        showingList.removeAll(showingList);
        PageBean tmpPage;
        try {
            JSONObject bodyJson = resultJson.getJSONObject("showapi_res_body");
            JSONObject pageJson = bodyJson.getJSONObject("pagebean");
            tmpPage = GsonUtil.changeGsonToBean(pageJson.toString(), PageBean.class);
            contentList = tmpPage.getContentlist();
            Log.e(TAG,"content size:"+contentList.size());
            //获取图片新闻集合
//            getTopNews();
            //添加列表新闻第一页,一定要在图片新闻之后，否则新闻会重复
//            loadMoreNews();
            //提醒数据显示界面，数据已经准备好
            dataSourceFinishListener.onDataSourceFinish(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //获取图片新闻集合
    public static int TOPSIZE = 3;
    List<PageBean.Content> topNews = new ArrayList<>();
    public List<PageBean.Content> getTopNews(){
        //处理数据
        List<PageBean.Content.ImageUrl> tmpImg;
        PageBean.Content tmpContent;
        //遍历所有新闻内容
        for(int i =0; i < contentList.size(); i++){
//            Log.e(TAG,"contentList:"+contentList.get(i).getTitle());
            tmpContent = contentList.get(i);
            //获取imageurl字段内容，如果有图片，就加入到titleList和urlList中
            tmpImg = tmpContent.getImageurls();
            if(tmpImg!= null && tmpImg.size()>0){
                topNews.add(tmpContent);
                contentList.remove(i);
            }
            //当达到展示图片的数量后，跳出循环
            if(topNews.size() >= TOPSIZE){
                break;
            }
        }
        Log.e(TAG,"topNews size:"+ topNews.size());
//        for(int i = 0; i < topNews.size(); i++){
//            Log.e(TAG,"topNews:"+ topNews.get(i).toString());
//        }

//        //当有图片新闻时，加载图片新闻显示区域
//        if(urlList.size() > 0){
//            Log.e(TAG,"rlImgNews visibility show");
//            //图片新闻可见
//            rlImgNews.setVisibility(View.VISIBLE);
//            // top新闻的图片地址
//            rpImgNews.setUriList(urlList);
//            rpImgNews.setTitle(topNewsTitle, titleList);
//            //开启线程  并实例化
//            rpImgNews.startRoll();
//            mViewPagerLay.removeAllViews();
//            //添加View
//            mViewPagerLay.addView(rpImgNews);
//        }
//        else {
//            Log.e(TAG,"rlImgNews visibility gone");
//            // 图片新闻不可见
//            rlImgNews.setVisibility(View.GONE);
//        }
        return topNews;
    }
    //获取list新闻集合/加载更多新闻
//    int curPage = 1;
    int REGION = 7;
    List<PageBean.Content> showingList;
    public List<PageBean.Content> loadMoreNews( int curPage){
//        if(showingList == null) showingList = new ArrayList<>();
        Log.e(TAG,"currentPage:"+curPage);
        //添加显示新闻项
        for(int i =(curPage-1)*REGION; i < curPage*REGION && i<contentList.size(); i++){
            showingList.add(contentList.get(i));
        }
        curPage++;

        Log.e(TAG, "showingList:" + showingList.size());
//        for(int i =0; i < showingList.size(); i++){
//            Log.e(TAG, "showingList:" + showingList.get(i).getTitle());
//        }
        return showingList;
    }


    //获取新闻频道
    List<ChannelBean.Channel> channelList;
    private static final String[] TITLE = new String[] { "国内", "财经", "娱乐", "科技",
            "体育", "社会", "军事", "互联网" };
    public void setChannelList(JSONObject resultJson){
        Log.e(TAG,"setChannelList");
        if(channelList==null){
            channelList = new ArrayList<>();
        }
        channelList.removeAll(channelList);
        try {
            JSONObject bodyJson = resultJson.getJSONObject("showapi_res_body");
            ChannelBean tmpChanel = GsonUtil.changeGsonToBean(bodyJson.toString(), ChannelBean.class);
            List<ChannelBean.Channel> tmpList = tmpChanel.getChannelList();
            for(int i = 0; i < TITLE.length; i++){
                for(int j = 0; j < tmpList.size(); j++){
                    //如果是Title中有的
                    if(tmpList.get(j).getName().startsWith(TITLE[i])){
                        channelList.add(tmpList.get(j));
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(dataSourceFinishListener!=null){
            dataSourceFinishListener.onDataSourceFinish(channelList);
        }
    }
    public List<ChannelBean.Channel> getChannelList(){
        if (channelList == null){
            channelList = new ArrayList<>();
        }
        return channelList;
    }

    /*===============================DataSource类构造函数==========================================*/
    private DataSource(){}
    static DataSource dataSource;
    public static DataSource getDataSource(){
        if(dataSource == null){
            dataSource = new DataSource();
        }
        return dataSource;
    }

    /*销毁DataSource对象*/
    public static void destroy(){
        dataSource = null;
    }
    /*===============================回调接口================================================*/
    private DataSourceFinishListener dataSourceFinishListener;
    public interface DataSourceFinishListener{
        void onDataSourceFinish(Object result);
    }
    public void setOnDataSourceFinashListener(DataSourceFinishListener dataSourceFinashListener){
        this.dataSourceFinishListener = dataSourceFinashListener;
    }

}
