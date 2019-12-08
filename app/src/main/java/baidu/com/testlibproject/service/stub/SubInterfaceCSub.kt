package baidu.com.testlibproject.service.stub

import android.os.RemoteException

import baidu.com.testlibproject.ISubInterfaceC

/**
 * Created by Wayne on 2017/3/24.
 */

class SubInterfaceCSub : ISubInterfaceC.Stub() {
    @Throws(RemoteException::class)
    override fun methodA3() {

    }

    @Throws(RemoteException::class)
    override fun methodB3() {

    }

    @Throws(RemoteException::class)
    override fun methodC3() {

    }
}
