package baidu.com.testlibproject.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object GlobalConfigMgr {

    private val PREFS_FILE = "global_config"

    val PREFS_KEY_USERNAME = "username"

    private var sPreferences: SharedPreferences? = null
    private var sEditor: SharedPreferences.Editor? = null

    @SuppressLint("CommitPrefEdits")
    private fun initSharedPreferences(cxt: Context): SharedPreferences {
        if (sPreferences == null) {
            sPreferences = cxt.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
            sEditor = sPreferences!!.edit()
        }
        return sPreferences!!
    }

    fun setUsername(cxt: Context, value: String) {
        initSharedPreferences(cxt)
        sEditor!!.putString(PREFS_KEY_USERNAME, value).commit()
    }

    fun getUsername(cxt: Context, defValue: String): String? {
        val shared = initSharedPreferences(cxt)
        return shared.getString(PREFS_KEY_USERNAME, defValue)
    }
}
