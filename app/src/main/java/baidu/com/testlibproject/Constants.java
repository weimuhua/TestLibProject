package baidu.com.testlibproject;

import android.net.Uri;

public class Constants {
    public static final String PACKAGE_NAME = "baidu.com.testlibproject";
    public static final String BKG_PROCESS_NAME = PACKAGE_NAME + ":bkg";
    public static final String AUTHORITY = "baidu.com.testlibproject.db.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
}
