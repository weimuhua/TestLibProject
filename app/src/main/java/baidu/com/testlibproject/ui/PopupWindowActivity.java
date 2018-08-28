package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import baidu.com.testlibproject.FeatureConfig;
import baidu.com.commontools.utils.LogHelper;
import baidu.com.testlibproject.R;

public class PopupWindowActivity extends Activity implements ImageView.OnClickListener {

    private static final String TAG = "PopupWindowActivity";
    private static final boolean DEBUG = FeatureConfig.DEBUG;

    private ImageView mSettingImg;
    private PopupWindow mPopupWindow;
    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_window_layout);
        initView();
    }

    private void initView() {
        mSettingImg = findViewById(R.id.titlebar_setting_img);
        mSettingImg.setOnClickListener(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        mContentView = inflater.inflate(R.layout.popup_window_inside_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mContentView.setLayoutParams(params);
        mPopupWindow = new PopupWindow(mContentView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mTextView1 = mContentView.findViewById(R.id.popup_window_inside_tv1);
        mTextView2 = mContentView.findViewById(R.id.popup_window_inside_tv2);
        mTextView3 = mContentView.findViewById(R.id.popup_window_inside_tv3);
        mTextView1.setOnClickListener(this);
        mTextView2.setOnClickListener(this);
        mTextView3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSettingImg) {
            showOrDismissPopupWindow();
        } else if (v == mTextView1) {
            Toast.makeText(this, getString(R.string.popup_window_inside_text1), Toast.LENGTH_SHORT).show();
            showOrDismissPopupWindow();
        } else if (v == mTextView2) {
            Toast.makeText(this, getString(R.string.popup_window_inside_text2), Toast.LENGTH_SHORT).show();
            showOrDismissPopupWindow();
        } else if (v == mTextView3) {
            Toast.makeText(this, getString(R.string.popup_window_inside_text3), Toast.LENGTH_SHORT).show();
            showOrDismissPopupWindow();
        }
    }

    private void showOrDismissPopupWindow() {
        if (mPopupWindow.isShowing()) {
            if (DEBUG) LogHelper.d(TAG, "mPopupWindow.dismiss()");
            mPopupWindow.dismiss();
        } else {
            if (DEBUG) LogHelper.d(TAG, "mPopupWindow.showAsDropDown");
            mPopupWindow.showAsDropDown(mSettingImg);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPopupWindow.isShowing()) mPopupWindow.dismiss();
        return super.onTouchEvent(event);
    }
}
