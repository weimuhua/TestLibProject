package baidu.com.testlibproject.test

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.delay

class KotlinSingleton private constructor(private val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: KotlinSingleton? = null

        fun getInstance(context: Context): KotlinSingleton {
            return instance ?: synchronized(this) {
                instance ?: KotlinSingleton(context).also {
                    instance = it
                }
            }
        }
    }

    fun gogogo() {
        context.applicationInfo.packageName
    }

    suspend fun <T> retry(times: Int, block: suspend () -> Int): Int {
        repeat(times - 1) {
            val result = block()
            if (result == 0) {
                return 0
            } else {
                delay(500L)
            }
        }
        return block()
    }
}