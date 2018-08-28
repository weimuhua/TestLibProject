package baidu.com.testlibproject.service;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import baidu.com.testlibproject.R;

public class TelephonyMgrActivity extends Activity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telephony_mgr_layout);
        initView();
        initData();
    }

    private void initView() {
        mListView = findViewById(R.id.telephony_listview);
    }

    private void initData() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String[] desArr = getResources().getStringArray(R.array.telephony_arr);
        List<String> tmStateList = new ArrayList<>();
        tmStateList.add(tm.getDeviceId());
        tmStateList.add(tm.getDeviceSoftwareVersion());
        tmStateList.add(tm.getNetworkOperatorName());
        tmStateList.add(tm.getCellLocation().toString());
        tmStateList.add(tm.getSimCountryIso());
        tmStateList.add(tm.getSimSerialNumber());
        int size = Math.min(desArr.length, tmStateList.size());
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = desArr[i] + tmStateList.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.main_activity_item, strArr);
        mListView.setAdapter(adapter);
    }
}
