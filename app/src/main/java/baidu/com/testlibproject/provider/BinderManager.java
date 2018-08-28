package baidu.com.testlibproject.provider;

import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import baidu.com.commontools.utils.LogHelper;
import baidu.com.testlibproject.BuildConfig;

/**
 * Created by Wayne on 2017/3/26.
 * 此类用于在后台进程中管理binder实体集合
 */
public class BinderManager {

    private static final String TAG = "BinderManager";
    private static final boolean DEBUG = BuildConfig.DEBUG_LOG;

    private static final Map<String, IBinder> sBinderMap = new HashMap<>();
    private static final Map<String, Class<? extends Binder>> sSubMap = new HashMap<>();

    public static void addBinder(@BinderHelper.BinderName @NonNull String name,
            @NonNull Class<? extends Binder> clazz) {
        if (DEBUG) {
            LogHelper.d(TAG, "addBinder " + clazz);
        }
        sSubMap.put(name, clazz);
    }

    @Nullable
    static IBinder getBinder(@BinderHelper.ProcessName @NonNull String name) {
        if (DEBUG) {
            LogHelper.d(TAG, "getBinder " + name);
        }
        synchronized (sBinderMap) {
            IBinder binder = sBinderMap.get(name);
            if (binder == null) {
                try {
                    Class<? extends Binder> clazz = sSubMap.get(name);
                    binder = clazz.newInstance();
                    sBinderMap.put(name, binder);
                } catch (Exception e) {
                    if (DEBUG) {
                        throw new RuntimeException("cannot create Binder instance.");
                    }
                }
            }
            return binder;
        }
    }
}
