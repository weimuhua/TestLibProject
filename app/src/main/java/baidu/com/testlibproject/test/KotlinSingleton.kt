package baidu.com.testlibproject.test

import android.content.Context

class KotlinSingleton private constructor(private val context: Context) {

    companion object {
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
}