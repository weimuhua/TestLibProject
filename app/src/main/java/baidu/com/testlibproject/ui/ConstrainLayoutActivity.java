package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import baidu.com.testlibproject.R;

public class ConstrainLayoutActivity extends Activity {

    private static final String[] TEST_PROGRAM_ARRAY = {
            "相对位置demo",
            "Margin demo",
            "bias demo",
            "ratio demo",
            "chains demo",
            "guideline demo"
    };

    private Spinner mTestSpinner;
    private TextView mSpinnerTv;
    private FrameLayout mFrameLayout;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constrain_layout);
        mContext = this;

        mTestSpinner = findViewById(R.id.spinner);
        mSpinnerTv = findViewById(R.id.spinner_tv);
        mFrameLayout = findViewById(R.id.frame_layout);

        mSpinnerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestSpinner.performClick();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TEST_PROGRAM_ARRAY);
        mTestSpinner.setAdapter(adapter);
        mTestSpinner.setSelection(0);
        mTestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpinnerTv.setText(TEST_PROGRAM_ARRAY[position]);
                mFrameLayout.removeAllViews();
                if (id == 0) {
                    addConstraintBasic();
                } else if (id == 1) {
                    addConstraintGoneMargin();
                } else if (id == 2) {
                    addConstraintBias();
                } else if (id == 3) {
                    addConstraintRatio();
                } else if (id == 4) {
                    addConstraintChains();
                } else if (id == 5) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinnerTv.setText("");
            }
        });
    }

    private void addConstraintBasic() {
        View subView = LayoutInflater.from(mContext).inflate(R.layout.layout_constraint_basic, mFrameLayout, false);
        mFrameLayout.addView(subView);
    }

    private void addConstraintGoneMargin() {
        View subView = LayoutInflater.from(mContext).inflate(R.layout.layout_constraint_margin, mFrameLayout, false);
        Button normalMarginBtn = subView.findViewById(R.id.normal_margin_btn);
        final TextView normalTvB = subView.findViewById(R.id.normal_tv_b);
        normalMarginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (normalTvB.getVisibility() == View.VISIBLE) {
                    normalTvB.setVisibility(View.GONE);
                } else {
                    normalTvB.setVisibility(View.VISIBLE);
                }
            }
        });

        Button goneMarginBtn = subView.findViewById(R.id.gone_margin_btn);
        final TextView goneTvB = subView.findViewById(R.id.gone_tv_b);
        goneMarginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goneTvB.getVisibility() == View.VISIBLE) {
                    goneTvB.setVisibility(View.GONE);
                } else {
                    goneTvB.setVisibility(View.VISIBLE);
                }
            }
        });
        mFrameLayout.addView(subView);
    }

    private void addConstraintBias() {
        View subView = LayoutInflater.from(mContext).inflate(R.layout.layout_constraint_bias, mFrameLayout, false);
        mFrameLayout.addView(subView);
    }

    private void addConstraintRatio() {
        View subView = LayoutInflater.from(mContext).inflate(R.layout.layout_constraint_ratio, mFrameLayout, false);
        mFrameLayout.addView(subView);
    }

    private void addConstraintChains() {
        View subView = LayoutInflater.from(mContext).inflate(R.layout.layout_constraint_chains, mFrameLayout, false);
        mFrameLayout.addView(subView);
    }
}
