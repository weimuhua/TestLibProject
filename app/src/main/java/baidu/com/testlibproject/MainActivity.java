package baidu.com.testlibproject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import baidu.com.commontools.threadpool.MhThreadPool;
import baidu.com.testlibproject.data.StationDbFactory;
import baidu.com.testlibproject.intent.IntentTestActivity;
import baidu.com.testlibproject.service.MainServiceClient;
import baidu.com.testlibproject.service.ServiceNotAvailable;
import baidu.com.testlibproject.ui.UiTestActivity;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

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
        testProvider();
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
        mListView = (ListView) findViewById(R.id.list_view);
    }

    private void initData() {
        String[] strArr = getResources().getStringArray(R.array.activity_item);
        SimpleAdapter adapter = new SimpleAdapter(mContext);
        adapter.setStrArr(strArr);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        MhThreadPool.getInstance().addBkgTask(new Runnable() {
            @Override
            public void run() {
                try {
                    int result = MainServiceClient.getInstance(mContext).add(2, 3, true);
                    if (DEBUG) LogHelper.d(TAG, "Service add, result : " + result);
                } catch (RemoteException e) {
                    if (DEBUG) LogHelper.e(TAG, "RemoteException : ", e);
                } catch (ServiceNotAvailable e) {
                    if (DEBUG) LogHelper.e(TAG, "ServiceNotAvailable : ", e);
                }
            }
        });
    }

    private void testProvider() {
        ContentResolver resolver = mContext.getContentResolver();
        Uri url = Uri.withAppendedPath(Constants.AUTHORITY_URI, StationDbFactory.class.getName() + "/" + "test");
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
            default:
                break;
        }

    }
}
