package baidu.com.testlibproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import java.util.HashMap;
import java.util.Map;

import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.LogHelper;

public class DatabaseMgr {

    private static final String TAG = "DatabaseMgr";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private static DatabaseMgr sInstance;

    private Context mAppContext;
    private Map<Class<? extends DbFactory>, DbInfo> mInfoMap;

    public static DatabaseMgr getInstance(Context cxt) {
        if (sInstance == null) {
            synchronized (DatabaseMgr.class) {
                if (sInstance == null) {
                    sInstance = new DatabaseMgr(cxt);
                }
            }
        }
        return sInstance;
    }

    private DatabaseMgr(Context cxt) {
        mAppContext = cxt.getApplicationContext();
        mInfoMap = new HashMap<>();
    }

    public SQLiteDatabase getDatabase(String className) {
        try {
            Class clazz = Class.forName(className);
            synchronized (DatabaseMgr.class) {
                DbInfo info = mInfoMap.get(clazz);
                if (info == null) {
                    info = new DbInfo();
                    DbFactory factory = (DbFactory) clazz.newInstance();
                    if (DEBUG) LogHelper.d(TAG, "create db : " + className);
                    info.db = factory.createDb(mAppContext);
                    mInfoMap.put(clazz, info);
                }
                info.refs++;
                return info.db;
            }
        } catch (Exception e) {
            if (DEBUG) {
                LogHelper.e(TAG, "getDatabase exception...", e);
            }
        }
        return null;
    }

    public void releaseDatabase(String className) {
        if (DEBUG) LogHelper.d(TAG, "release db : " + className);
        try {
            Class clazz = Class.forName(className);
            synchronized (DatabaseMgr.class) {
                DbInfo info = mInfoMap.get(clazz);
                if (info != null) {
                    info.refs--;
                    if (info.refs == 0 && info.db != null) {
                        info.db.close();
                        info.db = null;
                        mInfoMap.remove(clazz);
                    }
                }
            }
        } catch (Exception e) {
            if (DEBUG) {
                LogHelper.e(TAG, "releaseDatabase exception...", e);
            }
        }
    }
}
