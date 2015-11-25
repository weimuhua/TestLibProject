package baidu.com.testlibproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import baidu.com.testlibproject.intent.IntentTestActivity;
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
