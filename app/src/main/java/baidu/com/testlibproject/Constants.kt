package baidu.com.testlibproject

import android.net.Uri

object Constants {
    val PACKAGE_NAME = "baidu.com.testlibproject"
    val BKG_PROCESS_NAME = "$PACKAGE_NAME:bkg"
    val DB_AUTHORITY = "$PACKAGE_NAME.db.provider"
    val FILE_AUTHORITY = "$PACKAGE_NAME.file.provider"
    val DB_AUTHORITY_URI = Uri.parse("content://$DB_AUTHORITY")
    val FILE_AUTHORITY_URI = Uri.parse("content://$FILE_AUTHORITY")
}
