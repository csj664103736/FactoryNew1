package com.chenshujun.factorynew.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @note:新闻频道
 */
public class ChannelBean implements Serializable{
    int totalNum;
    int ret_code;
    List<Channel> channelList;

    @Override
    public String toString() {
        return "ChannelBean{" +
                "totalNum=" + totalNum +
                ", ret_code=" + ret_code +
                ", channelList=" + channelList +
                '}';
    }

    public class Channel implements Serializable {
        String channelId;
        String name;

        public Channel(String channelId, String name) {
            this.channelId = channelId;
            this.name = name;
        }

        public String getChannelId() {
            return channelId;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Channel{" +
                    "channelId='" + channelId + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public int getTotalNum() {
        return totalNum;
    }

    public int getRet_code() {
        return ret_code;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }
}
