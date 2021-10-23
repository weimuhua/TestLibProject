package baidu.com.testlibproject.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import baidu.com.testlibproject.R
import java.util.*

class TelephonyMgrActivity : Activity() {

    private lateinit var listView: ListView
    private val requestCode = 1001
    private val permissionArr = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.telephony_mgr_layout)
        initView()
        initData()
    }

    private fun initView() {
        listView = findViewById(R.id.telephony_listview)
    }

    private fun initData() {
        if (hasPermissions()) {
            readTelephonyMessage()
        } else {
            ActivityCompat.requestPermissions(this, permissionArr, requestCode)
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("HardwareIds", "MissingPermission")
    private fun readTelephonyMessage() {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val desArr = resources.getStringArray(R.array.telephony_arr)
        val tmStateList = ArrayList<String>()
        tmStateList.add(tm.deviceSoftwareVersion ?: "")
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
        listView.adapter = adapter
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (hasPermissions()) {
                readTelephonyMessage()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.no_permission_toast_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun hasPermissions() = (hasPermission(Manifest.permission.READ_PHONE_STATE)
            && hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
}
