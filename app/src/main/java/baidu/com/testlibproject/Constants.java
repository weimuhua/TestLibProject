package baidu.com.testlibproject;

import android.net.Uri;

public class Constants {
    public static final String PACKAGE_NAME = "baidu.com.testlibproject";
    public static final String BKG_PROCESS_NAME = PACKAGE_NAME + ":bkg";
    public static final String DB_AUTHORITY = PACKAGE_NAME + ".db.provider";
    public static final String FILE_AUTHORITY = PACKAGE_NAME + ".file.provider";
    public static final Uri DB_AUTHORITY_URI = Uri.parse("content://" + DB_AUTHORITY);
    public static final Uri FILE_AUTHORITY_URI = Uri.parse("content://" + FILE_AUTHORITY);
}
