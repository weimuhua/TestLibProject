package baidu.com.testlibproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class StationDbFactory implements DbFactory {

    @Override
    public SQLiteDatabase createDb(Context cxt) {
        return new StationDbHelper(cxt).getWritableDatabase();
    }
}