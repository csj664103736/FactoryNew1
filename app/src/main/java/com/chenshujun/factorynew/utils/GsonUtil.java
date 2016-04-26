package com.chenshujun.factorynew.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @note:Json与Bean转化工具类
 */
public class GsonUtil {

    //String -> Bean
    public static <T> T changeGsonToBean(String gsonString, Class<T> tClass){
        Gson gson = new Gson();
        T t = gson.fromJson(gsonString,tClass);
        return t;
    }

    //String -> List
    public static List changeGsonToList(String gsonString,Type type) {
        Gson gson = new Gson();
        List list = gson.fromJson(gsonString, type);
        return list;
    }

    //String -> Array
    public static <T> T[] changeGsonToArray(String gsonString,Type type) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(gsonString,type);
        return array;
    }

    //List -> String
    public static String getProdListGsonString(List localProdList){
        Gson gson = new Gson();
        String localProcRootJson = gson.toJson(localProdList);
        return localProcRootJson;
    }

    private GsonUtil(){}
}
