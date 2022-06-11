package com.example.launchdinner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class addressActivity extends AppCompatActivity {

    private WebView webView ;
    private TextView result;
    private Handler handler;
    String localIp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        localIp = getString(R.string.localip);
        Log.d("localIp ", localIp);

        init_webView();
        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);

    }

    public void init_webView() {
        Log.d("호출","init_webView");
            webView = (WebView) findViewById(R.id.web_View);
            // JavaScript 허용
            webView.getSettings().setJavaScriptEnabled(true);
            // JavaScript의 window.open 허용
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
            // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
            webView.addJavascriptInterface(new AndroidBridge(), "launchdinner");

        // web client 를 chrome 으로 설정
            webView.setWebChromeClient(new WebChromeClient());
            // webview url load
            webView.loadUrl("http://175.196.226.213:8008/address");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("call","call run");
                    String index;

                    //어느 화면에서 호출했는지 분기 태움
                    if(getIntent()!=null){
                        Log.d("call","getIntent");
                        Intent getintent = getIntent();
                        index = getintent.getStringExtra("index");
                        if(index!=null){
                            if(index.equals("departure")){
                                Intent intent = new Intent(addressActivity.this, HomeActivity.class);
                                intent.putExtra("arg1",arg1);
                                intent.putExtra("arg2",arg2);
                                intent.putExtra("arg3",arg3);
                                intent.putExtra("index","departure");
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }else if(index.equals("arrive")){
                                Intent intent = new Intent(addressActivity.this, HomeActivity.class);
                                intent.putExtra("arg1",arg1);
                                intent.putExtra("arg2",arg2);
                                intent.putExtra("arg3",arg3);
                                intent.putExtra("index","arrive");
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }else if(index.equals("userinfo")){
                                Intent intent = new Intent(addressActivity.this, HomeActivity.class);
                                intent.putExtra("arg1",arg1);
                                intent.putExtra("arg2",arg2);
                                intent.putExtra("arg3",arg3);
                                intent.putExtra("index","userinfo");
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }else{
                            Log.d("call","else");
                            Intent intent = new Intent(addressActivity.this, registerActivity.class);
                            intent.putExtra("arg1",arg1);
                            intent.putExtra("arg2",arg2);
                            intent.putExtra("arg3",arg3);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }

}