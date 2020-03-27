package baidu.com.testlibproject.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import baidu.com.commontools.utils.LogHelper;
import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.R;

public class TestActionBarActivity extends AppCompatActivity {

    private static final String TAG = "TestActionBarActivity";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_bar_layout);
        initView();
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (DEBUG) LogHelper.d(TAG, "onOptionsItemSelected : " + item.getTitle());
        return super.onOptionsItemSelected(item);
    }
}
