package baidu.com.testlibproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import baidu.com.commontools.utils.LogHelper;
import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.IMainService;
import baidu.com.testlibproject.service.stub.SubInterfaceAStub;
import baidu.com.testlibproject.service.stub.SubInterfaceBStub;
import baidu.com.testlibproject.service.stub.SubInterfaceCStub;

public class MainService extends Service {

    private static final String TAG = "MainService";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private IMainService.Stub mBinder = new IMainService.Stub() {
        @Override
        public IBinder getInterfaceA() {
            return new SubInterfaceAStub();
        }

        @Override
        public IBinder getInterfaceB() {
            return new SubInterfaceBStub();
        }

        @Override
        public IBinder getInterfaceC() {
            return new SubInterfaceCStub();
        }

        @Override
        public int add(int a, int b) {
            return a + b;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        ClassLoader classLoader = getClassLoader();
        if (DEBUG) {
            LogHelper.d(TAG, "classLoader = " + classLoader + " hashCode = " + classLoader.hashCode());
        }

        if (DEBUG) LogHelper.d(TAG, "MainService onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (DEBUG) LogHelper.d(TAG, "MainService onBind");
        return mBinder;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (DEBUG) LogHelper.d(TAG, "onTaskRemoved");
    }
}
