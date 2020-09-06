package baidu.com.testlibproject.service

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.ArrayAdapter
import android.widget.ListView
import baidu.com.testlibproject.R
import java.util.*

class TelephonyMgrActivity : Activity() {

    private var mListView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.telephony_mgr_layout)
        initView()
        initData()
    }

    private fun initView() {
        mListView = findViewById(R.id.telephony_listview)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("HardwareIds")
    private fun initData() {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val desArr = resources.getStringArray(R.array.telephony_arr)
        val tmStateList = ArrayList<String>()
        tmStateList.add(tm.deviceId)
        tmStateList.add(tm.deviceSoftwareVersion)
        tmStateList.add(tm.networkOperatorName)
        tmStateList.add(tm.cellLocation.toString())
        tmStateList.add(tm.simCountryIso)
        tmStateList.add(tm.simSerialNumber)
        val size = desArr.size.coerceAtMost(tmStateList.size)
        val strArr = arrayOfNulls<String>(size)
        for (i in 0 until size) {
            strArr[i] = desArr[i] + tmStateList[i]
        }
        val adapter = ArrayAdapter<String>(this, R.layout.main_activity_item, strArr)
        mListView!!.adapter = adapter
    }
}
