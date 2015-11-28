package baidu.com.testlibproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public interface DbFactory {
    SQLiteDatabase createDb(Context cxt);
}
