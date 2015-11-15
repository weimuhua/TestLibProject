package baidu.com.testlibproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;

import baidu.com.commontools.http.HttpUtils;
import baidu.com.commontools.threadpool.MhThreadPool;
import baidu.com.testlibproject.ui.UiTestActivity;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private static final String URL = "http://www.baidu.com";
    private static final int INTENT_UI_ACTIVITY = 0;

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
            case INTENT_UI_ACTIVITY:
                startActivity(new Intent(mContext, UiTestActivity.class));
                break;
            default:
                break;
        }

    }
}
