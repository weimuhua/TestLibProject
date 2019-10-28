@file:Suppress("ConstantConditionIf", "DEPRECATION")

package baidu.com.testlibproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import baidu.com.commontools.threadpool.MhThreadPool
import baidu.com.commontools.utils.FileUtils
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.coroutines.coroutinesExample4
import baidu.com.testlibproject.db.StationDbFactory
import baidu.com.testlibproject.dex.DexOptimizer
import baidu.com.testlibproject.intent.IntentTestActivity
import baidu.com.testlibproject.plugin.PluginActivity
import baidu.com.testlibproject.sensor.CameraActivity
import baidu.com.testlibproject.sensor.CompassActivity
import baidu.com.testlibproject.sensor.LocationMgrActivity
import baidu.com.testlibproject.service.*
import baidu.com.testlibproject.ui.UiTestActivity
import dalvik.system.DexClassLoader
import me.wayne.annotation.PluginCenterHolder
import java.io.File
import java.util.*

@PluginCenterHolder
class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var mContext: Context? = null

    private var mListView: ListView? = null

    private var redPoint: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this

        coroutinesExample4()

        initView()
        initData()
        testProvider()
        printExternalDir()

        MhThreadPool.getInstance().addBkgTask(object : Runnable {
            override fun run() {
                try {
                    val apkFile = File(mContext!!.cacheDir.toString() + "/mobileqq_android.apk")
                    if (!apkFile.exists()) {
                        val srcFile = File(Environment.getExternalStorageDirectory().toString()
                                + "/mobileqq_android.apk")
                        if (srcFile.exists()) {
                            FileUtils.copy(srcFile, apkFile)
                        }
                    }
                    if (!apkFile.exists()) {
                        Log.e(TAG, "apk file not exists, return")
                        return
                    }

                    val list = ArrayList<File>()
                    list.add(apkFile)
                    val optimizedDir = mContext!!.getDir("cache", Context.MODE_PRIVATE)

                    val callback = object : DexOptimizer.ResultCallback {
                        override fun onSuccess() {
                            Log.d(TAG, "optimize onSuccess")
                            DexClassLoader(apkFile.absolutePath, optimizedDir.absolutePath,
                                    null, mContext!!.classLoader)
                            Log.d(TAG, "create DexClassLoader done.")
                        }

                        override fun onFailed(thr: Throwable) {
                            Log.e(TAG, "optimize fail", thr)
                        }
                    }


                    //use ClassLoader, get cache file
                    //                new DexOptimizer().optimizeDexByClassLoader(mContext, apkFile, optimizedDir);


                    //user shell command, get cache file
                    //实践证明，用命令行来执行dex2oat得到的oat文件小了很多，需要研究下为何
                    //                new DexOptimizer().optimizeDexByShellCommand(list, optimizedDir, callback);


                    //user DexFile
                    DexOptimizer().optimizeDexByDexFile(list, optimizedDir, callback)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun initView() {
        mListView = findViewById(R.id.list_view)
    }

    private fun initData() {
        redPoint = findViewById(R.id.red_point)
        redPoint!!.post {
            val location = IntArray(2)
            redPoint!!.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]

            LogHelper.d(TAG, "redPoint getX = $x")
            LogHelper.d(TAG, "redPoint getY = $y")
        }

        val strArr = resources.getStringArray(R.array.activity_item)
        val adapter = SimpleAdapter(mContext!!)
        adapter.setStrArr(strArr)
        mListView!!.adapter = adapter
        mListView!!.onItemClickListener = this
        MhThreadPool.getInstance().addBkgTask {
            try {
                val result = MainServiceClient.getInstance(mContext).add(2, 3, true)
                if (DEBUG) LogHelper.d(TAG, "Service add, result : $result")

                val subBinderA = MainServiceClient.getInstance(mContext).getSubInterfaceA(true)
                val subInterfaceA = ISubInterfaceA.Stub.asInterface(subBinderA)
                subInterfaceA.methodA1()
                subInterfaceA.methodB1()
                subInterfaceA.methodC1()
                if (DEBUG) {
                    LogHelper.d(TAG, "complete invoke SubInterfaceA!")
                }
            } catch (e: RemoteException) {
                if (DEBUG) LogHelper.e(TAG, "Exception : ", e)
            } catch (e: ServiceNotAvailable) {
                if (DEBUG) LogHelper.e(TAG, "Exception : ", e)
            }
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

    private fun printExternalDir() {
        Log.i(TAG, "getExternalCacheDir = " + mContext!!.externalCacheDir!!.absolutePath)
        Log.i(TAG, "getExternalFilesDir = " + mContext!!.getExternalFilesDir(Environment.DIRECTORY_MUSIC)!!.absolutePath)
        Log.i(TAG, "getExternalFilesDir = " + mContext!!.getExternalFilesDir(Environment.DIRECTORY_PODCASTS)!!.absolutePath)
        Log.i(TAG, "getExternalFilesDir = " + mContext!!.getExternalFilesDir(Environment.DIRECTORY_RINGTONES)!!.absolutePath)
        Log.i(TAG, "getExternalFilesDir = " + mContext!!.getExternalFilesDir(Environment.DIRECTORY_ALARMS)!!.absolutePath)
        Log.i(TAG, "getExternalFilesDir = " + mContext!!.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS)!!.absolutePath)
        Log.i(TAG, "getExternalFilesDir = " + mContext!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath)
        Log.i(TAG, "getExternalFilesDir = " + mContext!!.getExternalFilesDir(Environment.DIRECTORY_MOVIES)!!.absolutePath)
        Log.i(TAG, "getExternalFilesDir = " + mContext!!.getExternalFilesDir(null)!!.absolutePath)
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
    }
}
