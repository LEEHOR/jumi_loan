package com.cashloan.jumidai.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cashloan.jumidai.R;
import com.cashloan.jumidai.common.BundleKeys;
import com.cashloan.jumidai.common.RouterUrl;
import com.cashloan.jumidai.utils.SharedInfo;
import com.commom.base.BaseMvpActivity;
import com.commom.base.BasePresenter;
import com.commom.utils.EmptyUtils;
import com.github.mzule.activityrouter.annotation.Router;

/**
 * 作者： Ruby
 * 时间： 2018/9/1
 * 描述： 通用的webview
 */
@Router(value = RouterUrl.AppCommon_WebView, stringParams = {BundleKeys.TITLE, BundleKeys.URL, BundleKeys.POST_DATA})
public class WebViewActivity extends BaseMvpActivity {

    private WebView mWebView;
    private String  url;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView() {
        mWebView = findViewById(R.id.webView);


        Intent intent = getIntent();
        String title = intent.getStringExtra(BundleKeys.TITLE);
        url = intent.getStringExtra(BundleKeys.URL);
        String postData = intent.getStringExtra(BundleKeys.POST_DATA);

        String urlTemp = (String) SharedInfo.getInstance().getValue(BundleKeys.URL, "");
        if (!TextUtils.isEmpty(urlTemp)) {
            url = urlTemp;
            SharedInfo.getInstance().saveValue(BundleKeys.URL, "");
        }
        if (null != title) {
            setPageTitleBack(title);
        } else {
            setPageTitleBack("安心用");
        }
    }

    @Override
    protected void initFunc() {
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

        mWebView.loadUrl(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();

    }
}
