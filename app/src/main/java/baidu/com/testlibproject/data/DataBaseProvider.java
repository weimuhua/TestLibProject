package baidu.com.testlibproject.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.LogHelper;

public class DataBaseProvider extends ContentProvider {

    private static final String TAG = "DataBaseProvider";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    @Override
    public boolean onCreate() {
        if (DEBUG) LogHelper.d(TAG, "DataBaseProvider onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DEBUG) {
            LogHelper.d(TAG, "DataBaseProvider query, uri : " + uri.toString()
                    + ", authority : " + uri.getAuthority());
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (DEBUG) LogHelper.d(TAG, "DataBaseProvider insert");
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG) LogHelper.d(TAG, "DataBaseProvider delete");
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (DEBUG) LogHelper.d(TAG, "DataBaseProvider update");
        return 0;
    }
}
