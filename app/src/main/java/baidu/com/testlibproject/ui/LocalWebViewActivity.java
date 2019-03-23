package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

import baidu.com.testlibproject.R;

public class LocalWebViewActivity extends Activity {

    private static final String TAG = "LocalWebViewActivity";

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_web_view);
        mWebView = findViewById(R.id.WebView);
        mWebView.loadUrl("file:///android_asset/web/index.html");
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.d(TAG, "shouldOverrideUrlLoading, url = " + url);
                Uri u = Uri.parse(url);
                if (u != null) {
                    Map<String, String> map = new HashMap<>();
//                    Set<String> paramNames = u.getQueryParameterNames();
//                    for (String key : paramNames) {
//                        map.put(key, u.getQueryParameter(key));
//                    }

                    String cmd = u.getAuthority();
                    String subCmd = u.getPath();
                    if (subCmd != null && subCmd.length() > 0) {
                        subCmd = subCmd.substring(1);
                    }

                    Log.d(TAG, "cmd = " + cmd + ", subCmd = " + subCmd);

                    if ("test".equals(subCmd)) {
                        mWebView.loadUrl("javascript:callback()");
                        return true;
                    } else if ("callback".equals(subCmd)) {
                        return true;
                    }

                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}
