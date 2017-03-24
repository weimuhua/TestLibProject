// IMainService.aidl
package baidu.com.testlibproject;

// Declare any non-default types here with import statements

interface IMainService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int add(int a, int b);

    IBinder getInterfaceA();
    IBinder getInterfaceB();
    IBinder getInterfaceC();
}
