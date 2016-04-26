package com.chenshujun.factorynew.activity;

import android.graphics.Rect;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.adapter.ViewPagerAdapter;
import com.chenshujun.factorynew.bean.PersonalNewsBean;
import com.chenshujun.factorynew.page.FunctionItemPage;
import com.chenshujun.factorynew.tools.DataSource;
import com.viewpagerindicator.TabPageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @note:个人新闻详情页面
 * */
@EActivity(R.layout.activity_personalnews_detail)
public class PersonalNewsDetailActivity extends BaseActivity {

    ViewPagerAdapter adapter;
    private List<FunctionItemPage> pagesList;
    private List<String> titleList;
    private int wHeight = -1;
    PersonalNewsBean curBean;

    @ViewById(R.id.viewpage)
    ViewPager pager;
    @ViewById(R.id.indicator)
    TabPageIndicator indicator;
    @ViewById(R.id.tv_actionbar)
    TextView rlActionbar;
    @ViewById(R.id.ll_function)
    LinearLayout llFunction;
    @ViewById(R.id.ib_dispacher)
    ImageButton ibDispacher;
    @ViewById(R.id.ib_comment)
    ImageButton ibComment;
    @ViewById(R.id.ib_zan)
    ImageButton ibZan;
    @ViewById(R.id.ib_socialize)
    ImageButton ibSocialize;

    private String TAG = "PersonalNewsDetail";

    @Override
    protected void initView() {
        //获取数据
        titleList = DataSource.getDataSource().getCommentTitles(getApplicationContext());
        pagesList = DataSource.getDataSource().getPage(getApplicationContext());
        adapter = new ViewPagerAdapter(this,pagesList,titleList);
        WindowManager manager = this.getWindowManager();
        wHeight = manager.getDefaultDisplay().getHeight();
        Log.e(TAG,"wHeight:"+wHeight);
    }

    //获取数据
    @Override
    protected void initData() {
        curBean = (PersonalNewsBean) getIntent().getSerializableExtra("data");
    }

    //界面有关信息配置
    private void initConfig(){
        //->设置pager高度
        //获取pager参数
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pager.getLayoutParams();

        //获取标题栏高度
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        //actionBar高度
        RelativeLayout.LayoutParams actionbarParam = (RelativeLayout.LayoutParams) rlActionbar.getLayoutParams();
        //indicator高度
        RelativeLayout.LayoutParams incatorParam = (RelativeLayout.LayoutParams) indicator.getLayoutParams();
        //操作区域高度
        RelativeLayout.LayoutParams functionParam = (RelativeLayout.LayoutParams) llFunction.getLayoutParams();

//        Log.e(TAG,"wActionbarHeight:status_bar_height"+result);
//        Log.e(TAG,"wActionbarHeight:incatorParam"+incatorParam.height);
//        Log.e(TAG,"wActionbarHeight:actionbarParam"+actionbarParam.height);
//        Log.e(TAG,"wActionbarHeight:functionParam"+functionParam.height);
        params.height = wHeight - actionbarParam.height - incatorParam.height - functionParam.height - result;
        //设置pager高度
        pager.setLayoutParams(params);
        //->功能按钮样式
        //赞
        changeIbZanStyle();
        //社会化
        changeIbSocializeStyle();
    }

    @AfterViews
    void afterViews(){
        //界面配置
        initConfig();
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagesList.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //赞
    @Click(R.id.ib_zan)
    void onZanClick(){
        curBean.isZan = !curBean.isZan;
        //修改Zan样式
        changeIbZanStyle();
        onDataChangeListener.notifyDataListChange(curBean);
    }
    //社会化
    @Click(R.id.ib_socialize)
    void onSocializeClick(){
        curBean.isSocialize = !curBean.isSocialize;
        //修改Zan样式
        changeIbSocializeStyle();
        onDataChangeListener.notifyDataListChange(curBean);
    }
    //转发
    @Click(R.id.ib_dispacher)
    void onDispacherClick(){}
    //评论
    @Click(R.id.ib_comment)
    void onCommentClick(){}

    //更改Zan的样式
    private void changeIbZanStyle(){
        if(curBean.isZan){
            ibZan.setImageResource(R.drawable.zan_press);
        }
        else{
            ibZan.setImageResource(R.drawable.zan);
        }
    }

    //更改Socialize样式
    private void changeIbSocializeStyle(){
        if(curBean.isSocialize){
            ibSocialize.setImageResource(R.drawable.social_press);
        }
        else {
            ibSocialize.setImageResource(R.drawable.social);
        }}

    /*======================================回调接口=============================================*/

    private static OnDataChangeListener onDataChangeListener;
    public interface OnDataChangeListener{
        void notifyDataListChange(PersonalNewsBean personalNewsBean);
    }

    public static void setOnDataChangeListener(OnDataChangeListener listener) {
        onDataChangeListener = listener;
    }
}
