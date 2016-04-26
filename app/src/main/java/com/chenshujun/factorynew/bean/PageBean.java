package com.chenshujun.factorynew.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 服务器返回新闻Bean
 */
public class PageBean implements Serializable{

    int allNum;
    int allPages;
    int currentPage;
    int maxResult;
    List<Content> contentlist;
    /*
        * @note:新闻内容url*/
    public class Content implements Serializable{
        String channelId;
        String channelName;
        String desc;
        String link;
        String nid;
        String pubDate;
        String source;
        String title;
        List<ImageUrl> imageurls;

        public String getChannelId() {
            return channelId;
        }

        public String getChannelName() {
            return channelName;
        }

        public String getDesc() {
            return desc;
        }

        public String getLink() {
            return link;
        }

        public String getNid() {
            return nid;
        }

        public String getPubDate() {
            return pubDate;
        }

        public String getSource() {
            return source;
        }

        public String getTitle() {
            return title;
        }

        public List<ImageUrl> getImageurls() {
            return imageurls;
        }

        /*
        * @note:图片url*/
        public class ImageUrl{
            int height;
            int width;
            String url;

            public int getHeight() {
                return height;
            }

            public int getWidth() {
                return width;
            }

            public String getUrl() {
                return url;
            }
        }
    }



    public int getAllNum() {
        return allNum;
    }

    public int getAllPages() {
        return allPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public List<Content> getContentlist() {
        return contentlist;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "allNum=" + allNum +
                ", allPages=" + allPages +
                ", currentPage=" + currentPage +
                ", maxResult=" + maxResult +
                ", contentlist=" + contentlist +
                '}';
    }
}
