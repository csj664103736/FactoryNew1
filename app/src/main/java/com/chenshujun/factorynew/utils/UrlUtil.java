package com.chenshujun.factorynew.utils;

import android.text.format.Time;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Uri
 */
public class UrlUtil {
    public static final String APPID = "16558";
    public static final String APPSECRET = "2c40181dc97b478db37c6999ce4381bb";

    public static final String IP = "http://172.19.38.1:8080/spring-mvc-showcase";

    //登录
//    public static final String URI_LOGIN = IP+"/LoginController/saveFactoryUser1";
    public static final String URI_LOGIN = IP+"/login/login";

    //新闻频道
    public static String getNewChannel() {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        String address = "http://route.showapi.com/109-34?" +
                "showapi_appid=" + APPID +
                "&showapi_timestamp=" + localTime.format("%Y%m%d%H%M%S") +
                "&showapi_sign=" + APPSECRET;
        return address;
    }

    //新闻接口URL
    public static String getNewAddress(String channelId, int page) {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        String address = "http://route.showapi.com/109-35?" +
                "showapi_appid=" + APPID +
                "&showapi_timestamp=" + localTime.format("%Y%m%d%H%M%S") +
                "&channelId=" + channelId +
                "&channelName=" +
                "&title=" +
                "&page=" + page +
                "&needContent=" +
                "&needHtml=" +
                "&showapi_sign=" + APPSECRET;
        return address;
    }
}
