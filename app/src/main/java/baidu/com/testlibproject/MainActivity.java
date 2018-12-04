package baidu.com.testlibproject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import baidu.com.commontools.threadpool.MhThreadPool;
import baidu.com.commontools.utils.LogHelper;
import baidu.com.testlibproject.db.StationDbFactory;
import baidu.com.testlibproject.intent.IntentTestActivity;
import baidu.com.testlibproject.plugin.PluginActivity;
import baidu.com.testlibproject.sensor.CameraActivity;
import baidu.com.testlibproject.sensor.CompassActivity;
import baidu.com.testlibproject.sensor.LocationMgrActivity;
import baidu.com.testlibproject.service.AudioMgrActivity;
import baidu.com.testlibproject.service.MainServiceClient;
import baidu.com.testlibproject.service.ServiceNotAvailable;
import baidu.com.testlibproject.service.SmsMgrActivity;
import baidu.com.testlibproject.service.TelephonyMgrActivity;
import baidu.com.testlibproject.service.VibratorActivity;
import baidu.com.testlibproject.ui.UiTestActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity|TAG";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private static final String EMOJI_REGEX = "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)";

    private static final int INTENT_TEST_UI_ACTIVITY = 0;
    private static final int INTENT_TEST_INTENT_ACTIVITY = 1;
    private static final int INTENT_TELEPHONY_MANAGER = 2;
    private static final int INTENT_SMS_MANAGER = 3;
    private static final int INTENT_AUDIO_MANAGER = 4;
    private static final int INTENT_VIBRATOR_ACTIVITY = 5;
    private static final int INTENT_COMPASS_ACTIVITY = 6;
    private static final int INTENT_LOCATION_MANAGER = 7;
    private static final int INTENT_CAMERA_ACTIVITY = 8;
    private static final int INTENT_PLUGIN_ACTIVITY = 9;

    private Context mContext;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initView();
        initData();
        testProvider();
    }

    private void testClassLoader() {
        ClassLoader classLoader = getClassLoader();
        if (classLoader != null) {
            Log.i(TAG, "[onCreate] classLoader " + " : " + classLoader.toString());
            while (classLoader.getParent() != null) {
                classLoader = classLoader.getParent();
                Log.i(TAG, "[onCreate] classLoader " + " : " + classLoader.toString());
            }
        }
    }

    private void testMatchEmoji() {
        ((TextView) findViewById(R.id.textview)).setText(String.valueOf(Character.toChars(Integer.parseInt("1F601", 16))));

        int i0 = 0x1f60a;
        int i1 = 0x1f60c;
        int i2 = 0x1f61a;
        int i3 = 0x1f613;
        int i4 = 0x1f630;
        int i5 = 0x1f61d;
        int i6 = 0x1f601;
        int i7 = 0x1f61c;
        int i8 = 0x263a;
        int i9 = 0x1f609;
        int[] intArr = new int[10];
        intArr[0] = i0;
        intArr[1] = i1;
        intArr[2] = i2;
        intArr[3] = i3;
        intArr[4] = i4;
        intArr[5] = i5;
        intArr[6] = i6;
        intArr[7] = i7;
        intArr[8] = i8;
        intArr[9] = i9;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < intArr.length; i++) {
            sb.append("嘿").append(String.valueOf(Character.toChars(intArr[i])));
        }
        LogHelper.d(TAG, "sb " + sb.toString());

        long time = System.currentTimeMillis();
        Pattern pattern = Pattern.compile(EMOJI_REGEX, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sb);
        LogHelper.d(TAG, "cost " + (System.currentTimeMillis() - time));

        while (matcher.find()) {
            LogHelper.d(TAG, "find " + matcher.group());
        }
        LogHelper.d(TAG, "cost " + (System.currentTimeMillis() - time));
    }

    private void testRuntimeMemory() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        LogHelper.d(TAG, "totalMemory=" + getValue(totalMemory));
        LogHelper.d(TAG, "freeMemory=" + getValue(freeMemory));
        LogHelper.d(TAG, "maxMemory=" + getValue(maxMemory));
    }

    private String getValue(long value) {
        return Long.toString(value / (1024));
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
        mListView = findViewById(R.id.list_view);
    }

    private void initData() {
        String[] strArr = getResources().getStringArray(R.array.activity_item);
        SimpleAdapter adapter = new SimpleAdapter(mContext);
        adapter.setStrArr(strArr);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        MhThreadPool.getInstance().addBkgTask(() -> {
            try {
                int result = MainServiceClient.getInstance(mContext).add(2, 3, true);
                if (DEBUG) LogHelper.d(TAG, "Service add, result : " + result);

                IBinder subBinderA = MainServiceClient.getInstance(mContext).getSubInterfaceA(true);
                ISubInterfaceA subInterfaceA = ISubInterfaceA.Stub.asInterface(subBinderA);
                subInterfaceA.methodA1();
                subInterfaceA.methodB1();
                subInterfaceA.methodC1();
                if (DEBUG) {
                    LogHelper.d(TAG, "complete invoke SubInterfaceA!");
                }
            } catch (RemoteException | ServiceNotAvailable e) {
                if (DEBUG) LogHelper.e(TAG, "Exception : ", e);
            }
        });
    }

    private void testProvider() {
        ContentResolver resolver = mContext.getContentResolver();
        Uri url = Uri.withAppendedPath(Constants.DB_AUTHORITY_URI, StationDbFactory.class.getName() + "/" + "test");
        resolver.query(url, null, null, null, null);
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
            case INTENT_TELEPHONY_MANAGER:
                startActivity(new Intent(mContext, TelephonyMgrActivity.class));
                break;
            case INTENT_SMS_MANAGER:
                startActivity(new Intent(mContext, SmsMgrActivity.class));
                break;
            case INTENT_AUDIO_MANAGER:
                startActivity(new Intent(mContext, AudioMgrActivity.class));
                break;
            case INTENT_VIBRATOR_ACTIVITY:
                startActivity(new Intent(mContext, VibratorActivity.class));
                break;
            case INTENT_COMPASS_ACTIVITY:
                startActivity(new Intent(mContext, CompassActivity.class));
                break;
            case INTENT_LOCATION_MANAGER:
                startActivity(new Intent(mContext, LocationMgrActivity.class));
                break;
            case INTENT_CAMERA_ACTIVITY:
                startActivity(new Intent(mContext, CameraActivity.class));
                break;
            case INTENT_PLUGIN_ACTIVITY:
                startActivity(new Intent(mContext, PluginActivity.class));
                break;
            default:
                break;
        }
    }
}
