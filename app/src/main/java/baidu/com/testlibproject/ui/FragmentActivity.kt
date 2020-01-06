package baidu.com.testlibproject.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.R

class MyFragmentActivity : FragmentActivity() {

    class MyFragment : Fragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            LogHelper.i(LOG_TAG, "${hashCode()} onCreate")
        }

        override fun onDestroy() {
            super.onDestroy()
            LogHelper.i(LOG_TAG, "${hashCode()} onDestroy")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_my_fragment_activity)
    }

    override fun onResume() {
        super.onResume()

        replaceFragment()
    }

    private fun replaceFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, MyFragment())
        transaction.commitAllowingStateLoss()
    }

    companion object {
        private const val LOG_TAG = "MyFragmentActivityTAG"
    }
}