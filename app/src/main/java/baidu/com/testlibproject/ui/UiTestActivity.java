package baidu.com.testlibproject.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import baidu.com.testlibproject.R;
import baidu.com.testlibproject.SimpleAdapter;

public class UiTestActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final int INTENT_AUTO_COMPLETE_TEXTVIEW = 0;
    private static final int INTENT_TEST_GRID_VIEW = 1;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_test_layout);
        initView();
        initData();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
    }

    private void initData() {
        String[] strArr = getResources().getStringArray(R.array.ui_test_item);
        SimpleAdapter adapter = new SimpleAdapter(this, strArr);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case INTENT_AUTO_COMPLETE_TEXTVIEW:
                startActivity(new Intent(this, TestAutoCompleteTvActivity.class));
                break;
            case INTENT_TEST_GRID_VIEW:
                startActivity(new Intent(this, TestGridViewActivity.class));
                break;
            default:
                break;
        }
    }
}
