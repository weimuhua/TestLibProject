package baidu.com.testlibproject.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.SparseArray
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.R
import java.util.*

class MyFragmentActivity : FragmentActivity() {

    class MyFragment : Fragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            LogHelper.i(LOG_TAG, "fragment ${hashCode()} onCreate")
        }

        override fun onDestroy() {
            super.onDestroy()
            LogHelper.i(LOG_TAG, "fragment ${hashCode()} onDestroy")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogHelper.i(LOG_TAG, "onCreate")
        setContentView(R.layout.layout_my_fragment_activity)
        savedInstanceState?.apply {
            clearFragments()
        }
    }

    override fun onResume() {
        super.onResume()
        LogHelper.i(LOG_TAG, "onResume")

        replaceFragment()
    }

    private fun replaceFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, MyFragment())
        transaction.commitAllowingStateLoss()
    }

    private fun clearFragments() {
        try {
            val fm = supportFragmentManager
            val f = fm.javaClass.getDeclaredField("mAdded")
            val f2 = fm.javaClass.getDeclaredField("mActive")
            f.isAccessible = true
            f2.isAccessible = true
            val fragments = f[fm] as ArrayList<Fragment>
            val fragments2 = f2[fm] as SparseArray<Fragment>
            fragments.clear()
            fragments2.clear()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val LOG_TAG = "MyFragmentActivityTAG"
    }
}