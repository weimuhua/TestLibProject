package baidu.com.testlibproject.coroutines

import android.util.Log
import kotlinx.coroutines.*

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

fun coroutinesExample3() {
    repeat(100_000) {
        // 启动大量的协程
        GlobalScope.launch {
            delay(200L)
            Log.d(TAG, "currentThread: ${Thread.currentThread().name}")
        }
    }
}

fun coroutinesExample4() {
    Log.d(TAG,"1")
    val job = GlobalScope.launch(context = Dispatchers.Main, start = CoroutineStart.LAZY) {
        delay(1000L)
        Log.d(TAG,"2")
        Log.d(TAG, "currentThread: ${Thread.currentThread().name}")
    }
    Log.d(TAG,"3")
    job.start() // 如果不调用这个方法，那么job的协程体就不会被调度执行
    Log.d(TAG,"4")
}

@ExperimentalCoroutinesApi
fun coroutinesExample5() {
    Log.d(TAG,"1")
    GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
        Log.d(TAG,"2")
        Log.d(TAG, "currentThread: ${Thread.currentThread().name}")
        delay(1000L)
        Log.d(TAG,"3")
        Log.d(TAG, "currentThread: ${Thread.currentThread().name}")
    }
    Log.d(TAG,"4")
    Log.d(TAG,"5")
}