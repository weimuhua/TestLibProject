package baidu.com.testlibproject.plugin

const val VALUE_NOT_UPLOAD_PIC = 0
const val VALUE_UPLOAD_PIC_EVERY_TIME = 1

/***
 * 0：关闭上报图片
 * 1：每次都上报图片
 * x：其他数字，表示每隔多少次上报图片
 */
fun needUploadOcrBitmap(translateCount: Long, uploadPic: Int): Boolean {
    return when {
        VALUE_NOT_UPLOAD_PIC == uploadPic -> {
            false
        }
        VALUE_UPLOAD_PIC_EVERY_TIME == uploadPic -> {
            true
        }
        uploadPic < 0 -> {
            false
        }
        else -> {
            translateCount % uploadPic == 0L
        }
    }
}