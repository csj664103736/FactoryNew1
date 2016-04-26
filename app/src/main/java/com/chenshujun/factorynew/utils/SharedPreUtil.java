package com.chenshujun.factorynew.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LiuJie on 2015/12/21.
 */
public class SharedPreUtil {
    public static final String SHAREDPREF_URI = "share_ip_port";
    private static SharedPreferences sharedPreUtil = null;
    //缓存key
    public static final String KEY_USERNAME = "key_username";
    public static final String KEY_PASSWORD = "key_password";

    public static final String KEY_NEWS_PERSONAL = "key_news_personal";
    public static final String KEY_NEWS_SOCIAL = "key_news_personal";


    //清除IP和PORT的所有缓存
    public static void removeAll(Context pContext){
        if(sharedPreUtil == null){
            sharedPreUtil = pContext.getSharedPreferences(SHAREDPREF_URI, 0);
        }
        //清除用户名密码
        sharedPreUtil.edit().remove(KEY_USERNAME).commit();
        sharedPreUtil.edit().remove(KEY_PASSWORD).commit();
    }

    public static void saveString(Context pContext,String key, String value){
        if(sharedPreUtil == null){
            sharedPreUtil = pContext.getSharedPreferences(SHAREDPREF_URI, 0);
        }
        boolean is = sharedPreUtil.edit().putString(key,value).commit();
    }
    public static void removeString(Context pContext,String key){
        if(sharedPreUtil == null){
            sharedPreUtil = pContext.getSharedPreferences(SHAREDPREF_URI, 0);
        }
        sharedPreUtil.edit().remove(key).commit();
    }
    public static String getString(Context pContext, String key){
        if(sharedPreUtil == null){
            sharedPreUtil = pContext.getSharedPreferences(SHAREDPREF_URI, 0);
        }
        return sharedPreUtil.getString(key,null);
    }

    private SharedPreUtil(){}
}
