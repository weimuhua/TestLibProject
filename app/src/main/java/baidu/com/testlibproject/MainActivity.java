package baidu.com.testlibproject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import baidu.com.commontools.threadpool.MhThreadPool;
import baidu.com.commontools.utils.FileUtils;
import baidu.com.commontools.utils.LogHelper;
import baidu.com.testlibproject.db.StationDbFactory;
import baidu.com.testlibproject.dex.DexOptimizer;
import baidu.com.testlibproject.intent.IntentTestActivity;
import baidu.com.testlibproject.plugin.PluginActivity;
import baidu.com.testlibproject.sensor.CameraActivity;
import baidu.com.testlibproject.sensor.CompassActivity;
import baidu.com.testlibproject.sensor.LocationMgrActivity;
import baidu.com.testlibproject.service.AudioMgrActivity;
import baidu.com.testlibproject.service.MainServiceClient;
import baidu.com.testlibproject.service.ServiceNotAvailable;
import baidu.com.testlibproject.service.SmsMgrActivity;
import baidu.com.testlibproject.service.TelephonyMgrActivity;
import baidu.com.testlibproject.service.VibratorActivity;
import baidu.com.testlibproject.ui.UiTestActivity;
import dalvik.system.DexClassLoader;
import me.wayne.annotation.PluginCenterHolder;

@PluginCenterHolder
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivityTAG";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private static final int INTENT_TEST_UI_ACTIVITY = 0;
    private static final int INTENT_TEST_INTENT_ACTIVITY = 1;
    private static final int INTENT_TELEPHONY_MANAGER = 2;
    private static final int INTENT_SMS_MANAGER = 3;
    private static final int INTENT_AUDIO_MANAGER = 4;
    private static final int INTENT_VIBRATOR_ACTIVITY = 5;
    private static final int INTENT_COMPASS_ACTIVITY = 6;
    private static final int INTENT_LOCATION_MANAGER = 7;
    private static final int INTENT_CAMERA_ACTIVITY = 8;
    private static final int INTENT_PLUGIN_ACTIVITY = 9;

    private Context mContext;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initView();
        initData();
        testProvider();

        MhThreadPool.getInstance().addBkgTask(() -> {
            try {
                File apkFile = new File(mContext.getCacheDir() + "/mobileqq_android.apk");
                if (!apkFile.exists()) {
                    File srcFile = new File(Environment.getExternalStorageDirectory() + "/mobileqq_android.apk");
                    if (srcFile.exists()) {
                        FileUtils.copy(srcFile, apkFile);
                    }
                }
                if (!apkFile.exists()) {
                    Log.e(TAG, "apk file not exists, return");
                    return;
                }

                List<File> list = new ArrayList<>();
                list.add(apkFile);
                File optimizedDir = mContext.getDir("cache", MODE_PRIVATE);

                DexOptimizer.ResultCallback callback = new DexOptimizer.ResultCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "optimize onSuccess");
                        new DexClassLoader(apkFile.getAbsolutePath(), optimizedDir.getAbsolutePath(),
                                null, mContext.getClassLoader());
                        Log.d(TAG, "create DexClassLoader done.");
                    }

                    @Override
                    public void onFailed(Throwable thr) {
                        Log.e(TAG, "optimize fail", thr);
                    }
                };


                //use ClassLoader, get cache file
//                new DexOptimizer().optimizeDexByClassLoader(mContext, apkFile, optimizedDir);


                //user shell command, get cache file
                //实践证明，用命令行来执行dex2oat得到的oat文件小了很多，需要研究下为何
//                new DexOptimizer().optimizeDexByShellCommand(list, optimizedDir, callback);


                //user DexFile
                new DexOptimizer().optimizeDexByDexFile(list, optimizedDir, callback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DEBUG) LogHelper.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) LogHelper.d(TAG, "onResume");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (DEBUG) LogHelper.d(TAG, "onWindowFocusChanged, hasFocus : " + hasFocus);
    }


    private void initView() {
        mListView = findViewById(R.id.list_view);
    }

    private void initData() {
        String[] strArr = getResources().getStringArray(R.array.activity_item);
        SimpleAdapter adapter = new SimpleAdapter(mContext);
        adapter.setStrArr(strArr);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        MhThreadPool.getInstance().addBkgTask(() -> {
            try {
                int result = MainServiceClient.getInstance(mContext).add(2, 3, true);
                if (DEBUG) LogHelper.d(TAG, "Service add, result : " + result);

                IBinder subBinderA = MainServiceClient.getInstance(mContext).getSubInterfaceA(true);
                ISubInterfaceA subInterfaceA = ISubInterfaceA.Stub.asInterface(subBinderA);
                subInterfaceA.methodA1();
                subInterfaceA.methodB1();
                subInterfaceA.methodC1();
                if (DEBUG) {
                    LogHelper.d(TAG, "complete invoke SubInterfaceA!");
                }
            } catch (RemoteException | ServiceNotAvailable e) {
                if (DEBUG) LogHelper.e(TAG, "Exception : ", e);
            }
        });
    }

    private void testProvider() {
        ClassLoader classLoader = getClassLoader();
        if (DEBUG) {
            LogHelper.d(TAG, "classLoader = " + classLoader + " hashCode = " + classLoader.hashCode());
        }


        ContentResolver resolver = mContext.getContentResolver();
        Uri url = Uri.withAppendedPath(Constants.DB_AUTHORITY_URI, StationDbFactory.class.getName() + "/" + "test");
        resolver.query(url, null, null, null, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case INTENT_TEST_UI_ACTIVITY:
                startActivity(new Intent(mContext, UiTestActivity.class));
                break;
            case INTENT_TEST_INTENT_ACTIVITY:
                startActivity(new Intent(mContext, IntentTestActivity.class));
                break;
            case INTENT_TELEPHONY_MANAGER:
                startActivity(new Intent(mContext, TelephonyMgrActivity.class));
                break;
            case INTENT_SMS_MANAGER:
                startActivity(new Intent(mContext, SmsMgrActivity.class));
                break;
            case INTENT_AUDIO_MANAGER:
                startActivity(new Intent(mContext, AudioMgrActivity.class));
                break;
            case INTENT_VIBRATOR_ACTIVITY:
                startActivity(new Intent(mContext, VibratorActivity.class));
                break;
            case INTENT_COMPASS_ACTIVITY:
                startActivity(new Intent(mContext, CompassActivity.class));
                break;
            case INTENT_LOCATION_MANAGER:
                startActivity(new Intent(mContext, LocationMgrActivity.class));
                break;
            case INTENT_CAMERA_ACTIVITY:
                startActivity(new Intent(mContext, CameraActivity.class));
                break;
            case INTENT_PLUGIN_ACTIVITY:
                startActivity(new Intent(mContext, PluginActivity.class));
                break;
            default:
                break;
        }
    }
}
