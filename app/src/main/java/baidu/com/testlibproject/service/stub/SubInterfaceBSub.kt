package baidu.com.testlibproject.service.stub

import android.os.RemoteException

import baidu.com.testlibproject.ISubInterfaceB

/**
 * Created by Wayne on 2017/3/24.
 */

class SubInterfaceBSub : ISubInterfaceB.Stub() {
    @Throws(RemoteException::class)
    override fun methodA2() {

    }

    @Throws(RemoteException::class)
    override fun methodB2() {

    }

    @Throws(RemoteException::class)
    override fun methodC2() {

    }
}
