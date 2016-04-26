package com.chenshujun.factorynew.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.fragment.BaseFragment;
import com.chenshujun.factorynew.tools.DataSource;
import com.chenshujun.factorynew.view.LoadingDialog;

/**
 * @note:Activity基本类
 */
public abstract class BaseActivity extends AppCompatActivity {

    LoadingDialog loadingDialog;
    BaseFragment baseFragment;

    TextView tvActionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取数据
        initData();
        //初始化界面
        initView();
        tvActionbar = (TextView) findViewById(R.id.tv_actionbar);
    }


    @Override
    protected void onDestroy() {
        destroy();
        super.onDestroy();
    }

    /*=============================功能性函数==================================*/
    /**
     * 显示loadingView*/
    protected void showLoadingView(){
//        if(!loadingView.isShow()){
//            loadingView.show();
//        }
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
    }
    /**
     * 修改Actionbar标题*/
//    protected void setActionbarText(String text){
//        if(tvActionbar == null) return;
//        tvActionbar.setText(text);
//    }

    protected abstract void initView();
    //初始化数据
    protected void initData(){}

    protected void destroy(){}
    /*============================键盘响应========================================*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(baseFragment != null){
            baseFragment.onKeyDown(keyCode,event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
