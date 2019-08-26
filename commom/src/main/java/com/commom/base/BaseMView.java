package com.commom.base;

import com.commom.widget.LoadingLayout;

/**
 * 作者： Ruby
 * 时间： 2018/8/6$
 * 描述：
 */

public interface BaseMView extends BaseView {


    void statusLoading();

    void statusNoNetwork();

    void statusEmpty();

    void statusError();

    void statusContent();

    void statusReTry(LoadingLayout.OnReloadListener reloadListener);

}
