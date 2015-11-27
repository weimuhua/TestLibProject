package baidu.com.testlibproject.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;

import java.util.concurrent.atomic.AtomicBoolean;

import baidu.com.commontools.utils.AsyncHandler;
import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.IMainService;
import baidu.com.testlibproject.LogHelper;

public class MainServiceClient {

    private static final String TAG = "MainServiceClient";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private static MainServiceClient sInstance;

    private Context mAppContext;
    private IMainService mService;
    private final AtomicBoolean mIsConnecting = new AtomicBoolean(false);

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

    public void tryToConnect() {
        connectServiceIfNeeded();
    }

    private void connectServiceIfNeeded() {
        if (mService != null && mService.asBinder().isBinderAlive()) {
            if (DEBUG) LogHelper.d(TAG, "binder is alive...");
            return;
        }

        mIsConnecting.set(true);

        ServiceConnection connection = new ServiceConnection() {

            private boolean mConnectLost = false;

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (DEBUG) LogHelper.d(TAG, "connected service");
                if (!mConnectLost) {
                    mService = IMainService.Stub.asInterface(service);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if (DEBUG) LogHelper.d(TAG, "service disconnected");
                if (mConnectLost) return;//avoid connect service multi times??
                mService = null;
                mIsConnecting.set(false);

                delayConnectService();
            }
        };

        Intent intent = new Intent(mAppContext, MainService.class);
        if (DEBUG) LogHelper.d(TAG, "try connect service");
        if (!mAppContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            if (DEBUG) LogHelper.d(TAG, "cannot connect service");
            mIsConnecting.set(false);
        }
    }


    private void delayConnectService() {
        AsyncHandler.getInstance().postDelayed(new Runnable() {
            @Override
            public void run() {
                tryToConnect();
            }
        }, 1000);
    }

    public void waitForConnected() {
        waitForConnected(-1);
    }

    public void waitForConnected(long timeoutMillis) {
        if ((Looper.myLooper() == Looper.getMainLooper()) && timeoutMillis != 0) {
            throw new RuntimeException("Cannot be invoked in UI thread");
        }
        if (mService != null) return;

        synchronized (mIsConnecting) {
            connectServiceIfNeeded();
            long timeElapsed = 0;
            while (true) {
                if (DEBUG) {
                    LogHelper.d(TAG, "mService : " + mService + ", mIsConnecting : " + mIsConnecting);
                }
                if (mService != null || !mIsConnecting.get()) {
                    return;
                }
                if (timeoutMillis >= 0 && timeoutMillis >= timeElapsed) {
                    return;
                }
                connectServiceIfNeeded();
                timeElapsed += 100;
                SystemClock.sleep(100);
            }
        }
    }

    public int add(int a, int b, boolean waitForConnected) throws RemoteException, ServiceNotAvailable {
        if (waitForConnected) {
            waitForConnected();
        } else {
            connectServiceIfNeeded();
        }
        if (mService != null) {
            return mService.add(a, b);
        } else {
            throw new ServiceNotAvailable();
        }
    }
}
