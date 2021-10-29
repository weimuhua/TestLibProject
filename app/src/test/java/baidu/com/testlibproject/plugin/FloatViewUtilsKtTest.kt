package baidu.com.testlibproject.plugin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class FloatViewUtilsKtTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun uploadPic0_shouldNotUploadPic() {
        Assert.assertFalse(needUploadOcrBitmap(Mockito.anyLong(), VALUE_NOT_UPLOAD_PIC))
    }

    @Test
    fun uploadPic1_shouldUploadPic() {
        Assert.assertTrue(needUploadOcrBitmap(Mockito.anyLong(), VALUE_UPLOAD_PIC_EVERY_TIME))
    }

    @Test
    fun uploadPicNegative_shouldNotUploadPic() {
        Assert.assertFalse(needUploadOcrBitmap(Mockito.anyLong(), -1))
    }

    @Test
    fun uploadPicX_shouldCompareTranslateCount1() {
        Assert.assertFalse(needUploadOcrBitmap(10, 3))
    }

    @Test
    fun uploadPicX_shouldCompareTranslateCount() {
        Assert.assertTrue(needUploadOcrBitmap(12, 3))
    }
}