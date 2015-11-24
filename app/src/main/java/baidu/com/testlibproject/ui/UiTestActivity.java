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
    private static final int INTENT_TEST_GRID_VIEW_ACTIVITY = 1;
    private static final int INTENT_TEST_SPINNER_ACTIVITY = 2;
    private static final int INTENT_TEST_TOAST_ACTIVITY = 3;
    private static final int INTENT_TEST_DATE_TIME_PICKET = 4;
    private static final int INTENT_SEARCH_VIEW_ACTIVITY = 5;
    private static final int INTENT_NOTIFICATION_TEST = 6;
    private static final int INTENT_POPUP_WINDOW_ACTIVITY = 7;
    private static final int INTENT_ACTION_BAR_ACTIVITY = 8;

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
            case INTENT_TEST_GRID_VIEW_ACTIVITY:
                startActivity(new Intent(this, TestGridViewActivity.class));
                break;
            case INTENT_TEST_SPINNER_ACTIVITY:
                startActivity(new Intent(this, TestSpinnerActivity.class));
                break;
            case INTENT_TEST_TOAST_ACTIVITY:
                startActivity(new Intent(this, TestToastActivity.class));
                break;
            case INTENT_TEST_DATE_TIME_PICKET:
                startActivity(new Intent(this, DateTimePickerActivity.class));
                break;
            case INTENT_SEARCH_VIEW_ACTIVITY:
                startActivity(new Intent(this, SearchViewActivity.class));
                break;
            case INTENT_NOTIFICATION_TEST:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case INTENT_POPUP_WINDOW_ACTIVITY:
                startActivity(new Intent(this, PopupWindowActivity.class));
                break;
            case INTENT_ACTION_BAR_ACTIVITY:
                startActivity(new Intent(this, TestActionBarActivity.class));
                break;
            default:
                break;
        }
    }
}
