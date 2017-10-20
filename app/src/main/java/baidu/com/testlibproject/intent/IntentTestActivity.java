package baidu.com.testlibproject.intent;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import baidu.com.commontools.threadpool.MhThreadPool;
import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.LogHelper;
import baidu.com.testlibproject.R;

public class IntentTestActivity extends Activity implements Handler.Callback, AdapterView.OnItemClickListener {

    private static final String TAG = "IntentTestActivity";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private static final int MSG_LOAD_CONTRACT_DATA_DONE = 1;

    private ListView mListView;

    private Handler mHandler;
    private List<String> mStrList;
    private List<String> mNumberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_test_layout);
        initView();
        initData();
    }

    private void initView() {
        mListView = findViewById(R.id.contract_list_view);
        mListView.setOnItemClickListener(this);
    }

    private void initData() {
        mHandler = new Handler(this);
        mStrList = new ArrayList<>();
        mNumberList = new ArrayList<>();
        MhThreadPool.getInstance().addUiTask(new Runnable() {
            @Override
            public void run() {
                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String displayName = cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME));
                        String number = cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER));
                        String content = displayName + " : " + number;
                        if (DEBUG) {
                            LogHelper.d(TAG, content);
                        }
                        mStrList.add(content);
                        mNumberList.add(number);
                    }
                    mHandler.sendEmptyMessage(MSG_LOAD_CONTRACT_DATA_DONE);
                    cursor.close();
                }
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LOAD_CONTRACT_DATA_DONE:
                String[] strArr = mStrList.toArray(new String[mStrList.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.main_activity_item, strArr);
                mListView.setAdapter(adapter);
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + mNumberList.get(position)));
        startActivity(intent);
    }
}
