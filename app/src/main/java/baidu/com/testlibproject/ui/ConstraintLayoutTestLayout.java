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
import android.widget.AbsListView;
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

    private static final int COUNT = 2000;

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

    private boolean mIsScrolling = false;

    private Handler mWorkHandler;
    private Handler mMainHandler = new Handler();
    private Window.OnFrameMetricsAvailableListener mMetricsAvailableListener
            = new Window.OnFrameMetricsAvailableListener() {
        @Override
        public void onFrameMetricsAvailable(Window window, FrameMetrics frameMetrics, int dropCountSinceLastInvocation) {
            if (!mIsScrolling) return;
            final long duration = frameMetrics.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION);
            mCount++;
            mTotalCount += duration;
            Log.d(TAG, "duration : " + duration + " mCount : " + mCount
                    + " mTotalCount : " + mTotalCount);

            if (mCount % 10 == 0 || mCount > COUNT) {
                mMainHandler.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        long avg = mCount == 0 ? mTotalCount : (mTotalCount / mCount);

//                        Log.d(TAG, "duration : " + duration + " mCount : " + mCount
//                                + " mTotalCount : " + mTotalCount + " avg : " + avg);

                        mCurDurationTv.setText("cur : " + duration);
                        mAvgDurationTv.setText(String.format("avg : %s", avg));
                    }
                });
            }
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

        switchView();
        initCLListView();

        if (mActivity != null) {
            mActivity.getWindow().addOnFrameMetricsAvailableListener(mMetricsAvailableListener, mWorkHandler);
        }

        mRLListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mIsScrolling = scrollState != SCROLL_STATE_IDLE;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        mCLListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mIsScrolling = scrollState != SCROLL_STATE_IDLE;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
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

        if (mActivity != null) {
            mActivity.getWindow().addOnFrameMetricsAvailableListener(mMetricsAvailableListener, mWorkHandler);
        }
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
