package baidu.com.testlibproject.sensor;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import baidu.com.testlibproject.R;

public class LocationMgrActivity extends Activity implements LocationListener, AdapterView.OnItemClickListener {

    private ListView mListView;
    private TextView mLocationTv;

    private LocationManager mLocationMgr;
    private List<String> mProviderNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_mgr_layout);
        initView();
        initData();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.location_provider_listview);
        mLocationTv = (TextView) findViewById(R.id.location_textview);
    }

    private void initData() {
        mLocationMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        mProviderNameList = mLocationMgr.getAllProviders();
        if (mProviderNameList != null && !mProviderNameList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                    mProviderNameList.toArray(new String[mProviderNameList.size()]));
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updateTextView(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        try {
            Location location = mLocationMgr.getLastKnownLocation(provider);
            updateTextView(location);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        updateTextView(null);
    }

    @Override
    public void onBackPressed() {
        try {
            mLocationMgr.removeUpdates(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String provider = mProviderNameList.get(position);
            Location location = mLocationMgr.getLastKnownLocation(provider);
            updateTextView(location);
            Toast.makeText(this, "update location info by " + provider, Toast.LENGTH_SHORT).show();

            mLocationMgr.removeUpdates(this);
            mLocationMgr.requestLocationUpdates(provider, 3000, 8, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updateTextView(Location location) {
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("实时位置信息：\n");
            sb.append("经度： ").append(location.getLongitude());
            sb.append("\n维度: ").append(location.getLatitude());
            sb.append("\n高度: ").append(location.getAltitude());
            sb.append("\n速度: ").append(location.getSpeed());
            sb.append("\n方向: ").append(location.getBearing());
            mLocationTv.setText(sb.toString());
        } else {
            mLocationTv.setText("");
        }
    }
}
