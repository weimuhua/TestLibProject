package baidu.com.testlibproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import baidu.com.commontools.http.HttpUtils;
import baidu.com.commontools.threadpool.MhThreadPool;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private static final String URL = "http://www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testHttp();
    }

    private void testHttp() {
        MhThreadPool.getInstance().addUiTask(new Runnable() {
            @Override
            public void run() {
                try {
                    String resp = HttpUtils.commonGet(URL);
                    if (DEBUG) {
                        Log.d(TAG, "commonGet resp : " + resp);
                    }
                } catch (IOException e) {
                    if (DEBUG) {
                        LogHelper.w(TAG, "http exception", e);
                    }
                }
            }
        });
    }
}
