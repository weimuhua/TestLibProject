package baidu.com.testlibproject.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.LogHelper;

/**
 * Created by Wayne on 2017/3/26.
 */

public class BinderProvider extends ContentProvider {

    private static final String TAG = "BinderProvider";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private Context mContext;

    @Override
    public boolean onCreate() {
        if (DEBUG) {
            LogHelper.d(TAG, "BinderProvider onCreate!");
        }
        mContext = getContext();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
            @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        if (DEBUG) {
            LogHelper.d(TAG, "BinderProvider call, method : " + method + " , arg : " + arg);
        }
        return super.call(method, arg, extras);
    }
}
