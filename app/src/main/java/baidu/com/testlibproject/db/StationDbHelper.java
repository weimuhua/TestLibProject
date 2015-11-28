package baidu.com.testlibproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.LogHelper;

public class StationDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "StationDbHelper";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private static final String DB_NAME = "station.db";
    private static final int DB_VERSION = 1;

    public StationDbHelper(Context cxt) {
        super(cxt, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (DEBUG) LogHelper.d(TAG, "StationDbHelper onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}