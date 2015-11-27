package baidu.com.testlibproject;

import android.net.Uri;

public class FeatureConfig {
    public static final boolean DEBUG = BuildConfig.DEBUG_LOG;
    public static final String AUTHORITY = "baidu.com.testlibproject.db.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
}
