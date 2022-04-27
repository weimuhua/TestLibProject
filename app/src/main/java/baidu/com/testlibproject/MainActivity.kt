@file:Suppress("ConstantConditionIf", "DEPRECATION")

package baidu.com.testlibproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.RemoteException
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.audio.AudioRecordDemoActivity
import baidu.com.testlibproject.composeui.ComposeMainActivity
import baidu.com.testlibproject.coroutines.CoroutinesActivity
import baidu.com.testlibproject.coroutines.coroutinesExample4
import baidu.com.testlibproject.db.StationDbFactory
import baidu.com.testlibproject.intent.IntentTestActivity
import baidu.com.testlibproject.plugin.PluginActivity
import baidu.com.testlibproject.sensor.CameraActivity
import baidu.com.testlibproject.sensor.CompassActivity
import baidu.com.testlibproject.sensor.LocationMgrActivity
import baidu.com.testlibproject.service.*
import baidu.com.testlibproject.test.KotlinSingleton
import baidu.com.testlibproject.ui.UiTestActivity
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import me.wayne.annotation.PluginCenterHolder

@PluginCenterHolder
class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var context: Context
    private lateinit var listView: ListView
    private val coroutinesScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        KotlinSingleton.getInstance(this).gogogo()

        coroutinesExample4()

        initView()
        initData()
        testProvider()
        testMmkv()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutinesScope.cancel()
    }

    private fun initView() {
        listView = findViewById(R.id.list_view)
    }

    private fun initData() {
        val adapter = SimpleAdapter(context).apply {
            setStrArr(resources.getStringArray(R.array.activity_item))
        }
        listView.let {
            it.adapter = adapter
            it.onItemClickListener = this
        }

        coroutinesScope.launch {
            testService()
        }
    }

    private suspend fun testService() {
        if (DEBUG) {
            LogHelper.d(TAG, "This code runs in Coroutines, current thread = " + Thread.currentThread())
        }
        try {
            val result = MainServiceClient.getInstance(context).add(2, 3, true)
            if (DEBUG) LogHelper.d(TAG, "Service add, result : $result")

            val subBinderA = MainServiceClient.getInstance(context).getSubInterfaceA(true)
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
            LogHelper.d(
                TAG,
                "classLoader = " + classLoader + " hashCode = " + classLoader.hashCode()
            )
        }


        val resolver = context.contentResolver
        val url = Uri.withAppendedPath(
            Constants.DB_AUTHORITY_URI,
            StationDbFactory::class.java.name + "/" + "test"
        )
        resolver.query(url, null, null, null, null)
    }

    private fun testMmkv() {
        coroutinesScope.launch {
            MMKV.initialize(this@MainActivity)

            var time = System.currentTimeMillis()
            val sp = getSharedPreferences("legacy", MODE_PRIVATE)
            val editor = sp.edit()
            for (i in 0..1000) {
                editor.putString("index$i", i.toString())
            }
            editor.apply()
            LogHelper.d(TAG, "sp putString cost : ${System.currentTimeMillis() - time}")

            time = System.currentTimeMillis()
            val kv = MMKV.mmkvWithID("legacy_data")
            kv.importFromSharedPreferences(sp)
            editor.clear().commit()
            LogHelper.d(
                TAG,
                "importFromSharedPreferences cost : ${System.currentTimeMillis() - time}"
            )

            LogHelper.d(TAG, "testMmkv run")
            val kvInner = MMKV.mmkvWithID("inner")
            for (i in 0..1000) {
                kvInner.encode("index", i)
            }
            LogHelper.d(TAG, "cost: ${System.currentTimeMillis() - time}")

            time = System.currentTimeMillis()
            val kvIpc = MMKV.mmkvWithID("ipc", MMKV.MULTI_PROCESS_MODE)
            for (i in 0..1000) {
                kvIpc.encode("index", i)
            }
            LogHelper.d(TAG, "cost: ${System.currentTimeMillis() - time}")
        }
    }


    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (position) {
            INTENT_TEST_UI_ACTIVITY -> startActivity(Intent(context, UiTestActivity::class.java))
            INTENT_TEST_INTENT_ACTIVITY -> startActivity(
                Intent(
                    context,
                    IntentTestActivity::class.java
                )
            )
            INTENT_TELEPHONY_MANAGER -> startActivity(
                Intent(
                    context,
                    TelephonyMgrActivity::class.java
                )
            )
            INTENT_SMS_MANAGER -> startActivity(Intent(context, SmsMgrActivity::class.java))
            INTENT_AUDIO_MANAGER -> startActivity(Intent(context, AudioMgrActivity::class.java))
            INTENT_VIBRATOR_ACTIVITY -> startActivity(Intent(context, VibratorActivity::class.java))
            INTENT_COMPASS_ACTIVITY -> startActivity(Intent(context, CompassActivity::class.java))
            INTENT_LOCATION_MANAGER -> startActivity(
                Intent(
                    context,
                    LocationMgrActivity::class.java
                )
            )
            INTENT_CAMERA_ACTIVITY -> startActivity(Intent(context, CameraActivity::class.java))
            INTENT_PLUGIN_ACTIVITY -> startActivity(Intent(context, PluginActivity::class.java))
            INTENT_COROUTINES_ACTIVITY -> startActivity(
                Intent(
                    context,
                    CoroutinesActivity::class.java
                )
            )
            INTENT_COMPOSE_UI_ACTIVITY -> startActivity(
                Intent(
                    context,
                    ComposeMainActivity::class.java
                )
            )
            INTENT_AUDIO_RECORD_DEMO_ACTIVITY -> startActivity(
                Intent(
                    context,
                    AudioRecordDemoActivity::class.java
                )
            )
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
        private const val INTENT_COMPOSE_UI_ACTIVITY = 11
        private const val INTENT_AUDIO_RECORD_DEMO_ACTIVITY = 12
    }
}
