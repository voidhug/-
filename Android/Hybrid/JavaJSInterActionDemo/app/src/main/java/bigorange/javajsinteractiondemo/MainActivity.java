package bigorange.javajsinteractiondemo;

import android.app.Activity;

/**
 * Created by voidhug on 16/5/14.
 */
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String LOGTAG = "MainActivity";
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new JsInteration(), "control");
        myWebView.setWebChromeClient(new WebChromeClient() {});
        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                testMethod(myWebView);
                testEvaluateJavascript(myWebView);
            }

        });
        myWebView.loadUrl("file:///android_asset/js_java_interaction.html");
    }

    private void testMethod(WebView webView) {
        String call = "javascript:sayHello()";

        call = "javascript:alertMessage(\"" + "content" + "\")";

        call = "javascript:toastMessage(\"" + "content" + "\")";

        call = "javascript:sumToJava(1,2)";
        webView.loadUrl(call);
    }

    private void testEvaluateJavascript(WebView webView) {
        webView.evaluateJavascript("getGreetings()", new ValueCallback<String>() {

            @Override
            public void onReceiveValue(String value) {
                Log.i(LOGTAG, "onReceiveValue value=" + value);
            }
        });
    }

    public class JsInteration {

        @JavascriptInterface
        public void toastMessage(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void onSumResult(int result) {
            Log.i(LOGTAG, "onSumResult result=" + result);
        }
    }

}