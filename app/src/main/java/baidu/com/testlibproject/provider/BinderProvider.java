package baidu.com.testlibproject.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import baidu.com.commontools.utils.LogHelper;
import baidu.com.testlibproject.FeatureConfig;

/**
 * Created by Wayne on 2017/3/26.
 */

public class BinderProvider extends ContentProvider {

    private static final String TAG = "BinderProvider";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    public static final String GET_BINDER_METHOD = "get_binder_method";
    public static final String BINDER_KEY = "binder_key";

    @Override
    public boolean onCreate() {
        if (DEBUG) {
            LogHelper.d(TAG, "BinderProvider onCreate!");
        }
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
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        if (DEBUG) {
            LogHelper.d(TAG, "BinderProvider call, method : " + method + " , arg : " + arg);
        }
        Bundle bundle = new Bundle();
        if (GET_BINDER_METHOD.equals(method)) {
            IBinder iBinder = BinderManager.getBinder(arg);
            if (iBinder != null) {
                if (DEBUG) {
                    LogHelper.d(TAG, "get iBinder.");
                }
                bundle.putParcelable(BINDER_KEY, new BinderParcelable(iBinder));
            }
        }
        if (DEBUG) {
            LogHelper.d(TAG, "return bundle!");
        }
        return bundle;
    }
}
