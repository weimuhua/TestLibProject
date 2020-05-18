package baidu.com.testlibproject.test

import android.content.Context

class KotlinSingleton private constructor(private val context: Context) {

    companion object {
        private var instance: KotlinSingleton? = null

        fun getInstance(context: Context): KotlinSingleton {
            val i = instance
            if (i != null) {
                return i
            }

            return synchronized(this) {
                val i2 = instance
                if (i2 != null) {
                    i2
                } else {
                    val created = KotlinSingleton(context)
                    instance = created
                    created
                }
            }
        }
    }

    fun gogogo() {
        context.applicationInfo.packageName
    }
}