package baidu.com.testlibproject.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FrameMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import baidu.com.testlibproject.R;
import baidu.com.testlibproject.SimpleAdapter;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ConstraintLayoutTestLayout extends LinearLayout implements View.OnClickListener {

    private static final String TAG = ConstraintLayoutTestLayout.class.getSimpleName();

    private static final int COUNT = 1000;

    private Button mButton;
    private RelativeLayout mRelativeLayout;
    private ConstraintLayout mConstraintLayout;
    private ListView mRLListView;
    private SimpleAdapter mRLAdapter;
    private ListView mCLListView;
    private SimpleAdapter mCLAdapter;

    private TextView mCurDurationTv;
    private TextView mAvgDurationTv;

    private String[] mRLStringArr;
    private String[] mCLStringArr;

    private Activity mActivity;

    private int mCount;
    private long mTotalCount;

    private Handler mHandler = new Handler();

    public ConstraintLayoutTestLayout(Context context) {
        super(context);
        init();
    }

    public ConstraintLayoutTestLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConstraintLayoutTestLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (getContext() instanceof Activity) {
            mActivity = (Activity) getContext();
        }

        inflate(getContext(), R.layout.constraint_test_layout, this);

        mCurDurationTv = findViewById(R.id.cur_duration_tv);
        mAvgDurationTv = findViewById(R.id.avg_duration_tv);
        mButton = findViewById(R.id.switch_btn);
        mButton.setOnClickListener(this);

        mRelativeLayout = findViewById(R.id.relative_layout);
        mConstraintLayout = findViewById(R.id.constraint_layout);
        mRLListView = findViewById(R.id.rl_list_view);
        mCLListView = findViewById(R.id.cl_list_view);

        mRLStringArr = new String[COUNT];
        mCLStringArr = new String[COUNT];
        for (int i = 0; i < COUNT; i++) {
            mRLStringArr[i] = String.format("RelativeLayout ListView, 第%s行", i);
            mCLStringArr[i] = String.format("ConstraintLayout ListView, 第%s行", i);
        }

        mRLAdapter = new SimpleAdapter(getContext());
        mRLAdapter.setStrArr(mRLStringArr);
        mRLListView.setAdapter(mRLAdapter);

        mCLAdapter = new SimpleAdapter(getContext());
        mCLAdapter.setStrArr(mCLStringArr);
        mCLListView.setAdapter(mCLAdapter);

        initRLListView();

        if (mActivity != null) {
            HandlerThread thread = new HandlerThread("addOnFrameMetricsAvailableListener");
            thread.start();
            Handler handler = new Handler(thread.getLooper());
            mActivity.getWindow().addOnFrameMetricsAvailableListener(new Window.OnFrameMetricsAvailableListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onFrameMetricsAvailable(Window window, FrameMetrics frameMetrics, int dropCountSinceLastInvocation) {
                    final long layoutMeasureDuration = frameMetrics.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION);
                    mCount++;
                    mTotalCount += layoutMeasureDuration;

                    Log.d(TAG, "layoutMeasureDuration : " + layoutMeasureDuration +
                            " mCount : " + mCount + " mTotalCount : " + mTotalCount);
                    if (mCount % 10 == 0) {
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                long avg = mTotalCount / mCount;
//                                mCurDurationTv.setText("cur : " + layoutMeasureDuration);
//                                mAvgDurationTv.setText(String.format("avg : %s", avg));
//                            }
//                        });
                    }
                }
            }, handler);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mButton) {
            switchView();
        }
    }

    private void switchView() {
        if (mRelativeLayout.getVisibility() == View.VISIBLE) {
            mRelativeLayout.setVisibility(GONE);
            mRLListView.setVisibility(GONE);

            mConstraintLayout.setVisibility(VISIBLE);
            mCLListView.setVisibility(VISIBLE);

            initCLListView();
        } else {
            mRelativeLayout.setVisibility(VISIBLE);
            mRLListView.setVisibility(VISIBLE);

            mConstraintLayout.setVisibility(GONE);
            mCLListView.setVisibility(GONE);

            initRLListView();
        }
    }

    private void initRLListView() {
        mCount = 0;
        mTotalCount = 0;
        mRLListView.setSelection(0);
        mRLListView.post(new Runnable() {
            @Override
            public void run() {
                mRLListView.smoothScrollToPosition(mRLListView.getCount() - 1);
            }
        });
    }

    private void initCLListView() {
        mCount = 0;
        mTotalCount = 0;
        mCLListView.setSelection(0);
        mCLListView.post(new Runnable() {
            @Override
            public void run() {
                mCLListView.smoothScrollToPosition(mCLListView.getCount() - 1);
            }
        });
    }
}
