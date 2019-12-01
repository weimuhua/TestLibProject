@file:Suppress("ConstantConditionIf")

package baidu.com.testlibproject.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.FeatureConfig
import baidu.com.testlibproject.IMainService
import baidu.com.testlibproject.service.stub.SubInterfaceAStub
import baidu.com.testlibproject.service.stub.SubInterfaceBSub
import baidu.com.testlibproject.service.stub.SubInterfaceCSub

class MainService : Service() {

    private val mBinder = object : IMainService.Stub() {
        override fun getInterfaceA(): IBinder {
            return SubInterfaceAStub()
        }

        override fun getInterfaceB(): IBinder {
            return SubInterfaceBSub()
        }

        override fun getInterfaceC(): IBinder {
            return SubInterfaceCSub()
        }

        override fun add(a: Int, b: Int): Int {
            return a + b
        }
    }

    override fun onCreate() {
        super.onCreate()
        val classLoader = classLoader
        if (DEBUG) {
            LogHelper.d(TAG, "classLoader = " + classLoader + " hashCode = " + classLoader.hashCode())
        }

        if (DEBUG) LogHelper.d(TAG, "MainService onCreate")
    }

    override fun onBind(intent: Intent): IBinder? {
        if (DEBUG) LogHelper.d(TAG, "MainService onBind")
        return mBinder
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        if (DEBUG) LogHelper.d(TAG, "onTaskRemoved")
    }

    companion object {

        private const val TAG = "MainService"
        private const val DEBUG = FeatureConfig.DEBUG
    }
}
