package baidu.com.testlibproject.plugin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import baidu.com.testlibproject.BuildConfig;
import baidu.com.testlibproject.LogHelper;
import baidu.com.testlibproject.R;

public class TargetActivity extends Activity {

    private static final String TAG = TargetActivity.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        Toast.makeText(this, "Great!I'm a activity which not registered in AndroidManifest.xml",
                Toast.LENGTH_LONG).show();
        if (DEBUG) {
            LogHelper.d(TAG, "onCreate");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) {
            LogHelper.d(TAG, "onResume");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DEBUG) {
            LogHelper.d(TAG, "onStart");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (DEBUG) {
            LogHelper.d(TAG, "onPause");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            LogHelper.d(TAG, "onDestroy");
        }
    }
}
