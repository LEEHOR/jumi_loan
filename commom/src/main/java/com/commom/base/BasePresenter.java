package com.commom.base;

import android.content.Context;

import com.commom.net.RxJava.RxManager;
import com.commom.utils.EmptyUtils;

import java.lang.ref.SoftReference;

/**
 * 作者： Ruby
 * 时间： 2018/8/6$
 * 描述：
 */

public abstract class BasePresenter<V extends BaseView> {

    private   SoftReference<V> mViewRef;
    protected RxManager        rxManager;
    protected Context          mContext;
    protected V                mView;

    public void attachView(V view, Context con) {
        if (EmptyUtils.isNull(view)) {
            throw new NullPointerException("BasePresenter#attechView view can not be null");
        }

        mContext = con;
        mViewRef = new SoftReference<>(view);
        rxManager = new RxManager();
        mView = view;
    }

    protected boolean isViewAttached() {
        return EmptyUtils.isNotEmpty(mViewRef) && EmptyUtils.isNotEmpty(mViewRef.get());
    }

    protected V getView() {
        return mViewRef.get();
    }

    public void detachView() {
        if (isViewAttached()) {
            mViewRef.clear();
            mViewRef = null;
            rxManager.clear();
        }
    }
}
