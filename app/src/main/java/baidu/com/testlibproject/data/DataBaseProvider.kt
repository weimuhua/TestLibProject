package baidu.com.testlibproject.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.text.TextUtils
import android.util.Pair
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.FeatureConfig
import baidu.com.testlibproject.db.DatabaseMgr

class DataBaseProvider : ContentProvider() {

    private var mContext: Context? = null

    override fun onCreate(): Boolean {
        if (DEBUG) LogHelper.d(TAG, "DataBaseProvider onCreate")
        mContext = context
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        if (DEBUG) LogHelper.d(TAG, "DataBaseProvider query, uri : $uri")
        val pair = parseUri(uri)
        DatabaseMgr.getInstance(mContext).getDatabase(pair!!.first)
        DatabaseMgr.getInstance(mContext).releaseDatabase(pair.first)
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (DEBUG) LogHelper.d(TAG, "DataBaseProvider insert")
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if (DEBUG) LogHelper.d(TAG, "DataBaseProvider delete")
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        if (DEBUG) LogHelper.d(TAG, "DataBaseProvider update")
        return 0
    }

    private fun parseUri(uri: Uri?): Pair<String, String>? {
        if (uri == null) return null

        val segments = uri.pathSegments
        if (segments == null || segments.isEmpty()) return null

        if (segments.size == 2) {
            val className = segments[0]
            val tableName = segments[1]
            if (!TextUtils.isEmpty(className) && !TextUtils.isEmpty(tableName)) {
                if (DEBUG) {
                    LogHelper.d(TAG, "parseUri, className : $className, tableName : $tableName")
                }
                return Pair(className, tableName)
            }
        }
        return null
    }

    companion object {

        private val TAG = "DataBaseProvider"
        private val DEBUG = FeatureConfig.DEBUG
    }
}