package baidu.com.testlibproject.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.IMainService
import baidu.com.testlibproject.service.stub.SubInterfaceAStub
import baidu.com.testlibproject.service.stub.SubInterfaceBSub
import baidu.com.testlibproject.service.stub.SubInterfaceCSub

class MainProcessService : Service() {

    private val binder = object : IMainService.Stub() {
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

    override fun onBind(intent: Intent?): IBinder? {
        LogHelper.i(LOG_TAG, "onBind")
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        LogHelper.i(LOG_TAG, "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogHelper.i(LOG_TAG, "onDestroy")
    }

    companion object {
        private const val LOG_TAG = "MainProcessService"
    }
}