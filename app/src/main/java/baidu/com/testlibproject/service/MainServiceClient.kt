package baidu.com.testlibproject.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Looper
import android.os.RemoteException
import android.os.SystemClock
import baidu.com.commontools.utils.AsyncHandler
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.FeatureConfig
import baidu.com.testlibproject.IMainService
import java.util.concurrent.atomic.AtomicBoolean

class MainServiceClient private constructor(cxt: Context) {

    private val mAppContext: Context = cxt.applicationContext
    private var mService: IMainService? = null
    private val mIsConnecting = AtomicBoolean(false)

    fun tryToConnect() {
        connectServiceIfNeeded()
    }

    private fun connectServiceIfNeeded() {
        if (mService != null && mService!!.asBinder().isBinderAlive) {
            if (DEBUG) LogHelper.d(TAG, "binder is alive...")
            return
        }

        mIsConnecting.set(true)

        val connection = object : ServiceConnection {

            private val mConnectLost = false

            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                if (DEBUG) LogHelper.d(TAG, "connected service")
                if (!mConnectLost) {
                    mService = IMainService.Stub.asInterface(service)
                }
            }

            override fun onServiceDisconnected(name: ComponentName) {
                if (DEBUG) LogHelper.d(TAG, "service disconnected")
                if (mConnectLost) return //avoid connect service multi times??
                mService = null
                mIsConnecting.set(false)

                delayConnectService()
            }
        }

        val intent = Intent(mAppContext, MainService::class.java)
        if (DEBUG) LogHelper.d(TAG, "try connect service")
        //此方法不能在Broadcast中调用，在Broadcast需要调用startService
        if (!mAppContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            if (DEBUG) LogHelper.d(TAG, "cannot connect service")
            mIsConnecting.set(false)
        }
    }


    private fun delayConnectService() {
        AsyncHandler.getInstance().postDelayed({ tryToConnect() }, 1000)
    }

    @JvmOverloads
    fun waitForConnected(timeoutMillis: Long = -1) {
        if (Looper.myLooper() == Looper.getMainLooper() && timeoutMillis != 0L) {
            throw RuntimeException("Cannot be invoked in UI thread")
        }
        if (mService != null) return

        synchronized(mIsConnecting) {
            connectServiceIfNeeded()
            var timeElapsed: Long = 0
            while (true) {
                if (DEBUG) {
                    LogHelper.d(TAG, "mService : $mService, mIsConnecting : $mIsConnecting")
                }
                if (mService != null || !mIsConnecting.get()) {
                    return
                }
                if (timeoutMillis >= 0 && timeoutMillis >= timeElapsed) {
                    return
                }
                connectServiceIfNeeded()
                timeElapsed += 100
                SystemClock.sleep(100)
            }
        }
    }

    @Throws(RemoteException::class, ServiceNotAvailable::class)
    fun add(a: Int, b: Int, waitForConnected: Boolean): Int {
        if (waitForConnected) {
            waitForConnected()
        } else {
            connectServiceIfNeeded()
        }
        return if (mService != null) {
            mService!!.add(a, b)
        } else {
            throw ServiceNotAvailable()
        }
    }

    @Throws(RemoteException::class, ServiceNotAvailable::class)
    fun getSubInterfaceA(waitForConnected: Boolean): IBinder {
        if (waitForConnected) {
            waitForConnected()
        } else {
            connectServiceIfNeeded()
        }
        return if (mService != null) {
            mService!!.interfaceA
        } else {
            throw ServiceNotAvailable()
        }
    }

    @Throws(RemoteException::class, ServiceNotAvailable::class)
    fun getSubInterfaceB(waitForConnected: Boolean): IBinder {
        if (waitForConnected) {
            waitForConnected()
        } else {
            connectServiceIfNeeded()
        }
        return if (mService != null) {
            mService!!.interfaceB
        } else {
            throw ServiceNotAvailable()
        }
    }

    @Throws(RemoteException::class, ServiceNotAvailable::class)
    fun getSubInterfaceC(waitForConnected: Boolean): IBinder {
        if (waitForConnected) {
            waitForConnected()
        } else {
            connectServiceIfNeeded()
        }
        return if (mService != null) {
            mService!!.interfaceC
        } else {
            throw ServiceNotAvailable()
        }
    }

    companion object {

        private val TAG = "MainServiceClient"
        private val DEBUG = FeatureConfig.DEBUG

        private var sInstance: MainServiceClient? = null

        fun getInstance(cxt: Context): MainServiceClient {
            if (sInstance == null) {
                synchronized(MainServiceClient::class.java) {
                    if (sInstance == null) {
                        sInstance = MainServiceClient(cxt)
                    }
                }
            }
            return sInstance!!
        }
    }
}
