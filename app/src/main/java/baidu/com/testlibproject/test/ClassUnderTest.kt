package baidu.com.testlibproject.test

import android.content.Context
import baidu.com.testlibproject.R

class ClassUnderTest(private val context: Context) {

    fun getHelloWorldString(): String {
        return context.getString(R.string.hello_world_string)
    }
}