package com.chenshujun.factorynew.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chenshujun.factorynew.R;
import com.chenshujun.factorynew.utils.UrlUtil;
import com.chenshujun.factorynew.tools.VolleyUtil;

/**
 * Created by chenshujun on 2016/3/21.
 */
public class GuideActivity extends AppCompatActivity {
    int requestType = -1;
    Context context;
    GuideActivity guideActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        guideActivity = this;

        requestType = VolleyUtil.REQUEST_NEWS_CHANAL;
        VolleyUtil.getVolleyUtil().requestNewsChanal(getApplicationContext(), VolleyUtil.METHOD_GET, UrlUtil.getNewChannel(), requestType);

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.execute();
    }
}
