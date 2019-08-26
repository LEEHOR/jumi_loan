package com.commom.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commom.R;
import com.commom.net.RxJava.RxManager;
import com.commom.utils.EmptyUtils;
import com.commom.utils.OnOnceClickListener;
import com.commom.utils.ScreenUtils;
import com.commom.utils.ToastManager;
import com.commom.utils.ViewUtils;
import com.commom.widget.LoadingLayout;
import com.commom.widget.TitleBar;


/**
 * 作者： Ruby
 * 时间： 2018/8/21$
 * 描述：
 */

public abstract class BaseMvpFragment<P extends BasePresenter> extends Fragment implements BaseMView {

    protected P         mPresenter;
    protected Context   mContext;
    protected RxManager mRxManager;

    protected TitleBar      mTitleBar;
    protected LoadingLayout mLoadingLayout;
    protected View          mStatusBar;                  // 状态栏

    /**
     * 缓存Fragment view
     * 避免多个Fragment切换时UI重绘
     */
    protected View contentView = null;

    public final static int mPageSize            = 10;
    protected           int mPageIndex           = 1;
    protected           int mIsRefreshOrLoadMore = 0; // 0是刷新1是加载更多
    protected boolean mIsHasNoData;         // 是否有更多数据

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        mRxManager = new RxManager();

        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this, mContext);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.activity_base, container, false);
            initRootView();
            setStatusBar();
            hideTitleBar();

            View view = inflater.inflate(getLayoutId(), container, false);
            mLoadingLayout.addView(view);

            initView(view);
            initFunc();
        }

        // 缓存的mView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) contentView.getParent();
        if (parent != null) {
            parent.removeView(contentView);
        }

        return contentView;
    }

    /*** 初始化Presenter在setContentView之前 ***/
    protected abstract P createPresenter();

    /*** 获取页面layout ***/
    protected abstract int getLayoutId();

    /*** 初始化界面控件 ***/
    protected abstract void initView(View view);

    /*** 初始化数据以及其他请求操作 ***/
    protected abstract void initFunc();


    /** 设置状态栏高度 */
    protected void setStatusBar() {
        mStatusBar.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = mStatusBar.getLayoutParams();
        layoutParams.height = ScreenUtils.getStatusHeight(mContext);
        mStatusBar.setLayoutParams(layoutParams);
        mStatusBar.setBackgroundResource(R.color.base_statusBar);
    }

    public TitleBar getTitleBar() {
        return mTitleBar;
    }

    public View getStatusBar() {
        return mStatusBar;
    }

    protected void hideTitleBar() {
        mTitleBar.setVisibility(View.GONE);
    }

    private void initRootView() {
        mLoadingLayout = contentView.findViewById(R.id.mLoadingLayout);
        mTitleBar = contentView.findViewById(R.id.mTitleBar);
        mStatusBar = contentView.findViewById(R.id.mStatusBar);
    }

    /*** 设置页面返回 ***/
    protected void setTitleLeft(int leftIcon, String leftText, OnOnceClickListener onceClickListener) {
        mTitleBar.setVisibility(View.VISIBLE);
        mTitleBar.setLeftMenu(leftIcon, leftText, onceClickListener);
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
    public void statusError() {
        mLoadingLayout.setStatus(LoadingLayout.Status.Error);
    }

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
    public void statusContent() {
        mLoadingLayout.setStatus(LoadingLayout.Status.Success);
    }

    @Override
    public void statusReTry(LoadingLayout.OnReloadListener listener) {
        if (EmptyUtils.isNull(listener)) {
            return;
        }
        mLoadingLayout.setOnReloadListener(listener);
    }

    @Override
    public void showToastMsg(String msg) {
        ToastManager.showShortToast(msg);
    }


    @Override
    public void showProgressingDialog() {
    }

    @Override
    public void dismissProgressDialog() {
    }

    /*****  通用的页面状态操作以及加载、提示文字显示等 --end  ***/


    public void gotoActivity(Class<?> cls) {
        gotoActivity(cls, null);
    }

    /***含有Bundle  通过Class跳转界面 ***/
    public void gotoActivity(Class<?> cls, Bundle bundle) {
        if (null == cls) {
            return;
        }

        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    protected void attachClickListener(View view) {
        if (contentView != null) {
            if (view != null) {
                view.setOnClickListener(clickListener);
            }
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

    protected void clearMemory() {
        if (mPresenter != null) mPresenter.detachView();
        if (mRxManager != null) mRxManager.clear();

        ViewUtils.clearAllChildViews(getActivity());
    }

    @Override
    public void onDestroy() {
        clearMemory();
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionCheck.getInstance().onRequestPermissionsResult(getActivity(), requestCode, permissions, grantResults);
    }
}
