package com.chenshujun.factorynew.fragment;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.utils.CommonUtil;
import com.chenshujun.factorynew.utils.SharedPreUtil;
import com.chenshujun.factorynew.utils.UrlUtil;
import com.chenshujun.factorynew.tools.VolleyUtil;
import com.chenshujun.factorynew.view.ClearableEditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;


/**
  * @note:登录页面
  */
@EFragment(R.layout.fragment_login)
public class LoginFragment extends BaseFragment{
    private final String TAG = "LoginFragment";
    @ViewById(R.id.tv_notice)
    TextView tvNotice;
    @ViewById(R.id.et_username)
    ClearableEditText etUsername;
    @ViewById(R.id.et_password)
    ClearableEditText etPassword;
    @ViewById(R.id.btn_submit)
    Button btnSubmit;

    @Override
    protected void destroy() {}

    String strUserName;
    //登录按钮点击事件
    @Click(R.id.btn_submit)
    void disposeBtnLoginOnclick(){
        strUserName = etUsername.getText().toString().trim();
        //如果用户名为空
        if(TextUtils.isEmpty(strUserName)){
            etUsername.setWarnIconVisible();
            return;
        }
        //如果密码为空
        String strPassword = etPassword.getText().toString().trim();
        if(TextUtils.isEmpty(strPassword)){
            etPassword.setWarnIconVisible();
            return;
        }
        //不为空，登录
        JSONObject paramJson = new JSONObject();
        try {
            paramJson.put("username",strUserName);
            paramJson.put("password",strPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showLoadingView();
        requestType = VolleyUtil.REQUEST_LOGIN;
        VolleyUtil.getVolleyUtil().reuqestLogin(getActivity(), VolleyUtil.METHOD_GET, UrlUtil.URI_LOGIN, requestType, strUserName,strPassword);
    }

    /*=================================Volley响应结束================================================*/

    @Override
    protected void disposeVolleySuccess(Object result) {
        CommonUtil.showNotice(getActivity(),tvNotice, (String) result);
        //将登陆成功的用户名保存在本地，下次检测到保存的用户名则直接跳转个人新闻界面，不用登陆
//        SharedPreUtil.saveString(getActivity(),SharedPreUtil.KEY_USERNAME,strUserName);
//        Fragment fragment = new PersonalNewsFragment();
        Fragment fragment =PersonalNewsFragment_.builder().build();
        getFragmentManager().beginTransaction().replace(R.id.container_fragment,fragment)
                .commitAllowingStateLoss();
    }

    @Override
    protected void disposeVolleyFailed(Object result) {
        CommonUtil.showNotice(getActivity(),tvNotice, (String) result);
        SharedPreUtil.removeString(getActivity(),SharedPreUtil.KEY_USERNAME);
    }

}
