package com.chenshujun.factorynew.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：csj
 * 邮箱：￥￥￥￥
 */
public class PersonalNewsBean implements Serializable{
    public int nid;
    //头像
    public String userImageUrl;
    //用户名
    public String userName;
    public String title;
    //新闻内容
    public String desc;
    //新闻发布时间
    public String pubDate;
    public List imageUrls;
    //是否点赞
    public boolean isZan;
    //是否社会化
    public boolean isSocialize;

    @Override
    public String toString() {
        return "PersonalNewsBean{" +
                "nid=" + nid +
                ", userImageUrl='" + userImageUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", imageUrls=" + imageUrls +
                '}';
    }

    public PersonalNewsBean(int nid, String userImageUrl, String userName, String title, String desc, String pubDate, List imageUrls, boolean isZan, boolean isSocialize) {
        this.nid = nid;
        this.userImageUrl = userImageUrl;
        this.userName = userName;
        this.title = title;
        this.desc = desc;
        this.pubDate = pubDate;
        this.imageUrls = imageUrls;
        this.isZan = isZan;
        this.isSocialize = isSocialize;
    }
}
