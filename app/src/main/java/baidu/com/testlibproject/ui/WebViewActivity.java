package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import baidu.com.testlibproject.R;

public class WebViewActivity extends Activity {

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
    }

    private void initView() {
        mProgressBar = findViewById(R.id.progress_bar);

        WebView webview = findViewById(R.id.WebView);
        webview.setWebViewClient(new MWebClient());
        webview.setWebChromeClient(new MChromeClient());
        webview.loadUrl("http://www.baidu.com");
    }

    private class MWebClient extends WebViewClient {

        private long mStartTimeStamp;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mStartTimeStamp = System.currentTimeMillis();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String text = url + " onPageFinished, cost : "
                    + (System.currentTimeMillis() - mStartTimeStamp);
            Toast.makeText(getApplication(), text, Toast.LENGTH_LONG).show();
        }
    }

    private class MChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setProgress(newProgress);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }
}
