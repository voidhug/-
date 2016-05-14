package bigorange.javajsinteractiondemo2;

import android.app.Activity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("JavascriptInterface")
public class MainActivity extends Activity implements android.view.View.OnClickListener{
    private WebView webView;
    private ProgressBar progressBar;
    private Button btn_js;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        btn_js = (Button)findViewById(R.id.btn_js);
        btn_js.setOnClickListener(this);
        progressBar = new ProgressBar(this, null,
                android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                5));

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        // 启用javascrip功能
        webSettings.setJavaScriptEnabled(true);
        //设置可以使用中文，否则会出现中文乱码
        webSettings.setDefaultTextEncodingName("gbk");
        webView.addView(progressBar);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE)
                        progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                // TODO Auto-generated method stub
                return super.onJsPrompt(view, url, message, defaultValue,
                        result);
            }
        });
        /*
         * 此段代码在不做处理的情况下会弹出多个浏览器选择界面 webView.loadUrl("http://www.baidu.com/");
         */
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
//         webView.loadUrl("http://www.baidu.com/");
        /**
         * 自定义对象，以及对象别名
         * 自定义对象中定义的方法可以供js调用
         * function jsCallAndroidAlert(){
         jsobject.androidAlert();
         }
         */
        webView.addJavascriptInterface(new JsObject(), "jsobject");
        //加载我们自定义的html网页并将其显示在webView上
        webView.loadUrl("file:///android_asset/jshtml.html");
    }

    /**
     * 此类的方法对应html中js的方法，供js调用Android的方法
     *
     * @author Administrator
     *
     */
    class JsObject {
        @JavascriptInterface
        public void androidAlert() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("温馨提示");
            dialog.setMessage("js调用了Android的Alert，测试完毕是否退出？");
            dialog.setNegativeButton("否", new OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            dialog.setPositiveButton("是", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog2 = dialog.create();
            dialog2.show();
            Toast.makeText(MainActivity.this, "您使用js调用了alert方法",
                    Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void androidConfirm() {
            Toast.makeText(MainActivity.this, "您使用js调用了confirm方法",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 对系统的back键进行设置
     */
    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_js:
                //android调用js代码
                webView.loadUrl("javascript:test_alert()");
                break;
        }
    }
}