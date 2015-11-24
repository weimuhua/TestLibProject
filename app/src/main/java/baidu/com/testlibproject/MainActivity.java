package baidu.com.testlibproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import baidu.com.commontools.http.HttpUtils;
import baidu.com.commontools.threadpool.MhThreadPool;
import baidu.com.testlibproject.intent.IntentTestActivity;
import baidu.com.testlibproject.ui.UiTestActivity;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private static final String URL = "http://www.baidu.com";
    private static final int INTENT_TEST_UI_ACTIVITY = 0;
    private static final int INTENT_TEST_INTENT_ACTIVITY = 1;

    private Context mContext;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initView();
        initData();

        testHttp();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
    }

    private void initData() {
        String[] strArr = getResources().getStringArray(R.array.activity_item);
        SimpleAdapter adapter = new SimpleAdapter(mContext);
        adapter.setStrArr(strArr);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    private void testHttp() {
        MhThreadPool.getInstance().addUiTask(new Runnable() {
            @Override
            public void run() {
                try {
                    String resp = HttpUtils.commonGet(URL);
                    if (DEBUG) {
                        Log.d(TAG, "commonGet resp : " + resp);
                    }

                    PackageManager pm = mContext.getPackageManager();
                    List<PackageInfo> infoList = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
                    if (DEBUG) {
                        for (PackageInfo info : infoList) {
                            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                LogHelper.d(TAG, "info : " + info.packageName + ",appName : " + info.applicationInfo.loadLabel(pm));
                            }
                        }
                    }
                } catch (IOException e) {
                    if (DEBUG) {
                        LogHelper.w(TAG, "http exception", e);
                    }
                }
            }
        });
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
            default:
                break;
        }

    }
}
