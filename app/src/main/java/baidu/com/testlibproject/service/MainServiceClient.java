package baidu.com.testlibproject.service;

import android.content.Context;

import baidu.com.testlibproject.FeatureConfig;

public class MainServiceClient {

    private static final String TAG = "MainServiceClient";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private static MainServiceClient sInstance;

    private Context mAppContext;

    public static MainServiceClient getInstance(Context cxt) {
        if (sInstance == null) {
            synchronized (MainServiceClient.class) {
                if (sInstance == null) {
                    sInstance = new MainServiceClient(cxt);
                }
            }
        }
        return sInstance;
    }

    private MainServiceClient(Context cxt) {
        mAppContext = cxt.getApplicationContext();
    }
}
