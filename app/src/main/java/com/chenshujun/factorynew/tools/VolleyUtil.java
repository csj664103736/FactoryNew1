package com.chenshujun.factorynew.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chenshujun.factorynew.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * VolleyUtil 网络请求
 */
public class VolleyUtil {
    //请求方式
    public static final int METHOD_POST = 1;
    public static final int METHOD_GET = 2;

    private static int requestType = -1;
    static String cookies;
    String TAGURL = "VOLLEYURL:";

    public static final int REQUEST_NEWS = 0;
    public static final int REQUEST_NEWS_CHANAL = 1;
    public static final int REQUEST_LOGIN = 10;
    /*===========================外部请求函数==================================*/
    //登录
    public void reuqestLogin(Context pContext, int pMethod, String pUrl,int pRequestType, String userName, String passWord){
        context = pContext;
        requestType = pRequestType;
        requestQueue = Volley.newRequestQueue(pContext);
        pUrl = pUrl+"/"+userName+"/"+passWord;
        Log.e(TAGURL,pUrl);
        if(pMethod == VolleyUtil.METHOD_GET){
            stringRequest = new StringRequest(Request.Method.GET,pUrl,stringListener,errorListener);
        }
        else{}
        requestQueue.add(stringRequest);
    }
    //登录
    public void reuqestLogin1(Context pContext, int pMethod, String pUrl,int pRequestType, final JSONObject jsonParam){
        context = pContext;
        requestType = pRequestType;
        requestQueue = Volley.newRequestQueue(pContext);
        Log.e(TAGURL,pUrl);
        Log.e("JsonParam",jsonParam.toString());
//        if(pMethod == VolleyUtil.METHOD_GET){}
//        else{
//            jsonRequest = new JsonObjectRequest(Request.Method.POST,pUrl,jsonParam,jsonListener,errorListener);
            stringRequest = new StringRequest(Request.Method.POST,pUrl,stringListener,errorListener){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map map = new HashMap();
                    map.put("data",jsonParam.toString());
                    return map;
                }
            };
//        }
        requestQueue.add(stringRequest);
    }
    //请求新闻频道
    public void requestNewsChanal(Context pContext, int pMethod, String pUrl, int pRequestType){
        context = pContext;
        requestType = pRequestType;
        requestQueue = Volley.newRequestQueue(context);
        Log.e(TAGURL,pUrl);
        jsonRequest = new JsonObjectRequest(Request.Method.GET,pUrl,null,jsonListener,errorListener);
        requestQueue.add(jsonRequest);
    }

    //请求某一频道的新闻
    public void requestNews(Context pContext, String pUrl, int pRequestType, int pMethod){
        context = pContext;
        requestType = pRequestType;
        requestQueue = Volley.newRequestQueue(pContext);
        Log.e(TAGURL,pUrl);
        jsonRequest = new JsonObjectRequest(Request.Method.GET,pUrl,null,jsonListener,errorListener);
        requestQueue.add(jsonRequest);
    }

    /*=======================================Volley监听类Listener==============================================*/
    private String TAGSTRING = "VOLLEYSTRING";
    /**
     * @注释：Volley响应成功Listener_String格式
     */
    private class StringResponseListener implements Response.Listener<String> {
        @Override
        public void onResponse(String s) {
            Log.e(TAGSTRING,"响应成功："+s);
            switch (requestType){
                case REQUEST_LOGIN:
                    volleyFinishListener.onVolleyFinish(SUCCESS_SUCCESS,s);
                    break;
            }
            requestType = -1;
        }
    }

    private String TAGJSON = "VOLLEYJSON";
    /**
     * @注释：Volley响应成功Listener_JSON格式
     */
    private class JsonResponseListener implements com.android.volley.Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject resultJson) {
            Log.e(TAGJSON,"响应成功："+resultJson.toString());
            switch (requestType){
                //请求新闻频道
                case REQUEST_NEWS_CHANAL:
                    DataSource.getDataSource().setChannelList(resultJson);
                    break;
                //请求新闻
                case REQUEST_NEWS:
//                    volleyFinishListener.onVolleyFinish(SUCCESS_SUCCESS,resultJson);
                    DataSource.getDataSource().setSocialNews(resultJson);
                    break;
            }
            requestType = -1;
        }
    }

    /**
     * @注释:Volley响应失败Listener
     */
    private class ResponseErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("VolleyErrorListener", "" + volleyError.getMessage() + ":" + volleyError.toString());
            //当连接不上服务器时
            String errorMsg = "连接服务器失败";
            errorMsg = disposeDefaultError(volleyError);
            if(volleyFinishListener!= null){
                volleyFinishListener.onVolleyFinish(FAILED_FAILED,errorMsg);
            }
            requestType = -1;
        }
    }


    /**
     * @note:默认错误处理
     */
    private String disposeDefaultError(VolleyError volleyError){
        String errorMsg = "连接服务器失败";
        //服务器返回的错误信息
        if(volleyError.networkResponse != null){
            byte[] htmlBodyBytes = volleyError.networkResponse.data;
            String errorInfo = new String(htmlBodyBytes);
            Log.e("VolleyErrorListener","Server:"+ errorInfo);
            //服务器传递的错误信息
            try {
                JSONObject errorJson = new JSONObject(errorInfo);
                errorMsg = errorJson.getString("exceptionInfo");
            } catch (JSONException e) {
                errorMsg = errorInfo;
            }
        }
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        return errorMsg;
    }

    /*=====================Volley具体操作==============================================*/



    /*=====================公用方法==============================================*/
    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @return true 表示网络可用
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
                else{
                    volleyFinishListener.onVolleyFinish(FAILED_FAILED,"网络连接不可用");
                    return false;
                }
            }
        }
        return false;
    }

    /*=====================Volley回调接口==========================================*/

    //连接服务器结果
    public static final int SUCCESS_SUCCESS = 11;
    public static final int FAILED_FAILED = 13;
    VolleyFinishListener volleyFinishListener;
    /**
     * @note:Volley完成接口*/
    public interface VolleyFinishListener{
        void onVolleyFinish(int isSuccess, Object result);
    }
    public void setOnVolleyFinishListener(VolleyFinishListener volleyFinishListener){
        this.volleyFinishListener =volleyFinishListener;
    }

    /*=====================Volley配置函数==============================================*/
    private static Context context;
    private static VolleyUtil volleyUtil = null;
    private static RequestQueue requestQueue = null;
    private static JsonObjectRequest jsonRequest;
    private static JsonResponseListener jsonListener;
    private static ResponseErrorListener errorListener;
    private static StringRequest stringRequest;
    private static StringResponseListener stringListener;
//    private static ImageLoader imageLoader;
    private static ImageLoader.ImageListener imageListener;
    private VolleyUtil() {
        //创建请求队列
        jsonListener = new JsonResponseListener();
        errorListener = new ResponseErrorListener();
        stringListener = new StringResponseListener();
    }
//    ImageLoader.ImageListener listener;
    public void loadImage(Context context,ImageView imageView,String url){
        requestQueue = Volley.newRequestQueue(context);
        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {

            }
        });
        Log.e(TAG_IMG,"url:"+url);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,R.drawable.load_default, R.drawable.load_error);
        if(!TextUtils.isEmpty(url)){
            Log.e(TAG_IMG,"url not null");
            if(imageLoader == null){
                Log.e(TAG_IMG,"imageLoder is null");
            }
            else{
                Log.e(TAG_IMG,"imageLoder not null");
            }
            if(listener == null){
                Log.e(TAG_IMG,"listener is null");
            }
            else{
                Log.e(TAG_IMG,"listener not null");
            }
            imageLoader.get(url,listener);
        }
    }

    private String TAG_IMG = "VolleyImage";

    public static VolleyUtil getVolleyUtil() {
        if (volleyUtil == null) {
            volleyUtil = new VolleyUtil();
        }
        return  volleyUtil;
    }

    public static void distoryVolley(){
        volleyUtil = null;
    }
}
