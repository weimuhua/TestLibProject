package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import baidu.com.testlibproject.R;

public class TestSpinnerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner_layout);
        initView();
    }

    private void initView() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] strArr = new String[]{
                "Baidu", "AliBaBa", "Tencent", "JingDong"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.main_activity_item, strArr);
        spinner.setAdapter(adapter);
    }
}
