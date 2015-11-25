package baidu.com.testlibproject.data;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalConfigMgr {

    private static final String PREFS_FILE = "global_config";

    public static final String PREFS_KEY_USERNAME = "username";

    private static SharedPreferences sPreferences;
    private static SharedPreferences.Editor sEditor;

    private static SharedPreferences initSharedPreferences(Context cxt) {
        if (sPreferences == null) {
            sPreferences = cxt.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
            sEditor = sPreferences.edit();
        }
        return sPreferences;
    }

    public static void setUsername(Context cxt, String value) {
        initSharedPreferences(cxt);
        sEditor.putString(PREFS_KEY_USERNAME, value).commit();
    }

    public static String getUsername(Context cxt, String defValue) {
        SharedPreferences shared = initSharedPreferences(cxt);
        return shared.getString(PREFS_KEY_USERNAME, defValue);
    }
}
