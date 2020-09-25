package baidu.com.testlibproject.ui

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.R
import baidu.com.testlibproject.service.MainProcessService

class TestAutoCompleteTvActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_auto_complete_tv_layout)

        bindMainService()
    }

    private fun bindMainService() {
        val intent = Intent(this, MainProcessService::class.java)
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                LogHelper.i(LOG_TAG, "onServiceConnected")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                LogHelper.i(LOG_TAG, "onServiceDisconnected")
            }

        }
        LogHelper.i(LOG_TAG, "bindService")
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    companion object {
        private const val LOG_TAG = "AutoCompleteTvTAG"
    }
}