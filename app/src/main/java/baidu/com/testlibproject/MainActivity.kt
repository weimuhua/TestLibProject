@file:Suppress("ConstantConditionIf", "DEPRECATION")

package baidu.com.testlibproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.coroutines.CoroutinesActivity
import baidu.com.testlibproject.coroutines.coroutinesExample4
import baidu.com.testlibproject.db.StationDbFactory
import baidu.com.testlibproject.intent.IntentTestActivity
import baidu.com.testlibproject.plugin.PluginActivity
import baidu.com.testlibproject.sensor.CameraActivity
import baidu.com.testlibproject.sensor.CompassActivity
import baidu.com.testlibproject.sensor.LocationMgrActivity
import baidu.com.testlibproject.service.*
import baidu.com.testlibproject.ui.UiTestActivity
import kotlinx.coroutines.*
import me.wayne.annotation.PluginCenterHolder

@PluginCenterHolder
class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var mContext: Context? = null

    private var mListView: ListView? = null

    private val coroutinesScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this

        coroutinesExample4()

        initView()
        initData()
        testProvider()
    }

    private fun initView() {
        mListView = findViewById(R.id.list_view)
    }

    private fun initData() {
        val strArr = resources.getStringArray(R.array.activity_item)
        val adapter = SimpleAdapter(mContext!!)
        adapter.setStrArr(strArr)
        mListView!!.adapter = adapter
        mListView!!.onItemClickListener = this

        coroutinesScope.launch {
            testService()
        }
    }

    private suspend fun testService() {
        if (DEBUG) {
            LogHelper.d(TAG, "This code runs in Coroutines, current thread = " + Thread.currentThread())
        }
        try {
            val result = MainServiceClient.getInstance(mContext).add(2, 3, true)
            if (DEBUG) LogHelper.d(TAG, "Service add, result : $result")

            val subBinderA = MainServiceClient.getInstance(mContext).getSubInterfaceA(true)
            val subInterfaceA = ISubInterfaceA.Stub.asInterface(subBinderA)
            subInterfaceA.methodA1()
            subInterfaceA.methodB1()
            subInterfaceA.methodC1()
            delay(1000L)
            if (DEBUG) {
                LogHelper.d(TAG, "complete invoke SubInterfaceA!")
            }
        } catch (e: RemoteException) {
            if (DEBUG) LogHelper.e(TAG, "Exception : ", e)
        } catch (e: ServiceNotAvailable) {
            if (DEBUG) LogHelper.e(TAG, "Exception : ", e)
        }
    }

    @SuppressLint("Recycle")
    private fun testProvider() {
        val classLoader = classLoader
        if (DEBUG) {
            LogHelper.d(TAG, "classLoader = " + classLoader + " hashCode = " + classLoader.hashCode())
        }


        val resolver = mContext!!.contentResolver
        val url = Uri.withAppendedPath(Constants.DB_AUTHORITY_URI, StationDbFactory::class.java.name + "/" + "test")
        resolver.query(url, null, null, null, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutinesScope.cancel()
    }


    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (position) {
            INTENT_TEST_UI_ACTIVITY -> startActivity(Intent(mContext, UiTestActivity::class.java))
            INTENT_TEST_INTENT_ACTIVITY -> startActivity(Intent(mContext, IntentTestActivity::class.java))
            INTENT_TELEPHONY_MANAGER -> startActivity(Intent(mContext, TelephonyMgrActivity::class.java))
            INTENT_SMS_MANAGER -> startActivity(Intent(mContext, SmsMgrActivity::class.java))
            INTENT_AUDIO_MANAGER -> startActivity(Intent(mContext, AudioMgrActivity::class.java))
            INTENT_VIBRATOR_ACTIVITY -> startActivity(Intent(mContext, VibratorActivity::class.java))
            INTENT_COMPASS_ACTIVITY -> startActivity(Intent(mContext, CompassActivity::class.java))
            INTENT_LOCATION_MANAGER -> startActivity(Intent(mContext, LocationMgrActivity::class.java))
            INTENT_CAMERA_ACTIVITY -> startActivity(Intent(mContext, CameraActivity::class.java))
            INTENT_PLUGIN_ACTIVITY -> startActivity(Intent(mContext, PluginActivity::class.java))
            INTENT_COROUTINES_ACTIVITY -> startActivity(Intent(mContext, CoroutinesActivity::class.java))
            else -> {
            }
        }
    }

    companion object {
        private const val TAG = "MainActivityTAG"
        private const val DEBUG = FeatureConfig.DEBUG

        private const val INTENT_TEST_UI_ACTIVITY = 0
        private const val INTENT_TEST_INTENT_ACTIVITY = 1
        private const val INTENT_TELEPHONY_MANAGER = 2
        private const val INTENT_SMS_MANAGER = 3
        private const val INTENT_AUDIO_MANAGER = 4
        private const val INTENT_VIBRATOR_ACTIVITY = 5
        private const val INTENT_COMPASS_ACTIVITY = 6
        private const val INTENT_LOCATION_MANAGER = 7
        private const val INTENT_CAMERA_ACTIVITY = 8
        private const val INTENT_PLUGIN_ACTIVITY = 9
        private const val INTENT_COROUTINES_ACTIVITY = 10
    }
}
