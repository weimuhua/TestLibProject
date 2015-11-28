package baidu.com.testlibproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StationDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "station.db";
    private static final int DB_VERSION = 1;

    public StationDbHelper(Context cxt) {
        super(cxt, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}