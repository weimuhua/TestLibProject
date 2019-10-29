@file:Suppress("ConstantConditionIf")

package baidu.com.testlibproject.coroutines

import android.os.Bundle
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.FeatureConfig
import baidu.com.testlibproject.ISubInterfaceA
import baidu.com.testlibproject.R
import baidu.com.testlibproject.service.MainServiceClient
import baidu.com.testlibproject.service.ServiceNotAvailable
import kotlinx.coroutines.*

class CoroutinesActivity : AppCompatActivity() {

    private val ioCoroutinesScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines)

        ioCoroutinesScope.launch {
            testService()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ioCoroutinesScope.cancel()
    }

    private suspend fun testService() {
        if (DEBUG) {
            LogHelper.d(TAG, "This code runs in Coroutines, current thread = " + Thread.currentThread())
        }
        try {
            val result = MainServiceClient.getInstance(this).add(2, 3, true)
            if (DEBUG) LogHelper.d(TAG, "Service add, result : $result")

            val subBinderA = MainServiceClient.getInstance(this).getSubInterfaceA(true)
            val subInterfaceA = ISubInterfaceA.Stub.asInterface(subBinderA)
            subInterfaceA.methodA1()
            subInterfaceA.methodB1()
            subInterfaceA.methodC1()
            delay(1000L)
            if (DEBUG) {
                LogHelper.d(TAG, "complete invoke SubInterfaceA!")
            }
        } catch (e: RemoteException) {
            if (DEBUG) LogHelper.e(TAG, "Exception : ", e)
        } catch (e: ServiceNotAvailable) {
            if (DEBUG) LogHelper.e(TAG, "Exception : ", e)
        }
    }

    companion object {
        private const val TAG = "CoroutinesActivityTAG"
        private const val DEBUG = FeatureConfig.DEBUG
    }
}
