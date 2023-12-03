#include <jni.h>
#include <string>

using namespace std;

extern "C" jstring
Java_com_example_nativelib_NativeLib_stringFromJNI(JNIEnv *env, jobject) {
    string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint
Java_com_example_nativelib_NativeLib_addNative(JNIEnv* env, jclass thiz, jint a, jint b) {
    return a + b;
}

extern "C" JNIEXPORT jint
Java_com_example_nativelib_NativeLib_subtractNative(JNIEnv* env, jclass thiz, jint a, jint b) {
    return a - b;
}