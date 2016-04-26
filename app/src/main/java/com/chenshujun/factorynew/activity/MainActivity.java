package com.chenshujun.factorynew.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.fragment.BaseFragment;
import com.chenshujun.factorynew.fragment.LoginFragment;
import com.chenshujun.factorynew.fragment.LoginFragment_;
import com.chenshujun.factorynew.fragment.PersonalNewsFragment;
import com.chenshujun.factorynew.fragment.PersonalNewsFragment_;
import com.chenshujun.factorynew.fragment.SocialFragment;
import com.chenshujun.factorynew.tools.DataSource;
import com.chenshujun.factorynew.utils.SharedPreUtil;
import com.chenshujun.factorynew.view.LoadingDialog;
import com.chenshujun.factorynew.view.LoadingView;

import java.util.zip.Inflater;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, DataSource.DataSourceFinishListener {

    private String TAG="MainActivity";

    RadioButton rbLeft,rbRight;
    int requestType = -1;
    public LoadingView loadingView;
    LoadingDialog loadingDialog;
    //是否已有本地登陆者
    boolean isLogin = false;
    //切换的三个Fragment
    SocialFragment socialFragment;
    LoginFragment loginFragment;
    PersonalNewsFragment personalNewsFragment;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);

        //获取组件
        rbLeft = (RadioButton) findViewById(R.id.rb_left);
        rbRight = (RadioButton) findViewById(R.id.rb_right);
        loadingView = new LoadingView(this);
        loadingDialog = new LoadingDialog(this);

        //接口回调
        DataSource.getDataSource().setOnDataSourceFinashListener(this);

//        setActionbarText(getString(R.string.news_social));
        //获取数据
        if(DataSource.getDataSource().getChannelList().size() > 0){
            //如果频道List不为空，则显示SocialFragment
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new SocialFragment()).commit();
//            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new Main2Activity()).commit();
        }
        else{
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
        }
        //判断是否有用户已经登录
        isUserLogin();
        //监听事件
        rbLeft.setOnCheckedChangeListener(this);
        rbRight.setOnCheckedChangeListener(this);
        //界面配置
//        ((Button)findViewById(R.id.btn_actionbar_withback)).setVisibility(View.GONE);
    }

    //标记是否有用户登录
    private boolean isUserLogin(){
        //获取本地用户名，密码不保存，登录成功的保存用户名
        String strUserName = SharedPreUtil.getString(getApplicationContext(),SharedPreUtil.KEY_USERNAME);
        if(!TextUtils.isEmpty(strUserName)){
            isLogin = true;
        }
        return isLogin;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked){
            return;
        }
        switch (buttonView.getId()){
            case R.id.rb_left:
//                Log.e(TAG,"left");
//                setActionbarText(getString(R.string.news_social));
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new SocialFragment()).commitAllowingStateLoss();
                break;
            case R.id.rb_right:
//                Log.e(TAG, "right");
                BaseFragment fragment;
                //如果已经登录
                if(!isLogin){
                    //如果personlNewsFragment不存在
                    if(personalNewsFragment == null){
//                    personalNewsFragment = new PersonalNewsFragment();
                        personalNewsFragment = PersonalNewsFragment_.builder().build();
                    }
                    fragment = personalNewsFragment;
                }
                //尚未登录
                else{
                    if(loginFragment == null){
//                        loginFragment = new LoginFragment();
                        loginFragment = LoginFragment_.builder().build();
                    }
                    fragment = loginFragment;
                }
//                setActionbarText(getString(R.string.news_personal));
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment).commitAllowingStateLoss();
                break;
        }
    }

    /*==================================共用方法==========================================*/



    private void disposeDatasourceSuccess(){
        //加载完频道后，才可跳转社会新闻版面
        Log.e(TAG, "getSupportFragmentManager");
        socialFragment = new SocialFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container_fragment,socialFragment).commitAllowingStateLoss();
    }

    @Override
    protected void destroy() {
        super.destroy();
        Log.e("MainActivity","onDestroy");
        DataSource.destroy();
    }

    @Override
    public void onDataSourceFinish(Object result) {
        disposeDatasourceSuccess();
        loadingDialog.dismiss();
    }

}

