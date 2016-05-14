package bigorange.myandroidwebview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by voidhug on 16/5/13.
 */

public class MainActivity extends Activity {
    private WebView webView;
    private Button back;
    private Button refresh;
    private TextView titleView;
    private TextView mTextView_Error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        webView = (WebView) findViewById(R.id.webView);
        // 只写 loadUrl，会用浏览器去打开
        webView.loadUrl("http://www.baidu.com/");

        back = (Button) findViewById(R.id.back);
        refresh = (Button) findViewById(R.id.refresh);
        titleView = (TextView) findViewById(R.id.title);
        mTextView_Error = (TextView) findViewById(R.id.textView_error);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleView.setText(title);
                super.onReceivedTitle(view, title);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 用当前项目打开，而不是用浏览器
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.d("MainActivity", "onReceivedError");
//                view.loadUrl("file:///android_asset/error.html");
                mTextView_Error.setText("404 error");
                webView.setVisibility(View.GONE);
            }
        });

        refresh.setOnClickListener(new MyLisenter());
        back.setOnClickListener(new MyLisenter());
        webView.setDownloadListener(new MyDownload());
    }

    class MyLisenter implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.refresh:
                    webView.reload();
                    break;
                case R.id.back:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    class MyDownload implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Log.d("MyDownload", "url---------------------->" + url);
            if (url.endsWith(".apk")) {
//                new HttpThread(url).start();
                // 通过系统去下载
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }
    }
}
