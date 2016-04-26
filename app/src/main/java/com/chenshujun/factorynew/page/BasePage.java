package com.chenshujun.factorynew.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @note:Page基础类
 */
public abstract class BasePage {
    protected Context context;
    protected View contentView;

    public BasePage(Context context) {
        this.context = context;
        //配置页面
        initConfig();
        //获取数据
        initData();
        //配置界面
        contentView = initView((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getContentView() {
        return contentView;
    }

    //界面、数据等其他配置
    protected void initConfig(){}

    //初始化数据
    protected void initData(){}

    protected abstract View initView(LayoutInflater inflater);
}
