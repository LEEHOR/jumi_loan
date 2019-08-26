package com.cashloan.jumidai.ui.homeMine.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.utils.viewInject.AnnotateUtils;
import com.cashloan.jumidai.utils.viewInject.ViewInject;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.utils.EmptyUtils;
import com.commom.utils.OnOnceClickListener;
import com.github.mzule.activityrouter.annotation.Router;
import com.github.mzule.activityrouter.router.Routers;

/**
 * 作者： Ruby
 * 时间： 2018/9/3
 * 描述： 帮助中心
 */
@Router(value = RouterUrl.Mine_HelpCenter, stringParams = BundleKeys.URL)
public class HelpCenterActivity extends BaseMvpActivity {

    @ViewInject(R.id.web_help)
    private WebView mWebView;
    private String  url;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help_center;
    }

    @Override
    protected void initView() {
        AnnotateUtils.inject(this);

        setPageTitleBack("帮助中心");
        setTitleRight(R.drawable.icon_advice, null, new OnOnceClickListener() {

            @Override
            public void onOnceClick(View v) {
                Routers.open(mContext, RouterUrl.getRouterUrl(RouterUrl.Mine_Settings_Idea));
            }
        });

    }

    @Override
    protected void initFunc() {
        url = getIntent().getStringExtra(BundleKeys.URL);

        final ProgressDialog progressDialog = new ProgressDialog(mContext);//1.创建一个ProgressDialog的实例
        progressDialog.setMessage("正在加载...");//2.设置显示内容
        progressDialog.setCancelable(true);//3.设置可否用back键关闭对话框

        WebSettings setting = mWebView.getSettings();
        // 支持缩放
        setting.setSupportZoom(false);
        setting.setUseWideViewPort(false);
        setting.setLoadWithOverviewMode(true);
        // 设置WebView属性，能够执行Javascript脚本
        setting.setJavaScriptEnabled(true);
        setting.setSavePassword(false);
        setting.setDomStorageEnabled(true);
        setting.setDefaultTextEncodingName("utf-8");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (EmptyUtils.isNotEmpty(progressDialog)) {
                    progressDialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (EmptyUtils.isNotEmpty(progressDialog)) {
                    progressDialog.dismiss();
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());

        System.out.println("web页接收的url：" + url);

        if (EmptyUtils.isNotEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }
}
