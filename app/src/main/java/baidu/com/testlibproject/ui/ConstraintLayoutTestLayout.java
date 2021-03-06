package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FrameMetrics;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import baidu.com.testlibproject.R;
import baidu.com.testlibproject.SimpleAdapter;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ConstraintLayoutTestLayout extends LinearLayout implements View.OnClickListener {

    private static final String TAG = ConstraintLayoutTestLayout.class.getSimpleName();

    private static final int COUNT = 2000;

    private RelativeLayout mRelativeLayout;
    private ConstraintLayout mConstraintLayout;
    private ListView mRLListView;
    private SimpleAdapter mRLAdapter;
    private ListView mCLListView;
    private SimpleAdapter mCLAdapter;

    private String[] mRLStringArr;
    private String[] mCLStringArr;

    private Activity mActivity;

    private int mCount;
    private long mTotalCount;


    private Handler mWorkHandler;
    private Window.OnFrameMetricsAvailableListener mMetricsAvailableListener
            = new Window.OnFrameMetricsAvailableListener() {
        @Override
        public void onFrameMetricsAvailable(Window window, FrameMetrics frameMetrics, int dropCountSinceLastInvocation) {
            final long duration = frameMetrics.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION);
            mCount++;
            mTotalCount += duration;
            long avg = mCount == 0 ? mTotalCount : (mTotalCount / mCount);
            Log.d(TAG, "cur : " + duration + " avg : " + avg);
        }
    };

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

        HandlerThread thread = new HandlerThread("addOnFrameMetricsAvailableListener");
        thread.start();
        mWorkHandler = new Handler(thread.getLooper());

        inflate(getContext(), R.layout.constraint_test_layout, this);

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


        if (mActivity != null) {
            mActivity.getWindow().addOnFrameMetricsAvailableListener(mMetricsAvailableListener, mWorkHandler);
        }
    }

    public void testRelativeLayout() {
        mConstraintLayout.setVisibility(GONE);
        mRelativeLayout.setVisibility(VISIBLE);
        initRLListView();
    }

    public void testConstraintLayout() {
        mRelativeLayout.setVisibility(GONE);
        mConstraintLayout.setVisibility(VISIBLE);
        initCLListView();
    }

    @Override
    public void onClick(View v) {
    }

    private void initRLListView() {
        mRLListView.setSelection(0);
        mRLListView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "smoothScrollToPosition");
                mCount = 0;
                mTotalCount = 0;
                mRLListView.smoothScrollToPosition(mRLListView.getCount() - 1);
            }
        });
    }

    private void initCLListView() {
        mCLListView.setSelection(0);
        mCLListView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "smoothScrollToPosition");
                mCount = 0;
                mTotalCount = 0;
                mCLListView.smoothScrollToPosition(mCLListView.getCount() - 1);
            }
        });
    }
}
