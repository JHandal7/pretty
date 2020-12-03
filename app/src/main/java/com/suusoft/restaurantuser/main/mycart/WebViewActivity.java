package com.suusoft.restaurantuser.main.mycart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivity;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.Order;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.widgets.dialog.MyProgressDialog;

public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private MyProgressDialog progressDialog;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initView() {
        webView = (WebView) findViewById(R.id.web_view);
        setUpWebView(webView);
        progressDialog = new MyProgressDialog(this);
    }

    @Override
    protected void onViewCreated() {
        setToolbarTitle(R.string.payment);
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString(Constant.URL_PAYMENT);
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing())
            progressDialog.show();
        webView.loadUrl(url);
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NAVI;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    private void setUpWebView(final WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("EEEEE",url);
                if (url.contains("token") && url.contains("type") && url.contains("objectid")) {
                    confirmPayment(url);
                } else {
                    webView.loadUrl(url);
                }
                return true;

            }
        });
    }

    private void confirmPayment(String url) {
        RequestManager.comfirmPayment(url, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                Intent intent = new Intent();
                Order order = response.getDataObject(Order.class);
                if (order != null) {
                    intent.putExtra(Constant.KEY_ORDER, order);
                    setResult(Constant.RC_CONFIRM_PAYMENT, intent);
                    finish();
                }

            }

            @Override
            public void onError(String message) {
                AppUtil.showToast(getApplicationContext(), message);
                finish();
            }
        });
    }
}
