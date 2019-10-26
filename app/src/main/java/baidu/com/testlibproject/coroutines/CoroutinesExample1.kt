package baidu.com.testlibproject.coroutines

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val TAG = "CoroutinesExample"

fun coroutinesExample1() {
    GlobalScope.launch {
        delay(1000L)
        println("World!")
    }
    println("Hello,") // 主线程中的代码会立即执行
    runBlocking {     // 但是这个表达式阻塞了主线程
        delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
    }
}

fun coroutinesExample2() {
    Log.d(TAG,"1")
    GlobalScope.launch {
        Log.d(TAG,"2")
        Log.d(TAG, "currentThread: ${Thread.currentThread().name}")
    }
    Log.d(TAG,"3")
    Log.d(TAG,"4")
}