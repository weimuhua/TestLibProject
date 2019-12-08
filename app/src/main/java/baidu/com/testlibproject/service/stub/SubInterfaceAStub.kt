package baidu.com.testlibproject.service.stub

import android.os.RemoteException
import android.util.Log

import baidu.com.testlibproject.ISubInterfaceA

/**
 * Created by Wayne on 2017/3/24.
 */

class SubInterfaceAStub : ISubInterfaceA.Stub() {

    @Throws(RemoteException::class)
    override fun methodA1() {
        Log.d(TAG, "methodA1 invoked!")
    }

    @Throws(RemoteException::class)
    override fun methodB1() {
        Log.d(TAG, "methodB1 invoked!")
    }

    @Throws(RemoteException::class)
    override fun methodC1() {
        Log.d(TAG, "methodC1 invoked!")
    }

    companion object {

        private val TAG = "SubInterfaceAStub"
    }
}
