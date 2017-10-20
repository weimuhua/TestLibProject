package baidu.com.testlibproject.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import baidu.com.testlibproject.FeatureConfig;
import baidu.com.testlibproject.LogHelper;
import baidu.com.testlibproject.R;

public class CompassActivity extends Activity implements SensorEventListener {

    private static final String TAG = "CompassActivity";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private SensorManager mSensorManager;
    private Sensor mSensor1;
    private Sensor mSensor2;

    private ImageView mCompass;

    private float[] mAccValues;
    private float[] mMagValues;
    private float[] mValues;
    private float[] mR;
    private float mCurDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass_layout);
        initView();
        initData();
    }

    private void initView() {
        mCompass = findViewById(R.id.compass);
    }

    private void initData() {
        mAccValues = new float[3];
        mMagValues = new float[3];
        mValues = new float[3];
        mR = new float[9];
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor1 = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor2 = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor1, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensor2, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccValues = event.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagValues = event.values;
                break;
        }
        SensorManager.getRotationMatrix(mR, null, mAccValues, mMagValues);
        SensorManager.getOrientation(mR, mValues);
        if (DEBUG) {
            LogHelper.d(TAG, "orientation : " + (float) Math.toDegrees(mValues[0]));
        }
        rotateCompass((float) Math.toDegrees(mValues[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void rotateCompass(float toDegree) {
        RotateAnimation ra = new RotateAnimation(mCurDegree, -toDegree, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(200);
        mCompass.startAnimation(ra);
        mCurDegree = -toDegree;
    }
}
