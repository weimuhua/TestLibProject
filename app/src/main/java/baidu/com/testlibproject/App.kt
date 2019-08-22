package baidu.com.testlibproject

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.service.MainService
import baidu.com.testlibproject.service.MainServiceClient

class App : Application() {

    private val processType: Int
        get() {
            return when (appProcessName) {
                Constants.PACKAGE_NAME -> PROCESS_UI
                Constants.BKG_PROCESS_NAME -> PROCESS_BKG
                else -> PROCESS_UNKNOWN
            }
        }

    private val appProcessName: String?
        get() {
            val pid = Process.myPid()
            val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningApps = am.runningAppProcesses
            if (runningApps != null && runningApps.isNotEmpty()) {
                for (info in runningApps) {
                    if (info.pid == pid) return info.processName
                }
            }

            return null
        }

    override fun onCreate() {
        super.onCreate()
        if (DEBUG) LogHelper.d(TAG, "Application onCreate, process : " + appProcessName!!)

        LogHelper.setTag("TestLibProject")

        when (processType) {
            PROCESS_UI -> MainServiceClient.getInstance(this).tryToConnect()
            PROCESS_BKG -> {
                val intent = Intent(this, MainService::class.java)
                startService(intent)
            }
        }

        getActivityCallback()
    }

    private fun getActivityCallback() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityDestroyed(p0: Activity) {
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
                if (DEBUG) {
                    LogHelper.i(TAG, "onActivitySaveInstanceState, activity = $p0")
                }
            }

            override fun onActivityStopped(p0: Activity) {
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                if (DEBUG) {
                    LogHelper.i(TAG, "onActivityCreated, activity = $p0")
                }
            }

            override fun onActivityResumed(p0: Activity) {
            }
        })
    }

    companion object {

        private const val TAG = "ProjectApp"
        private const val DEBUG = FeatureConfig.DEBUG

        private const val PROCESS_UNKNOWN = 0
        private const val PROCESS_UI = 1
        private const val PROCESS_BKG = 2
    }
}
