package baidu.com.testlibproject.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import baidu.com.testlibproject.BuildConfig;
import baidu.com.testlibproject.LogHelper;
import baidu.com.testlibproject.R;
import baidu.com.testlibproject.plugin.hook.AMSHookHelper;
import dalvik.system.DexClassLoader;

public class PluginActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "PluginActivity";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            AMSHookHelper.hookActivityManagerNative();
            AMSHookHelper.hookActivityThreadHandler();
        } catch (Throwable throwable) {
            throw new RuntimeException("hook failed", throwable);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        initView();
    }

    private void initView() {
        findViewById(R.id.load_jar_tv).setOnClickListener(this);
        findViewById(R.id.start_activity_tv).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.load_jar_tv) {
            testLoadJarFile();
        } else if (v.getId() == R.id.start_activity_tv ) {
            startActivity(new Intent(this, TargetActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            AMSHookHelper.stopHookActivityManagerNative();
            AMSHookHelper.stopHookActivityThreadHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
