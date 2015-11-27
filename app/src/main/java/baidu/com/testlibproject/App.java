package baidu.com.testlibproject;

import android.app.Application;

public class App extends Application {

    private static String TAG = "ProjectApp";
    private static boolean DEBUG = FeatureConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG) LogHelper.d(TAG, "Application onCreate.");
    }
}
