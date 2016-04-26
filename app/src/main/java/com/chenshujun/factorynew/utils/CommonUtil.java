package com.chenshujun.factorynew.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.view.ConfirmDialog;
import com.chenshujun.factorynew.view.LoadingView;

import java.security.MessageDigest;

public class CommonUtil {

    /**
     * @注释:显示信息提示
     */
    public static void showNotice(Context context,final TextView textView, String noticeMsg) {
        if(textView == null || noticeMsg == null || context == null ){
            return;
        }
        textView.setText(noticeMsg);
        Animation noticeIn = AnimationUtils.loadAnimation(context, R.anim.notice_in);
        final Animation noticeOut = AnimationUtils.loadAnimation(context, R.anim.notice_out);
        //控件保存在动画结束的状态
        noticeIn.setFillAfter(true);
        noticeOut.setFillAfter(true);
        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(noticeIn);

        new AsyncTask<String, Void, Float>() {
            @Override
            protected Float doInBackground(String... params) {
                try {
                    //notice显示1.5s
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Float aFloat) {
                //notice退出
                textView.startAnimation(noticeOut);
                //  noticeTextView.setVisibility(View.GONE);
            }
        }.execute();
    }
    /**
     * 打卡软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 获取输入框焦点
     */
    public static  void editTextGetFocus(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setEnabled(true);
        editText.setClickable(true);
        editText.requestFocus();
    }

    /**
     * 获取文本框焦点
     */
    public static  void setEnabled(Button btn) {
        btn.setEnabled(true);
    }
    /**
     * 获取文本框焦点,后按钮变亮
     */
    public static  void setEnabled(final EditText editText, final Button btn) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    btn.setEnabled(true);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 按钮失效*/
    public static void invalidButton(TextView button){
        button.setEnabled(false);
    }

    /**
     * 启用按钮*/
    public static void enableButton(TextView button){
        button.setEnabled(true);
    }

    /**
     * @注释:提示弹出框
     */
    public static void showNotice(String noticeMsg,Context context) {
        final ConfirmDialog noticeDialog = new ConfirmDialog(context);
        noticeDialog.show(noticeMsg, ConfirmDialog.NOTICE);

        new AsyncTask<String, Void, Float>() {
            @Override
            protected Float doInBackground(String... params) {
                try {
                    //notice显示1.5s
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Float aFloat) {
                noticeDialog.dismiss();
            }
        }.execute();
    }

    /**
     * 将dp 转化为px
     */
    public static int changeDipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
     *
     * @param context
     * @return
     */
    public static int isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return 0;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return 1;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            String extraInfo = netWorkInfo.getExtraInfo();
                            if ("cmwap".equalsIgnoreCase(extraInfo)
                                    || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                return 2;
                            }
                            return 3;
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * @note:MD5加密*/
    public static String md5(String paramString) {
        String returnStr;
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            returnStr = byteToHexString(localMessageDigest.digest());
            return returnStr;
        } catch (Exception e) {
            return paramString;
        }
    }

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }
}