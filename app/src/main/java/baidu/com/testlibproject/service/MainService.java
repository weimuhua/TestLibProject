package baidu.com.testlibproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.IMainService;
import baidu.com.testlibproject.LogHelper;

public class MainService extends Service {

    private static final String TAG = "MainService";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private IMainService.Stub mBinder = new IMainService.Stub() {
        @Override
        public int add(int a, int b) throws RemoteException {
            try {
                //should not sleep, just for test!!!
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                if (DEBUG) LogHelper.e(TAG, "thread sleep failed", e);
            }
            return a + b;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG) LogHelper.d(TAG, "MainService onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (DEBUG) LogHelper.d(TAG, "MainService onBind");
        return mBinder;
    }
}
