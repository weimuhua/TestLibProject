#include <jni.h>
#include <string>

using namespace std;

extern "C" jstring
Java_com_example_nativelib_NativeLib_stringFromJNI(JNIEnv *env, jobject thiz) {
    string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" jint
Java_com_example_nativelib_NativeLib_addNative(JNIEnv *env, jobject thiz, jint a, jint b) {
    return a + b;
}

extern "C" jint
Java_com_example_nativelib_NativeLib_subtractNative(JNIEnv *env, jobject thiz, jint a, jint b) {
    return a - b;
}