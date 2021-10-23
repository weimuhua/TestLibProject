package baidu.com.testlibproject.intent

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.ContactsContract.CommonDataKinds
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import baidu.com.commontools.threadpool.MhThreadPool
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.FeatureConfig
import baidu.com.testlibproject.R

class IntentTestActivity : Activity(), Handler.Callback, OnItemClickListener {

    private lateinit var listView: ListView
    private lateinit var handler: Handler
    private val strList: MutableList<String> = mutableListOf()
    private var numberList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intent_test_layout)
        initView()
        initData()
    }

    private fun initView() {
        listView = findViewById(R.id.contract_list_view)
        listView.onItemClickListener = this
    }

    private fun initData() {
        handler = Handler(this)
        if (hasReadContactsPermission()) {
            readContacts()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE
            )
        }
    }

    private fun hasReadContactsPermission() =
        (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED)

    private fun readContacts() {
        MhThreadPool.getInstance().addUiTask {
            val cursor = contentResolver.query(
                CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val displayName =
                        cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME))
                    val number =
                        cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER))
                    val content = "$displayName : $number"
                    if (DEBUG) {
                        LogHelper.d(TAG, content)
                    }
                    strList.add(content)
                    numberList.add(number)
                }
                handler.sendEmptyMessage(MSG_LOAD_CONTRACT_DATA_DONE)
                cursor.close()
            }
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            MSG_LOAD_CONTRACT_DATA_DONE -> {
                val strArr = strList.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.main_activity_item, strArr)
                listView.adapter = adapter
            }
        }
        return false
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + numberList[position]))
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (hasReadContactsPermission()) {
                readContacts()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.no_permission_toast_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val TAG = "IntentTestActivity"
        private const val DEBUG = FeatureConfig.DEBUG
        private const val MSG_LOAD_CONTRACT_DATA_DONE = 1
        private const val REQUEST_CODE = 1001
    }
}