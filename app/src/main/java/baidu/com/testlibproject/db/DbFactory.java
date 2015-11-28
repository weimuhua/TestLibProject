package baidu.com.testlibproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public interface DbFactory {
    SQLiteDatabase createDb(Context cxt);
}
