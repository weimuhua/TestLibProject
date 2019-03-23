package baidu.com.testlibproject.service.stub;

import android.os.RemoteException;
import android.util.Log;

import baidu.com.testlibproject.ISubInterfaceA;

/**
 * Created by Wayne on 2017/3/24.
 */

public class SubInterfaceAStub extends ISubInterfaceA.Stub {

    private static final String TAG = "SubInterfaceAStub";

    @Override
    public void methodA1() {
        Log.d(TAG, "methodA1 invoked!");
    }

    @Override
    public void methodB1() {
        Log.d(TAG, "methodB1 invoked!");
    }

    @Override
    public void methodC1() {
        Log.d(TAG, "methodC1 invoked!");
    }
}
