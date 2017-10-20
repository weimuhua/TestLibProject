package baidu.com.testlibproject.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import baidu.com.testlibproject.BuildConfig;
import baidu.com.commontools.utils.LogHelper;
import baidu.com.testlibproject.R;
import baidu.com.testlibproject.plugin.hook.AMSHookHelper;
import baidu.com.testlibproject.plugin.hook.DexUtils;
import dalvik.system.DexClassLoader;

public class PluginActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "PluginActivity";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static ClassLoader sPluginClassloader;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            AMSHookHelper.hookActivityManagerNative();
            AMSHookHelper.hookActivityThreadHandler();
        } catch (Throwable ignore) {
            ignore.printStackTrace();
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
        findViewById(R.id.start_plugin_activity_tv).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.load_jar_tv) {
            testLoadApkFile();
        } else if (v.getId() == R.id.start_activity_tv) {
            startUnregisteredActivity();
        } else if (v.getId() == R.id.start_plugin_activity_tv) {
            startPluginActivity();
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

    @SuppressWarnings({"unchecked"})
    private void testLoadApkFile() {
        if (sPluginClassloader == null) {
            initPluginClassloader();
        }

        try {
            Class clazz = sPluginClassloader.loadClass("wayne.me.testapplication.Main");
            Constructor constructor = clazz.getConstructor();
            Object obj = constructor.newInstance();

            Method helloMethod = clazz.getMethod("getMessage");
            helloMethod.setAccessible(true);
            Object content = helloMethod.invoke(obj);
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

    private void initPluginClassloader() {
        if (sPluginClassloader == null) {
            //编译出apk之后，adb push app/build/outputs/apk/debug/app-debug.apk /data/local/tmp
            File jarFile = new File("/data/local/tmp/app-debug.apk");
            File dexOutputDir = getDir("dex", 0);
            sPluginClassloader = new DexClassLoader(jarFile.getAbsolutePath(),
                    dexOutputDir.getAbsolutePath(), null, getClassLoader());

            try {
                DexUtils.injectDexAtFirst(sPluginClassloader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startUnregisteredActivity() {
        startActivity(new Intent(this, TargetActivity.class));
    }

    private void startPluginActivity() {
        if (sPluginClassloader == null) {
            initPluginClassloader();
        }

        Intent intent = new Intent();
        intent.setClassName("wayne.me.testapplication", "wayne.me.testapplication.MainActivity");
        startActivity(intent);
    }
}
