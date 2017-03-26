package baidu.com.testlibproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.IMainService;
import baidu.com.testlibproject.LogHelper;
import baidu.com.testlibproject.service.stub.SubInterfaceAStub;
import baidu.com.testlibproject.service.stub.SubInterfaceBStub;
import baidu.com.testlibproject.service.stub.SubInterfaceCStub;

public class MainService extends Service {

    private static final String TAG = "MainService";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private IMainService.Stub mBinder = new IMainService.Stub() {
        @Override
        public IBinder getInterfaceA() throws RemoteException {
            return new SubInterfaceAStub();
        }

        @Override
        public IBinder getInterfaceB() throws RemoteException {
            return new SubInterfaceBStub();
        }

        @Override
        public IBinder getInterfaceC() throws RemoteException {
            return new SubInterfaceCStub();
        }

        @Override
        public int add(int a, int b) throws RemoteException {
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
