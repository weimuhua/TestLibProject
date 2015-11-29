package baidu.com.testlibproject.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;

import java.util.List;

import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.LogHelper;
import baidu.com.testlibproject.db.DatabaseMgr;

public class DataBaseProvider extends ContentProvider {

    private static final String TAG = "DataBaseProvider";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private Context mContext;

    @Override
    public boolean onCreate() {
        if (DEBUG) LogHelper.d(TAG, "DataBaseProvider onCreate");
        mContext = getContext();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DEBUG) LogHelper.d(TAG, "DataBaseProvider query, uri : " + uri.toString());
        Pair<String, String> pair = parseUri(uri);
        DatabaseMgr.getInstance(mContext).getDatabase(pair.first);
        DatabaseMgr.getInstance(mContext).releaseDatabase(pair.first);
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

    private Pair<String, String> parseUri(Uri uri) {
        if (uri == null) return null;

        List<String> segments = uri.getPathSegments();
        if (segments == null || segments.isEmpty()) return null;

        if (segments.size() == 2) {
            String className = segments.get(0);
            String tableName = segments.get(1);
            if (!TextUtils.isEmpty(className) && !TextUtils.isEmpty(tableName)) {
                if (DEBUG) {
                    LogHelper.d(TAG, "parseUri, className : " + className + ", tableName : " + tableName);
                }
                return new Pair<>(className, tableName);
            }
        }
        return null;
    }
}