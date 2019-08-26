package com.commom.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.commom.R;
import com.commom.utils.ActivityManage;
import com.commom.utils.ActivityStack;
import com.commom.utils.EmptyUtils;
import com.commom.utils.OnOnceClickListener;
import com.commom.utils.ScreenUtils;
import com.commom.widget.LoadingLayout;
import com.commom.widget.TitleBar;

import java.util.List;

/**
 * 作者： Ruby
 * 时间： 2018/8/6
 * 描述： BaseMvpActivity
 */

public abstract class BaseMvpActivity<P extends BasePresenter> extends AppCompatActivity implements BaseMView {

    //    protected RxManager mRxManager;
    protected Context mContext;
    protected P       mPresenter;


    protected LayoutInflater mInflater;
    //根布局
    protected LinearLayout   mBaseRoot;
    //状态栏
    protected View           mStatusBar;
    //标题导航栏
    protected TitleBar       mTitleBar;
    //页面状态
    protected LoadingLayout  mLoadingLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = this;
        ActivityManage.push(this);
        if (EmptyUtils.isNotEmpty(getSupportActionBar())) {
            getSupportActionBar().hide();
        }

        super.setContentView(R.layout.activity_base);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        mRxManager = new RxManager();

        // 设置状态栏字体颜色为白色
        ScreenUtils.setTextColorStatusBar((Activity) mContext, false);
        hideStatusBar();
        initRootView();
        setStatusBar();
        hideTitleBar();

        mPresenter = createPresenter();
        if (null != mPresenter) {
            mPresenter.attachView(this, mContext);
        }

        View view = mInflater.inflate(getLayoutId(), mBaseRoot, false);
        mLoadingLayout.addView(view);

        //添加Activity到堆栈
        ActivityStack.getInstance().addActivity(this);

        initView();
        initFunc();
    }

    /*** 初始化Presenter在setContentView之前 ***/
    protected abstract P createPresenter();

    /*** 获取页面layout ***/
    protected abstract int getLayoutId();

    /*** 初始化界面控件 ***/
    protected abstract void initView();

    /*** 初始化数据以及其他请求操作 ***/
    protected abstract void initFunc();


    /*** 获取根布局layout ***/
    private void initRootView() {
        mInflater = LayoutInflater.from(this);
        mBaseRoot = this.findViewById(R.id.base_root);
        mStatusBar = this.findViewById(R.id.mStatusBar);
        mTitleBar = this.findViewById(R.id.mTitleBar);
        mLoadingLayout = this.findViewById(R.id.mLoadingLayout);
    }


    /*** 设置状态栏背景透明 ***/
    protected void hideStatusBar() {
        ScreenUtils.translateStatusBar((Activity) mContext);
        setTheme(R.style.TranslucentTheme);
    }

    /*** 设置状态栏高度,颜色 ***/
    protected void setStatusBar() {
        mStatusBar.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = mStatusBar.getLayoutParams();
        layoutParams.height = ScreenUtils.getStatusHeight(mContext);
        mStatusBar.setLayoutParams(layoutParams);
        mStatusBar.setBackgroundResource(R.color.base_statusBar);
    }

    /*** 隐藏titleBar ***/
    protected void hideTitleBar() {
        mTitleBar.setVisibility(View.GONE);
    }

    /*** 设置页面返回 ***/
    protected void setPageBack() {
        mTitleBar.setVisibility(View.VISIBLE);
        mTitleBar.setLeftMenu(R.mipmap.icon_common_back_white, null, new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void setPageBack(int res) {
        mTitleBar.setVisibility(View.VISIBLE);
        mTitleBar.setLeftMenu(res, null, new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                onBackPressed();
            }
        });
    }

    /*** 设置页面标题以及左返回 ***/
    protected void setPageTitleBack(String title) {
        mTitleBar.setVisibility(View.VISIBLE);
        mTitleBar.setTitleText(title);
        mTitleBar.setLeftMenu(R.mipmap.icon_common_back_white, null, new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void setPageTitle(String title) {
        mTitleBar.setVisibility(View.VISIBLE);
        mTitleBar.setTitleText(title);
    }

    /*** 设置标题右侧按钮 ***/
    protected void setTitleRight(int rightIcon, String rightText, OnOnceClickListener onceClickListener) {
        mTitleBar.setVisibility(View.VISIBLE);
        mTitleBar.setRightMenu(rightIcon, rightText, onceClickListener);
    }


    /*****  通用的页面状态操作以及加载、提示文字显示等 --start   ***/
    @Override
    public void statusLoading() {
        mLoadingLayout.setStatus(LoadingLayout.Status.Loading);
    }

    @Override
    public void statusNoNetwork() {
        mLoadingLayout.setStatus(LoadingLayout.Status.No_Network);
    }

    @Override
    public void statusEmpty() {
        mLoadingLayout.setStatus(LoadingLayout.Status.Empty);
    }

    @Override
    public void statusError() {
        mLoadingLayout.setStatus(LoadingLayout.Status.Error);
    }

    @Override
    public void statusContent() {
        mLoadingLayout.setStatus(LoadingLayout.Status.Success);
    }

    @Override
    public void statusReTry(LoadingLayout.OnReloadListener onReloadListener) {
        if (EmptyUtils.isNull(onReloadListener)) return;
        mLoadingLayout.setOnReloadListener(onReloadListener);
    }

    @Override
    public void showToastMsg(String msg) {

    }

    @Override
    public void showProgressingDialog() {

    }

    @Override
    public void dismissProgressDialog() {

    }

    /*****  通用的页面状态操作以及加载、提示文字显示等 --end  ***/


    protected void attachClickListener(View view) {
        if (view != null) {
            view.setOnClickListener(clickListener);
        }
    }

    private OnOnceClickListener clickListener = new OnOnceClickListener() {
        @Override
        public void onOnceClick(View v) {
            onViewClicked(v);
        }
    };

    protected void onViewClicked(View view) {

    }


    @Override
    public void onBackPressed() {
        releaseMemory();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        releaseMemory();
        super.onDestroy();
        ActivityManage.remove(this);
    }

    /*** 页面关闭时释放内存 ***/
    private void releaseMemory() {
        if (mPresenter != null) mPresenter.detachView();
//        if (mRxManager != null) mRxManager.clear();
        // 清除栈
        ActivityStack.getInstance().finishActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("permission", permissions.toString());
//        PermissionCheck.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0 || index >= fm.getFragments().size()) {
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag != null) {
                handleResult(frag, requestCode, resultCode, data);
            } else {
            }
        }
        if (EmptyUtils.isNotEmpty(mActivityResultListener)) {
            mActivityResultListener.activityResult(requestCode, resultCode, data);
        }
    }

    protected static BaseActivityResultListener mActivityResultListener;

    public interface BaseActivityResultListener {

        void activityResult(int requestCode, int resultCode, Intent data);

    }

    /**
     * 递归调用，对所有子 Fragment 生效
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }
}
