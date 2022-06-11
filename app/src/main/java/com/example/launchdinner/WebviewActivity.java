package com.example.launchdinner;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.URISyntaxException;

//webview 더치트를 열기 위한 activity
public class WebviewActivity extends AppCompatActivity {

    private String TAG = WebviewActivity.class.getSimpleName();

    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient());  // 새 창 띄우기 않기
////        webView.setWebChromeClient(new WebChromeClient());
////
//        webView.getSettings().setLoadWithOverviewMode(true);  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
//        webView.getSettings().setUseWideViewPort(true);  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함
////
//        webView.getSettings().setSupportZoom(false);  // 줌 설정 여부
//        webView.getSettings().setBuiltInZoomControls(false);  // 줌 확대/축소 버튼 여부
////
//        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용여부
////        webView.addJavascriptInterface(new AndroidBridge(), "android");
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // javascript가 window.open()을 사용할 수 있도록 설정
//        webView.getSettings().setSupportMultipleWindows(true); // 멀티 윈도우 사용 여부
////
//        webView.getSettings().setDomStorageEnabled(true);  // 로컬 스토리지 (localStorage) 사용여부

        webView.setWebViewClient(new WebViewClientClass());

        WebViewClientClass wc = new WebViewClientClass();

        wc.shouldOverrideUrlLoading(webView, "https://thecheat.co.kr");

        //웹페이지 호출
//        webView.loadUrl("http://www.naver.com");
        webView.loadUrl("https://thecheat.co.kr");
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null && url.startsWith("intent://")) { // url이 intent://를 포함하고 있으면 앱으로 이동
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                    if (existPackage != null) {
                        startActivity(intent);
                    } else {
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                        marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                        startActivity(marketIntent);
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (url != null && url.startsWith("market://")) { // url이 market://을 포함하고 있으면 플레이 스토어로 이동
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    if (intent != null) {
                        startActivity(intent);
                    }
                    return true;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            view.loadUrl(url);
            return false;
        }
    }
}