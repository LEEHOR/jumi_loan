package com.commom.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.commom.net.RxJava.RxManager;

/**
 * 作者： zxb
 * 时间： 2018/4/8
 * 描述：
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;

    protected String rxTag;

    protected RxManager mRxManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题
        super.onCreate(savedInstanceState);
        mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏

        mRxManager = new RxManager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRxManager != null) {
            mRxManager.clear();
        }

    }
}
