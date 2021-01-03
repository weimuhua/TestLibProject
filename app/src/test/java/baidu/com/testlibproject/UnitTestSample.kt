package baidu.com.testlibproject

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import baidu.com.testlibproject.test.ClassUnderTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

private const val FAKE_STRING = "HELLO_WORLD"

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class UnitTestSample {

    @Test
    fun readStringFromContext_LocalizedString() {
        val objectUnderTest = ClassUnderTest(ApplicationProvider.getApplicationContext())
        val result = objectUnderTest.getHelloWorldString()
        Assert.assertEquals(result, FAKE_STRING)
    }
}