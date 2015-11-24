package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import baidu.com.testlibproject.R;

public class ConfigurationTest extends Activity implements Handler.Callback {

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_test_layout);
        mHandler = new Handler(this);
        initData();
    }

    private void initData() {
        Configuration config = getResources().getConfiguration();
        String screen = config.orientation == Configuration.ORIENTATION_PORTRAIT ? "竖屏" : "横屏";
        Toast.makeText(this, screen, Toast.LENGTH_LONG).show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        }, 5000);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
