package baidu.com.testlibproject.plugin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import baidu.com.testlibproject.BuildConfig;
import baidu.com.testlibproject.LogHelper;
import baidu.com.testlibproject.R;
import dalvik.system.DexClassLoader;

public class PluginActivity extends Activity {

    private static final String TAG = "PluginActivity";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        testLoadJarFile();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressWarnings({"unchecked", "ConfusingArgumentToVarargsMethod"})
    private void testLoadJarFile() {
        //编译出jar包之后，需要注意，要调用./dx --dex --output=target.jar Secret.jar
        File jarFile = new File("/data/local/tmp/target.jar");

        File dexOutputDir = getDir("dex", 0);
        ClassLoader classLoader = new DexClassLoader(jarFile.getAbsolutePath(), dexOutputDir.getAbsolutePath(),
                null, getClassLoader());
        try {
            Class clazz = classLoader.loadClass("me.wayne.Main");
            Constructor constructor = clazz.getConstructor();
            Object obj = constructor.newInstance();

            Method helloMethod = clazz.getMethod("getMessage", null);
            helloMethod.setAccessible(true);
            Object content = helloMethod.invoke(obj, null);
            if (DEBUG) {
                LogHelper.d(TAG, "content : " + content);
            }
            Toast.makeText(this, content.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception exception) {
            if (DEBUG) {
                LogHelper.w(TAG, "Exception ", exception);
            }
        }
    }
}
