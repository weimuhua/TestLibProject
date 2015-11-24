package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import baidu.com.testlibproject.R;

public class ConfigurationTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_test_layout);
        initData();
    }

    private void initData() {
        Configuration config = getResources().getConfiguration();
        String screen = config.orientation == Configuration.ORIENTATION_PORTRAIT ? "竖屏" : "横屏";
        Toast.makeText(this, screen, Toast.LENGTH_LONG).show();
    }
}
