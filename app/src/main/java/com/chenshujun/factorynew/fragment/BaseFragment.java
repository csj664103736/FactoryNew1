package com.chenshujun.factorynew.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.tools.DataSource;
import com.chenshujun.factorynew.tools.VolleyUtil;
import com.chenshujun.factorynew.view.LoadingView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 作者：csj
 * 邮箱：￥￥￥￥
 */
public abstract class BaseFragment extends Fragment implements VolleyUtil.VolleyFinishListener, DataSource.DataSourceFinishListener {
    private String TAG = "BaseFragment";
    protected Context context;

    protected LoadingView loadingView;
    protected SlidingMenu smRight;

    protected int requestType = -1;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        context = getActivity();
//        loadingView = new LoadingView(getActivity());
//        //接口回调
//        VolleyUtil.getVolleyUtil().setOnVolleyFinishListener(this);
//
//        //获取组件
//        View view = initView(inflater);
//        return view;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        context = getActivity();
        loadingView = new LoadingView(getActivity());
        //接口回调
        VolleyUtil.getVolleyUtil().setOnVolleyFinishListener(this);
        DataSource.getDataSource().setOnDataSourceFinashListener(this);
        //加载数据
        initData();
        //加载页面
        initView();
    }

    @Override
    public void onDestroy() {
        //子类销毁
        destroy();
        super.onDestroy();
    }

    //侧滑菜单
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void configSlidingMenu(){
        //如果侧滑菜单没有创建，则创建侧滑菜单
        if(smRight == null){
            smRight = new SlidingMenu(context);
            //BUG：sliding has already attach to window
            smRight.setMode(SlidingMenu.RIGHT);
            smRight.setBehindOffsetRes(R.dimen.sliding_menu_offset);
            smRight.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            smRight.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
            smRight.setMenu(R.layout.layout_menu);
            return;
        }
        smRight.setSlidingEnabled(true);
    }

    //侧滑菜单不可用
    protected void disableSliding(){
        if(smRight!=null){
            smRight.setSlidingEnabled(false);
        }
    }

    //显示进度条
    protected void showLoadingView(){
        if(loadingView.isShow()){
            loadingView.show();
        }
    }

    //隐藏进度条
    protected void dismissLoadingView(){
        if(loadingView!=null && loadingView.isShow()){
            loadingView.dismiss();
        }
    }

    //Volley响应接口
    @Override
    public void onVolleyFinish(int isSuccess, Object result) {
        requestType = -1;
        switch (isSuccess){
            //成功
            case VolleyUtil.SUCCESS_SUCCESS:
                disposeVolleySuccess(result);
                //进度条消失
                dismissLoadingView();
                break;
            //失败
            case VolleyUtil.FAILED_FAILED:
                //进度条消失
                dismissLoadingView();
                disposeVolleyFailed(result);
                break;
        }
    }

    //回调接口
    @Override
    public void onDataSourceFinish(Object result) {
        disposeDataSource(result);
    }

    /*===============================可覆盖方法================================================*/
    //Volley响应成功
    protected void disposeVolleySuccess(Object result){}
    //Volley响应成功
    protected void disposeVolleyFailed(Object result){}
    //DataSource响应函数
    protected void disposeDataSource(Object result){}
    //子类初始化View方法
    protected void initView(){
        Log.e(TAG,"initView");
    }
    //加载数据
    protected void initData(){
        Log.e(TAG,"initData");
    }
    //键盘回退方法
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(loadingView!=null && loadingView.isShow()){
                loadingView.dismiss();
                return true;
            }

        }
        return false;
    }
    /*===============================抽象方法==================================================*/
    //Fragment onDestroy销毁方法
    protected abstract void destroy();

}
